package com.codegym.mos.module4projectmos.service;

import com.codegym.mos.module4projectmos.model.entity.Playlist;
import com.codegym.mos.module4projectmos.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface PlaylistService {
    Optional<Playlist> findById(Long id);

    Page<Playlist> findAllByUser_Id(Long userId, Pageable pageable);

    Iterable<Playlist> findAllByNameContaining(String name);

    void save(Playlist playlist);

    void deleteById(Long id);

    boolean addSongToPlaylist(Long songId, Long playlistId);

    boolean deleteSongFromPlaylist(Long songId, Long playlistId);

    boolean checkPlaylistOwner(Long id);

    Iterable<Playlist> getPlaylistListToAdd(Long songId);

    void setFieldsEdit(Playlist oldPlaylistInfo, Playlist newPlaylistInfo);

    Page<Playlist> fillAll(Pageable pageable);

}
