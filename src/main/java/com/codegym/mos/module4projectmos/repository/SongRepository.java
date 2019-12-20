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
    @Query("SELECT s from Song s LEFT JOIN FETCH s.comments c LEFT JOIN FETCH s.uploader u WHERE s.id=:id")
    Optional<Song> findById(@Param("id") Long id);

    @Query(value = "SELECT * FROM song WHERE BINARY title=:title", nativeQuery = true)
    Optional<Song> findByTitle(@Param("title") String title);

    Page<Song> findAllByUploader_Id(Long id, Pageable pageable);

    Page<Song> findAllByArtistsContains(Artist artist, Pageable pageable);

    Iterable<Song> findAllByTitleContainingIgnoreCase(String title);

    //    @Query(nativeQuery = true, value = "SELECT * FROM public.song "
//            + "WHERE LOWER(unaccent(title)) LIKE LOWER(unaccent(:title))||'%'")
    Iterable<Song> findAllByUnaccentTitleContainingIgnoreCase(String title);

    Page<Song> findAllByUsersContains(User user, Pageable pageable);


    Page<Song> findAllByOrderByReleaseDateDesc(Pageable pageable);

    Page<Song> findAllByOrderByListeningFrequencyDesc(Pageable pageable);

    Iterable<Song> findFirst10ByOrderByListeningFrequencyDesc();

    @Query("SELECT s FROM Song s ORDER BY SIZE(s.users) DESC")
    Page<Song> findAllByOrderByUsers_Size(Pageable pageable);
}
