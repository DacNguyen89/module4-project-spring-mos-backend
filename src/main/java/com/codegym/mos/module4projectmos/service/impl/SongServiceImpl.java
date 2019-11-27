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

    @Autowired
    AudioStorageService audioStorageService;

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
    public Page<Song> findAllByUploader_Id(Long id, Pageable pageable) {
        return songRepository.findAllByUploader_Id(id, pageable);
    }

    @Override
    public Song save(Song song) {
        return songRepository.saveAndFlush(song);
    }

    @Override
    public Boolean deleteById(Long id) {
        Optional<Song> song = songRepository.findById(id);
        if (song.isPresent()) {
            songRepository.deleteById(id);
            String fileUrl = song.get().getUrl();
            String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            return audioStorageService.deleteLocalStorageFile(audioStorageService.audioStorageLocation, filename);
        }
        return false;
    }
}
