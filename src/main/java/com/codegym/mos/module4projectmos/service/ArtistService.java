package com.codegym.mos.module4projectmos.service;

import com.codegym.mos.module4projectmos.model.entity.Artist;

public interface ArtistService {
    Artist findByName(String name);

    void save(Artist artist);
}
