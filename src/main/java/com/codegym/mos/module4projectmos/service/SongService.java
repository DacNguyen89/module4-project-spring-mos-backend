package com.codegym.mos.module4projectmos.service;

import com.codegym.mos.module4projectmos.model.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SongService {
    Page<Song> findAll(Pageable pageable);

    Optional<Song> findById(Long id);

    Optional<Song> findByName(String name);

    Iterable<Song> findAllByNameContaining(String name);

    Page<Song> findAllByUploader_Id(Long id, Pageable pageable);

    Song save(Song song);

    Boolean deleteById(Long id);

    void setFields(Song oldSongInfo, Song newSongInfo);
}
