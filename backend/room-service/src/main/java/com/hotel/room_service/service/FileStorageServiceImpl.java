package com.hotel.room_service.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageServiceImpl() {
        this.fileStorageLocation = Paths.get("src/main/resources/static/images").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory for file storage.", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename()).replace(" ", "_");
        String fileName = System.currentTimeMillis() + "_" + originalFileName;

        if (fileName.contains("..")) {
            throw new IllegalArgumentException("Invalid path sequence in filename: " + fileName);
        }

        try {
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file " + fileName + ". Please try again.", ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        // Implement this if needed
        return null;
    }

    @Override
    public void deleteFile(String fileName) {
        Path filePath = fileStorageLocation.resolve(fileName).normalize();

        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                throw new RuntimeException("File not found: " + fileName);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to delete file " + fileName, ex);
        }
    }
}
