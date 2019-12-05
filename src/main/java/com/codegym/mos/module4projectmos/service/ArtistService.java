package com.codegym.mos.module4projectmos.service;

import com.codegym.mos.module4projectmos.model.entity.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Optional;

public interface ArtistService {
    Artist findByName(String name);

    void save(Artist artist);

    String convertToString(Collection<Artist> artists);

    Iterable<Artist> findAllByNameContaining(String name);

    Iterable<Artist> findTop10ByNameContaining(String name);

    Page<Artist> findAllByNameContaining(String name, Pageable pageable);

    Page<Artist> findAll(Pageable pageable);

    Optional<Artist> findById(Long id);
}
