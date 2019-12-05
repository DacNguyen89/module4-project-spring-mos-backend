package com.codegym.mos.module4projectmos.controller;

import com.codegym.mos.module4projectmos.model.entity.*;
import com.codegym.mos.module4projectmos.service.*;
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
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
    UserDetailServiceImpl userDetailService;

    @Autowired
    private AudioStorageService audioStorageService;

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

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
    public ResponseEntity<String> uploadSong(@RequestPart("song") Song song, @RequestPart("audio") MultipartFile file, @RequestParam(value = "album-id", required = false) Long id) {
        try {
            Song songToSave = songService.save(song);
            String fileDownloadUri = audioStorageService.saveToFirebaseStorage(songToSave, file);
            songToSave.setUrl(fileDownloadUri);
            songToSave.setUploader(userDetailService.getCurrentUser());
            if (id != null) {
                Optional<Album> album = albumService.findById(id);
                if (album.isPresent()) {
                    Collection<Song> songList = album.get().getSongs();
                    if (songList == null) {
                        songList = new ArrayList<>();
                    }
                    songList.add(song);
                    album.get().setSongs(songList);
                    albumService.save(album.get());
                }
            }
            songService.save(songToSave);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            if (song.getId() != null) {
                songService.deleteById(song.getId());
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*@PreAuthorize("isAuthenticated()")
    @PostMapping("/upload")
    public ResponseEntity<Void> uploadSong(@RequestPart("song") Song song, @RequestPart("audio") MultipartFile file, @RequestParam(value = "album-id", required = false) Long id) {
        try {
            Song songToSave = songService.save(song);
            String fileDownloadUri = audioStorageService.saveToFirebaseStorage(songToSave, file);
            songToSave.setUrl(fileDownloadUri);
            songToSave.setUploader(userDetailService.getCurrentUser());

            songService.save(songToSave);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            if (song.getId() != null) {
                songService.deleteById(song.getId());
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

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

    @PostMapping(params = {"listen", "song-id"})
    public ResponseEntity<Void> listenToSong(@RequestParam("song-id") Long id) {
        Optional<Song> song = songService.findById(id);
        if (song.isPresent()) {
            long currentListeningFrequency = song.get().getListeningFrequency();
            song.get().setListeningFrequency(++currentListeningFrequency);
            songService.save(song.get());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete", params = "id")
    public ResponseEntity<Void> deleteSong(@RequestParam("id") Long id) {
        songService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "name")
    public ResponseEntity<Iterable<Song>> songListByName(@RequestParam("name") String name) {
        Iterable<Song> songList = songService.findAllByNameContaining(name);
        int listSize = 0;
        if (songList instanceof Collection) {
            listSize = ((Collection<?>) songList).size();
        }
        if (listSize == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(songList, HttpStatus.OK);
    }

    @PutMapping(value = "/edit", params = "id")
    public ResponseEntity<Void> editSong(@RequestPart("song") Song song, @RequestParam("id") Long id, @RequestPart(value = "audio", required = false) MultipartFile multipartFile) {
        Optional<Song> oldSong = songService.findById(id);
        if (oldSong.isPresent()) {
            if (multipartFile != null) {
                String fileDownloadUri = audioStorageService.saveToFirebaseStorage(oldSong.get(), multipartFile);
                song.setUrl(fileDownloadUri);
            }
            songService.setFields(oldSong.get(), song);
            songService.save(oldSong.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(params = {"comment", "song-id"})
    public ResponseEntity<Void> commentOnSong(@Valid @RequestBody Comment comment, @RequestParam("song-id") Long id) {
        Optional<Song> song = songService.findById(id);
        if (song.isPresent()) {
            LocalDateTime localDateTime = LocalDateTime.now();
            User currentUser = userDetailService.getCurrentUser();
            comment.setLocalDateTime(localDateTime);
            comment.setSong(song.get());
            comment.setUser(currentUser);
            commentService.save(comment);
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-song")
    public ResponseEntity<Page<Song>> mySongList(Pageable pageable) {
        Page<Song> mySongList = songService.findAllByUsersContains(userDetailService.getCurrentUser(), pageable);
        if (mySongList.getTotalElements() > 0) {
            return new ResponseEntity<>(mySongList, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(params = {"like", "song-id"})
    public ResponseEntity<Void> likeSong(@RequestParam("song-id") Long id) {
        likeService.like(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(params = {"unlike", "song-id"})
    public ResponseEntity<Void> dislikeSong(@RequestParam("song-id") Long id) {
        likeService.unlike(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
