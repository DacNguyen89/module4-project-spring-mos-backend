package com.codegym.mos.module4projectmos.service;

import com.codegym.mos.module4projectmos.model.entity.Artist;
import com.codegym.mos.module4projectmos.model.entity.Song;
import com.codegym.mos.module4projectmos.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SongService {
    Iterable<Song> findAll();

    Page<Song> findAll(Pageable pageable, String sort);

    Optional<Song> findById(Long id);

    Optional<Song> findByName(String name);

    Iterable<Song> findAllByNameContaining(String name);

    Page<Song> findAllByUploader_Id(Long id, Pageable pageable);

    Page<Song> findAllByArtistsContains(Artist artist, Pageable pageable);

    Iterable<Song> findTop10By(String sort);

    Song save(Song song);

    Boolean deleteById(Long id);

    void setFields(Song oldSongInfo, Song newSongInfo);

    Page<Song> findAllByUsersContains(User user, Pageable pageable);

    boolean hasUserLiked(Long songId);

    void setLike(Page<Song> songList);

    void setLike(Iterable<Song> songList);

    void setLike(Song song);
}
