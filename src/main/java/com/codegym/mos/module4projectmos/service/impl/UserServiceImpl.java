package com.codegym.mos.module4projectmos.service.impl;

import com.codegym.mos.module4projectmos.model.entity.Artist;
import com.codegym.mos.module4projectmos.model.entity.Playlist;
import com.codegym.mos.module4projectmos.model.entity.Song;
import com.codegym.mos.module4projectmos.model.entity.User;
import com.codegym.mos.module4projectmos.model.form.SearchResponse;
import com.codegym.mos.module4projectmos.repository.UserRepository;
import com.codegym.mos.module4projectmos.service.ArtistService;
import com.codegym.mos.module4projectmos.service.PlaylistService;
import com.codegym.mos.module4projectmos.service.SongService;
import com.codegym.mos.module4projectmos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    SongService songService;
    @Autowired
    ArtistService artistService;
    @Autowired
    UserService userService;

    @Autowired
    PlaylistService playlistService;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Iterable<User> findByUsernameContaining(String username) {
        return userRepository.findByUsernameContaining(username);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findByUsernameContaining(String username, Pageable pageable) {
        return userRepository.findByUsernameContaining(username, pageable);
    }

    @Override
    public Page<User> findByRoles_Name(String username, Pageable pageable) {
        return userRepository.findByRoles_Name(username, pageable);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void save(User user) {
        userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void setFields(User newUserInfo, User oldUserInfo) {
        newUserInfo.setRoles(oldUserInfo.getRoles());
        newUserInfo.setAccountNonExpired(oldUserInfo.isAccountNonExpired());
        newUserInfo.setAccountNonLocked(oldUserInfo.isAccountNonLocked());
        newUserInfo.setCredentialsNonExpired(oldUserInfo.isCredentialsNonExpired());
        newUserInfo.setEnabled(oldUserInfo.isEnabled());
    }

    @Override
    public void setFieldsEdit(User oldUserInfo, User newUserInfo) {
        oldUserInfo.setFirstName(newUserInfo.getFirstName());
        oldUserInfo.setLastName(newUserInfo.getLastName());
        oldUserInfo.setBirthDate(newUserInfo.getBirthDate());
        oldUserInfo.setGender(newUserInfo.getGender());
        oldUserInfo.setPhoneNumber(newUserInfo.getPhoneNumber());
        if (!oldUserInfo.getPassword().equals(newUserInfo.getPassword())) {
            oldUserInfo.setPassword(passwordEncoder.encode(newUserInfo.getPassword()));
        }
    }

    @Override
    public SearchResponse search(String searchText) {
        Iterable<Song> songs = songService.findAllByTitleContaining(searchText);
        Iterable<Artist> artists = artistService.findAllByNameContaining(searchText);
        Iterable<Playlist> playlists = playlistService.findAllByNameContaining(searchText);
        return new SearchResponse(songs, artists, playlists);
    }
}