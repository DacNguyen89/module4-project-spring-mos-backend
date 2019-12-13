package com.codegym.mos.module4projectmos.service;

import com.codegym.mos.module4projectmos.model.entity.User;
import com.codegym.mos.module4projectmos.model.form.SearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);

    Iterable<User> findByUsernameContaining(String username);

    Page<User> findAll(Pageable pageable);

    Page<User> findByUsernameContaining(String username, Pageable pageable);

    Page<User> findByRoles_Name(String username, Pageable pageable);

    Optional<User> findById(Long id);

    void save(User user);

    void deleteById(Long id);

    void setFields(User newUserInfo, User oldUserInfo);

    void setFieldsEdit(User oldUserInfo, User newUserInfo);

    SearchResponse search(String searchText);
}