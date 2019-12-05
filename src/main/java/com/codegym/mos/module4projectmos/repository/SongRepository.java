package com.codegym.mos.module4projectmos.repository;

import com.codegym.mos.module4projectmos.model.entity.Artist;
import com.codegym.mos.module4projectmos.model.entity.Song;
import com.codegym.mos.module4projectmos.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    //@Query("SELECT s, ar, c, t from Song s JOIN s.artists ar JOIN s.country c JOIN s.theme t")
    Page<Song> findAll(Pageable pageable);

    //@Query("SELECT s, ar, c, t from Song s JOIN FETCH s.artists ar JOIN FETCH s.country c JOIN FETCH s.theme t WHERE s.id=:id")
    Optional<Song> findById(@Param("id") Long id);

    @Query(value = "SELECT * FROM song WHERE BINARY name=:name", nativeQuery = true)
    Optional<Song> findByName(@Param("name") String name);

    Page<Song> findAllByUploader_Id(Long id, Pageable pageable);

    Page<Song> findAllByArtistsContains(Artist artist, Pageable pageable);

    Iterable<Song> findAllByNameContaining(@Param("name") String name);

    Page<Song> findAllByUsersContains(User user, Pageable pageable);
}
