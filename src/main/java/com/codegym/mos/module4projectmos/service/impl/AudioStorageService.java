package com.codegym.mos.module4projectmos.service.impl;

import com.codegym.mos.module4projectmos.exception.FileStorageException;
import com.codegym.mos.module4projectmos.model.entity.Song;
import com.codegym.mos.module4projectmos.property.AudioStorageProperties;
import com.codegym.mos.module4projectmos.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AudioStorageService extends StorageService<Song> {
    final Path audioStorageLocation;

    @Autowired
    public AudioStorageService(AudioStorageProperties audioStorageProperties) {
        this.audioStorageLocation = Paths.get(audioStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.audioStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
}
