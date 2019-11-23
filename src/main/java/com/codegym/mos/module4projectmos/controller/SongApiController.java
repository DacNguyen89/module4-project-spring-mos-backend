package com.codegym.mos.module4projectmos.controller;

import com.codegym.mos.module4projectmos.model.entity.Artist;
import com.codegym.mos.module4projectmos.model.entity.Song;
import com.codegym.mos.module4projectmos.model.entity.User;
import com.codegym.mos.module4projectmos.service.ArtistService;
import com.codegym.mos.module4projectmos.service.SongService;
import com.codegym.mos.module4projectmos.service.impl.AudioStorageService;
import com.codegym.mos.module4projectmos.service.impl.DownloadService;
import com.codegym.mos.module4projectmos.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/song")
public class SongApiController {
    @Autowired
    SongService songService;

    @Autowired
    ArtistService artistService;

    @Autowired
    private AudioStorageService audioStorageService;

    @Autowired
    private DownloadService downloadService;

    @Autowired
    UserDetailServiceImpl userDetailService;

/*    @PostMapping("/upload")
    public ResponseEntity<Void> createSong(@RequestPart("song") Song song, @RequestPart("audio") MultipartFile multipartFile) {
        Collection<Artist> artists = song.getArtists();
        for (Artist artist : artists) {
            artistService.save(artist);
        }
        songService.save(song);
        String fileDownloadUri = audioStorageService.saveToFirebaseStorage(multipartFile, song);
        song.setUrl(fileDownloadUri);
        song.setUploader(userDetailService.getCurrentUser());
        songService.save(song);
        return new ResponseEntity<>(HttpStatus.OK);
    }*/

    @PostMapping("/upload")
    public ResponseEntity<Void> createSong(@RequestPart("song") Song song, @RequestPart("audio") MultipartFile file) {
        Collection<Artist> artists = song.getArtists();
        for (Artist artist : artists) {
            artistService.save(artist);
        }
        songService.save(song);
        String fileDownloadUri = audioStorageService.saveToFirebaseStorage(song, file);
        song.setUrl(fileDownloadUri);
        song.setUploader(userDetailService.getCurrentUser());
        songService.save(song);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadAudio(@PathVariable String fileName, HttpServletRequest request) {
        return downloadService.generateUrl(fileName, request, audioStorageService);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Song>> songList(Pageable pageable) {
        Page<Song> songList = songService.findAll(pageable);
        if (songList.getTotalElements() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else return new ResponseEntity<>(songList, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/uploaded/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Song>> userSongList(Pageable pageable) {
        User currentUser = userDetailService.getCurrentUser();
        Page<Song> userSongList = songService.findAllByUploader_Id(currentUser.getId(), pageable);
        boolean isEmpty = userSongList.getTotalElements() == 0;
        if (isEmpty) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else return new ResponseEntity<>(userSongList, HttpStatus.OK);
    }

    @GetMapping(value = "/detail")
    public ResponseEntity<Song> songDetail(@RequestParam Long id) {
        Optional<Song> song = songService.findById(id);
        return song.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
