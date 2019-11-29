package com.codegym.mos.module4projectmos.service;

import com.codegym.mos.module4projectmos.model.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AlbumService {
    Page<Album> findAll(Pageable pageable);

    Optional<Album> findById(Long id);

    void save(Album album);
}
