package com.codegym.mos.module4projectmos.controller;

import com.codegym.mos.module4projectmos.model.entity.Artist;
import com.codegym.mos.module4projectmos.model.entity.Song;
import com.codegym.mos.module4projectmos.service.ArtistService;
import com.codegym.mos.module4projectmos.service.SongService;
import com.codegym.mos.module4projectmos.service.impl.AudioStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

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

    @PostMapping("/upload")
    public ResponseEntity<Void> createSong(@RequestPart("song") Song song, @RequestPart("audio") MultipartFile multipartFile) {
        Collection<Artist> artists = song.getArtists();
        for (Artist artist : artists) {
            artistService.save(artist);
        }
        songService.save(song);
        String fileDownloadUri = audioStorageService.storeFile(multipartFile, song);
        song.setUrl(fileDownloadUri);
        songService.save(song);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
