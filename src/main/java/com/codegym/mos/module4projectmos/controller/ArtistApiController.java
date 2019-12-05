package com.codegym.mos.module4projectmos.controller;

import com.codegym.mos.module4projectmos.model.entity.Artist;
import com.codegym.mos.module4projectmos.service.ArtistService;
import com.codegym.mos.module4projectmos.service.impl.AvatarStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/artist")
public class ArtistApiController {
    @Autowired
    ArtistService artistService;
    @Autowired

    AvatarStorageService avatarStorageService;

    @PostMapping(value = "/create")
    public ResponseEntity<Void> createArtist(@RequestPart("artist") Artist artist, @RequestPart("avatar") MultipartFile multipartFile) {
        artistService.save(artist);
        String fileDownloadUri = avatarStorageService.saveToFirebaseStorage(artist, multipartFile);
        artist.setAvatarUrl(fileDownloadUri);
        artistService.save(artist);
        return new ResponseEntity<>(HttpStatus.OK);
    }

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
}
