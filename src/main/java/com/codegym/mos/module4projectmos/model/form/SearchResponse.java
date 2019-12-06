package com.codegym.mos.module4projectmos.model.form;

import com.codegym.mos.module4projectmos.model.entity.Artist;
import com.codegym.mos.module4projectmos.model.entity.User;
import lombok.Data;

@Data
public class SearchResponse {
    private Iterable<User> users;
    private Iterable<Artist> artists;

    public SearchResponse(Iterable<User> users, Iterable<Artist> artists) {
        this.users = users;
        this.artists = artists;
    }

    public SearchResponse() {
    }
}
