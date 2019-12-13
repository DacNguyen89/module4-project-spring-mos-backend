package com.codegym.mos.module4projectmos.controller;

import com.codegym.mos.module4projectmos.model.entity.Artist;
import com.codegym.mos.module4projectmos.model.entity.Song;
import com.codegym.mos.module4projectmos.service.ArtistService;
import com.codegym.mos.module4projectmos.service.SongService;
import com.codegym.mos.module4projectmos.service.impl.AvatarStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/artist")
public class ArtistApiController {
    @Autowired
    ArtistService artistService;

    @Autowired
    SongService songService;

    AvatarStorageService avatarStorageService;

    @PreAuthorize("permitAll()")
    @PostMapping(value = "/create")
    public ResponseEntity<Void> createArtist(@RequestPart("artist") Artist artist, @RequestPart("avatar") MultipartFile multipartFile) {
        artistService.save(artist);
        String fileDownloadUri = avatarStorageService.saveToFirebaseStorage(artist, multipartFile);
        artist.setAvatarUrl(fileDownloadUri);
        artistService.save(artist);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/search", params = "name")
    public ResponseEntity<Iterable<Artist>> searchArtistByName(@RequestParam("name") String name) {
        Iterable<Artist> artistList = artistService.findTop10ByNameContaining(name);
        long size = 0;
        if (artistList instanceof Collection) {
            size = ((Collection<Artist>) artistList).size();
        }
        if (size == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else return new ResponseEntity<>(artistList, HttpStatus.OK);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/list")
    public ResponseEntity<Page<Artist>> getArtistList(Pageable pageable) {
        Page<Artist> artistList = artistService.findAll(pageable);
        if (artistList.getTotalElements() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(artistList, HttpStatus.OK);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/song-list", params = "artist-id")
    public ResponseEntity<Page<Song>> getSongListOfArtist(@RequestParam("artist-id") Long id, @PageableDefault(size = 5) Pageable pageable) {
        Optional<Artist> artist = artistService.findById(id);
        if (artist.isPresent()) {
            Page<Song> songList = songService.findAllByArtistsContains(artist.get(), pageable);
            if (songList.getTotalElements() > 0) {
                songService.setLike(songList);
                return new ResponseEntity<>(songList, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/detail", params = "id")
    public ResponseEntity<Artist> artistDetail(@RequestParam("id") Long id) {
        Optional<Artist> artist = artistService.findById(id);
        return artist.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }
}
