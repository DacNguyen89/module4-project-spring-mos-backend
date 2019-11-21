package com.codegym.mos.module4projectmos.service.impl;

import com.codegym.mos.module4projectmos.model.entity.Artist;
import com.codegym.mos.module4projectmos.repository.ArtistRepository;
import com.codegym.mos.module4projectmos.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArtistServiceImpl implements ArtistService {
    @Autowired
    ArtistRepository artistRepository;

    @Override
    public Artist findByName(String name) {
        return artistRepository.findByName(name);
    }

    @Override
    public void save(Artist artist) {
        artistRepository.saveAndFlush(artist);
    }
}
