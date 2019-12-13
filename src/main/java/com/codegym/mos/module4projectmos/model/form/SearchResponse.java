package com.codegym.mos.module4projectmos.model.form;
import com.codegym.mos.module4projectmos.model.entity.Artist;
import com.codegym.mos.module4projectmos.model.entity.Song;
import com.codegym.mos.module4projectmos.model.entity.User;
import lombok.Data;

@Data
public class SearchResponse {
    private Iterable<Song> songs;
    private Iterable<Artist> artists;

    public SearchResponse(Iterable<Song> songs, Iterable<Artist> artists) {
        this.songs = songs;
        this.artists = artists;
    }

    public SearchResponse() {
    }
}