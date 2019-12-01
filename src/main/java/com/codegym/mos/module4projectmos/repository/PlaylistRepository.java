package com.codegym.mos.module4projectmos.repository;

import com.codegym.mos.module4projectmos.model.entity.Playlist;
import com.codegym.mos.module4projectmos.model.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("SELECT p FROM Playlist p LEFT JOIN FETCH p.songs WHERE p.id=:id")
    Optional<Playlist> findById(@Param("id") Long id);

    Page<Playlist> findAllByUser_Id(Long userId, Pageable pageable);

    Page<Playlist> findAllByNameContaining(String name, Pageable pageable);

    Iterable<Playlist> findAllByUser_IdAndSongsNotContains(Long userId, Song song);
}
