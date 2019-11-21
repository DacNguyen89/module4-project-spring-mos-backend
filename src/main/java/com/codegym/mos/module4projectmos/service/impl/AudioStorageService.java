package com.codegym.mos.module4projectmos.service.impl;

import com.codegym.mos.module4projectmos.exception.FileNotFoundException;
import com.codegym.mos.module4projectmos.exception.FileStorageException;
import com.codegym.mos.module4projectmos.model.entity.Artist;
import com.codegym.mos.module4projectmos.model.entity.Song;
import com.codegym.mos.module4projectmos.property.AudioStorageProperties;
import com.codegym.mos.module4projectmos.service.StorageService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.common.collect.Lists;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

@Service
public class AudioStorageService implements StorageService<Song> {
    private final Path audioStorageLocation;

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

    @Override
    public String storeFile(MultipartFile file, Song song) {
        String originalFileName = file.getOriginalFilename();
        // Get file extension
        String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".") + 1) : "";
        String url = song.getUrl();

        Collection<Artist> artists = song.getArtists();
        String artistsString = "";
        if (!artists.isEmpty()) {
            artistsString = " - ";
            for (Artist artist : artists) {
                artistsString = artistsString.concat(artist.getName()).concat("_");
            }
        }

        // check if new audio ext is different from old file ext
        if (url != null && !url.equals("")) {
            String oldExtension = url.substring(url.lastIndexOf(".") + 1);
            if (!oldExtension.equals(extension)) {
                String oldFileName = song.getId().toString().concat(" - ").concat(song.getName()).concat(artistsString).concat(".").concat(oldExtension);
                deleteFile(oldFileName);
            }
        }

        // Normalize file name
        String fileName = StringUtils.cleanPath(song.getId().toString().concat(" - ").concat(song.getName()).concat(artistsString).concat(".").concat(extension));
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("/media/scopex/SSD/musikonthesea-firebase-adminsdk-menby-5fd17288f9.json"))
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setDatabaseUrl("https://musikonthesea.firebaseio.com")
                    .setStorageBucket("musikonthesea.appspot.com")
                    .build();

            FirebaseApp fireApp = FirebaseApp.initializeApp(options);

            StorageClient storageClient = StorageClient.getInstance(fireApp);
            InputStream testFile = file.getInputStream();
            String blobString = "audio/" + fileName;

            Bucket bucket = storageClient.bucket();

            Blob blob = bucket.create(blobString, testFile, Bucket.BlobWriteOption.userProject("climax-sound"));
            bucket.getStorage().updateAcl(blob.getBlobId(), Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

            return blob.getMediaLink();
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(""))
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setDatabaseUrl("https://musikonthesea.firebaseio.com")
                    .setStorageBucket("musikonthesea.appspot.com")
                    .build();

            FirebaseApp fireApp = FirebaseApp.initializeApp(options);

            StorageClient storageClient = StorageClient.getInstance(fireApp);
            String blobString = "audio/" + fileName;
            return new ByteArrayResource(storageClient.bucket().get(blobString, Storage.BlobGetOption.userProject("climax-sound")).getContent());
        } catch (IOException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }

    @Override
    public Boolean deleteFile(String fileName) {
        Path filePath = this.audioStorageLocation.resolve(fileName).normalize();
        File file = filePath.toFile();
        return file.delete();
    }
}
