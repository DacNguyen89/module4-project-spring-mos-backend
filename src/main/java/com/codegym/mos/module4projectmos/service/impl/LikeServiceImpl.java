package com.codegym.mos.module4projectmos.service.impl;

import com.codegym.mos.module4projectmos.model.entity.Like;
import com.codegym.mos.module4projectmos.model.entity.Song;
import com.codegym.mos.module4projectmos.model.entity.User;
import com.codegym.mos.module4projectmos.repository.LikeRepository;
import com.codegym.mos.module4projectmos.service.LikeService;
import com.codegym.mos.module4projectmos.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    SongService songService;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Override
    public void like(Long id) {
        Optional<Song> song = songService.findById(id);
        User user = userDetailService.getCurrentUser();
        if (song.isPresent()) {
            Like like = likeRepository.findBySongIdAndUserId(song.get().getId(), user.getId());
            if (like == null) {
                like = new Like(song.get().getId(), user.getId());
                likeRepository.save(like);
            }
        }
    }

    @Override
    public void unlike(Long id) {
        Optional<Song> song = songService.findById(id);
        User currentUser = userDetailService.getCurrentUser();
        if (song.isPresent()) {
            Like like = likeRepository.findBySongIdAndUserId(song.get().getId(), currentUser.getId());
            if (like != null) {
                likeRepository.delete(like);
            }
        }
    }
}
