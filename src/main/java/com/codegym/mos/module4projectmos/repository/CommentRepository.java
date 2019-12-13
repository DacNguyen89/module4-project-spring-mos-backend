package com.codegym.mos.module4projectmos.repository;

import com.codegym.mos.module4projectmos.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
