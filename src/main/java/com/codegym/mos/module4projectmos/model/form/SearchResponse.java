package com.codegym.mos.module4projectmos.model.form;
import com.codegym.mos.module4projectmos.model.entity.Artist;
import com.codegym.mos.module4projectmos.model.entity.Playlist;
import com.codegym.mos.module4projectmos.model.entity.Song;
import lombok.Data;

@Data
public class SearchResponse {
    private Iterable<Song> songs;
    private Iterable<Artist> artists;
    private Iterable<Playlist> playlists;

    public SearchResponse(Iterable<Song> songs, Iterable<Artist> artists, Iterable<Playlist> playlists) {
        this.songs = songs;
        this.artists = artists;
        this.playlists = playlists;
    }

    public SearchResponse() {
    }
}