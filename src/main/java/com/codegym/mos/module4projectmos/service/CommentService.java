package com.codegym.mos.module4projectmos.service;

import com.codegym.mos.module4projectmos.model.entity.Comment;

import java.util.Optional;

public interface CommentService {
    Optional<Comment> findById(Long id);

    void save(Comment comment);

    void deleteById(Long id);
}
