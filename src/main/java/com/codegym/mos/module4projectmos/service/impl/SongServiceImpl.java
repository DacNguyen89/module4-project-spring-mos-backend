package com.codegym.mos.module4projectmos.service.impl;

import com.codegym.mos.module4projectmos.model.entity.Artist;
import com.codegym.mos.module4projectmos.model.entity.Like;
import com.codegym.mos.module4projectmos.model.entity.Song;
import com.codegym.mos.module4projectmos.model.entity.User;
import com.codegym.mos.module4projectmos.repository.LikeRepository;
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
    LikeRepository likeRepository;

    @Autowired
    AudioStorageService audioStorageService;

    @Autowired
    UserDetailServiceImpl userDetailService;

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
    public Iterable<Song> findAllByNameContaining(String name) {
        return songRepository.findAllByNameContaining(name);
    }

    @Override
    public Page<Song> findAllByUploader_Id(Long id, Pageable pageable) {
        return songRepository.findAllByUploader_Id(id, pageable);
    }

    @Override
    public Page<Song> findAllByArtistsContains(Artist artist, Pageable pageable) {
        return songRepository.findAllByArtistsContains(artist, pageable);
    }

    @Override
    public Song save(Song song) {
        songRepository.saveAndFlush(song);
        return song;
    }

    @Override
    public Boolean deleteById(Long id) {
        Optional<Song> song = songRepository.findById(id);
        if (song.isPresent()) {
            songRepository.deleteById(id);
            /*String fileUrl = song.get().getUrl();
            String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            return audioStorageService.deleteLocalStorageFile(audioStorageService.audioStorageLocation, filename);*/
        }
        return false;
    }

    @Override
    public void setFields(Song oldSongInfo, Song newSongInfo) {
        oldSongInfo.setName(newSongInfo.getName());
        oldSongInfo.setArtists(newSongInfo.getArtists());
        oldSongInfo.setGenres(newSongInfo.getGenres());
        oldSongInfo.setCountry(newSongInfo.getCountry());
        oldSongInfo.setReleaseDate(newSongInfo.getReleaseDate());
        oldSongInfo.setTags(newSongInfo.getTags());
        oldSongInfo.setTheme(newSongInfo.getTheme());
        if (newSongInfo.getUrl() != null) {
            oldSongInfo.setUrl(newSongInfo.getUrl());
        }
    }

    @Override
    public Page<Song> findAllByUsersContains(User user, Pageable pageable) {
        Page<Song> songList = songRepository.findAllByUsersContains(user, pageable);
        setLike(songList);
        return songList;
    }

    @Override
    public boolean hasUserLiked(Long songId) {
        Long userId = userDetailService.getCurrentUser().getId();
        Like like = likeRepository.findBySongIdAndUserId(songId, userId);
        return (like != null);
    }

    @Override
    public void setLike(Song song) {
        if (hasUserLiked(song.getId())) {
            song.setLiked(true);
        } else {
            song.setLiked(false);
        }
    }

    @Override
    public void setLike(Page<Song> songList) {
        for (Song song : songList) {
            if (hasUserLiked(song.getId())) {
                song.setLiked(true);
            } else {
                song.setLiked(false);
            }
        }
    }

    @Override
    public void setLike(Iterable<Song> songList) {
        for (Song song : songList) {
            if (hasUserLiked(song.getId())) {
                song.setLiked(true);
            } else {
                song.setLiked(false);
            }
        }
    }
}
