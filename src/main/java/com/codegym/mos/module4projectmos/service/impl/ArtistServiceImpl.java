package com.codegym.mos.module4projectmos.service.impl;

import com.codegym.mos.module4projectmos.formatter.StringAccentRemover;
import com.codegym.mos.module4projectmos.model.entity.Artist;
import com.codegym.mos.module4projectmos.repository.ArtistRepository;
import com.codegym.mos.module4projectmos.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ArtistServiceImpl implements ArtistService {
    @Autowired
    ArtistRepository artistRepository;

    @Override
    public Artist findByName(String name) {
        return artistRepository.findByName(name);
    }

    @Override
    public void save(Artist artist) {
        String unaccentName = StringAccentRemover.removeStringAccent(artist.getName());
        artist.setUnaccentName(unaccentName.toLowerCase());
        artistRepository.saveAndFlush(artist);
    }

    @Override
    public Iterable<Artist> findAllByNameContaining(String name) {
        if (name.equals(StringAccentRemover.removeStringAccent(name))) {
            return artistRepository.findAllByUnaccentNameContainingIgnoreCase(name);
        } else {
            return artistRepository.findAllByNameContainingIgnoreCase(name);
        }
    }

    @Override
    public Iterable<Artist> findTop10ByNameContaining(String name) {
        if (name.equals(StringAccentRemover.removeStringAccent(name))) {
            return artistRepository.findFirst10ByUnaccentNameContainingIgnoreCase(name);
        } else {
            return artistRepository.findFirst10ByNameContainingIgnoreCase(name);
        }
    }

    @Override
    public Page<Artist> findAllByNameContaining(String name, Pageable pageable) {
        return artistRepository.findAllByNameContaining(name, pageable);
    }

    @Override
    public Page<Artist> findAll(Pageable pageable) {
        return artistRepository.findAll(pageable);
    }

    @Override
    public Optional<Artist> findById(Long id) {
        return artistRepository.findById(id);
    }

    public String convertToString(Collection<Artist> artists) {
        String artistsString = "";
        if (!artists.isEmpty()) {
            artistsString = " - ";
            for (Artist artist : artists) {
                artistsString = artistsString.concat(artist.getName()).concat("_");
            }
        }
        return artistsString;
    }
}
