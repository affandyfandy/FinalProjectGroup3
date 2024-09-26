package com.hotel.room_service.service.Impl;

import com.hotel.room_service.service.FileStorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileStorageServiceImplTest {

    @InjectMocks
    private FileStorageServiceImpl fileStorageService;

    @BeforeEach
    void setUp() {
        fileStorageService = new FileStorageServiceImpl();
    }

    @Test
    void testStoreFileSuccess() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getInputStream()).thenReturn(mock(InputStream.class));
        String fileName = fileStorageService.storeFile(file);
        assertTrue(fileName.contains("test.jpg"), "Stored file name should contain the original file name.");
    }

    @Test
    void testStoreFileInvalidName() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("../test.jpg");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fileStorageService.storeFile(file);
        });
        assertTrue(exception.getMessage().contains("Invalid path sequence"),
                "The exception message should mention the invalid path sequence.");
    }

    @Test
    void testDeleteFileSuccess() throws IOException {
        String fileName = "test.jpg";
        Path filePath = Paths.get("src/main/resources/static/images/" + fileName).toAbsolutePath().normalize();
        Files.createFile(filePath);  // Simulate a file being created
        fileStorageService.deleteFile(fileName);
        assertFalse(Files.exists(filePath), "File should be deleted after calling deleteFile.");
    }

    @Test
    void testDeleteFileNotExist() {
        String fileName = "non_existent.jpg";
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fileStorageService.deleteFile(fileName);
        });
        assertTrue(exception.getMessage().contains("File not found"),
                "The exception message should indicate that the file does not exist.");
    }

    @Test
    void testDeleteFileFailure() throws IOException {
        String fileName = "unremovable.jpg";
        Path filePath = Paths.get("src/main/resources/static/images/" + fileName).toAbsolutePath().normalize();
        Files.createFile(filePath);
        filePath.toFile().setWritable(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fileStorageService.deleteFile(fileName);
        });
        filePath.toFile().setWritable(true);
        Files.deleteIfExists(filePath);
        assertTrue(exception.getMessage().contains("Failed to delete file"),
                "The exception message should indicate failure in deleting the file.");
    }

}
