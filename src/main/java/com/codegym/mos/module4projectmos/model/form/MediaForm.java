package com.codegym.mos.module4projectmos.model.form;

import com.codegym.mos.module4projectmos.model.entity.Artist;
import lombok.Data;

import java.util.Collection;
import java.util.Date;

@Data
public abstract class MediaForm {
    private String name;

    private Date releaseDate;

    private Collection<Artist> artists;

    private String genres;

    private String tags;

    private String country;

    private String theme;
}
