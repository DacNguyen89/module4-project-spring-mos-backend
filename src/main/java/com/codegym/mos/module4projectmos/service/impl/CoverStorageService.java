package com.codegym.mos.module4projectmos.service.impl;

import com.codegym.mos.module4projectmos.exception.FileStorageException;
import com.codegym.mos.module4projectmos.model.entity.Album;
import com.codegym.mos.module4projectmos.property.CoverStorageProperty;
import com.codegym.mos.module4projectmos.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CoverStorageService extends StorageService<Album> {
    final Path coverStorageLocation;

    @Autowired
    public CoverStorageService(CoverStorageProperty coverStorageProperty) {
        this.coverStorageLocation = Paths.get(coverStorageProperty.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.coverStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
}
