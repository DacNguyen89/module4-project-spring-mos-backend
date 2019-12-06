package com.codegym.mos.module4projectmos.model.form;

import com.codegym.mos.module4projectmos.model.entity.Artist;
import com.codegym.mos.module4projectmos.model.entity.Song;
import com.codegym.mos.module4projectmos.model.entity.User;
import lombok.Data;

import java.util.Optional;

@Data
public class SearchResponse {
    private Optional<User> users;
    private Iterable<Artist> artists;

    public SearchResponse(Optional<User> users, Iterable<Artist> artists) {
        this.users = users;
        this.artists = artists;
    }

    public SearchResponse() {
    }
}
