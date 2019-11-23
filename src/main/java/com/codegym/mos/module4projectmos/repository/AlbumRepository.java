package com.codegym.mos.module4projectmos.repository;

import com.codegym.mos.module4projectmos.model.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    @Query("SELECT a, ar, c, t FROM Album a JOIN a.artists ar JOIN a.country c JOIN a.theme t WHERE a.id = :id")
    Optional<Album> findById(@Param("id") Long id);

    @Query("SELECT a, ar, c, t FROM Album a JOIN a.artists ar JOIN a.country c JOIN a.theme t WHERE a.id = :id")
    Page<Album> findAll(Pageable pageable);
}
