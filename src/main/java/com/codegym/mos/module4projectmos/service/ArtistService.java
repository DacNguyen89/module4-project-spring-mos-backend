package com.codegym.mos.module4projectmos.service;

import com.codegym.mos.module4projectmos.model.entity.Artist;

import java.util.Collection;

public interface ArtistService {
    Artist findByName(String name);

    void save(Artist artist);

    String convertToString(Collection<Artist> artists);

    Iterable<Artist> findAllByNameContaining(String name);
}
