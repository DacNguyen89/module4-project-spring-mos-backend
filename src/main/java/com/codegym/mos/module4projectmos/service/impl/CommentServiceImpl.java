package com.codegym.mos.module4projectmos.service.impl;

import com.codegym.mos.module4projectmos.model.entity.Comment;
import com.codegym.mos.module4projectmos.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl {

    @Autowired
    CommentRepository commentRepository;

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    public void save(Comment comment) {
        commentRepository.saveAndFlush(comment);
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
