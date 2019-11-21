package com.codegym.mos.module4projectmos.service.impl;

import com.codegym.mos.module4projectmos.model.entity.Song;
import com.codegym.mos.module4projectmos.repository.SongRepository;
import com.codegym.mos.module4projectmos.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SongServiceImpl implements SongService {
    @Autowired
    SongRepository songRepository;

    @Override
    public Page<Song> findAll(Pageable pageable) {
        return songRepository.findAll(pageable);
    }

    @Override
    public Optional<Song> findById(Long id) {
        return songRepository.findById(id);
    }

    @Override
    public Optional<Song> findByName(String name) {
        return songRepository.findByName(name);
    }

    @Override
    public Song save(Song song) {
        songRepository.saveAndFlush(song);
        return song;
    }
}
