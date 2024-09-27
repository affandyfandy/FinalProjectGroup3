package com.hotel.auth_service.service;

import com.hotel.auth_service.service.impl.FileStorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileStorageServiceTest {

    private FileStorageServiceImpl fileStorageService;
    private Path testPath;

    @BeforeEach
    void setUp() throws IOException {
        fileStorageService = new FileStorageServiceImpl();
        testPath = Paths.get("src/main/resources/static/images").toAbsolutePath().normalize();

        // Clear the test directory before each test
        Files.createDirectories(testPath);
        Files.list(testPath).forEach(file -> {
            try {
                Files.delete(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    void storeFile_WithValidFile_ReturnsFileName() throws Exception {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        String originalFileName = "test file.txt";

        when(file.getOriginalFilename()).thenReturn(originalFileName);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes()));

        // Act
        String fileName = fileStorageService.storeFile(file);

        // Assert
        // Check that the filename contains a timestamp followed by the cleaned original filename
        assertTrue(fileName.matches("\\d+_test_file.txt"));

        // Check if the file is correctly stored in the test directory
        assertTrue(Files.exists(testPath.resolve(fileName)));
    }


    @Test
    void storeFile_WithFileNameContainingInvalidPath_ReturnsException() {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("../invalid.txt"); // Invalid path sequence

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            fileStorageService.storeFile(file);
        });

        // Assert the actual message that is being thrown
        assertTrue(exception.getMessage().contains("Could not store file"));
    }




    @Test
    void storeFile_WhenIOExceptionOccurs_ReturnsException() throws Exception {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.txt");
        when(file.getInputStream()).thenThrow(new IOException());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            fileStorageService.storeFile(file);
        });
        assertTrue(exception.getMessage().contains("Could not store file"));
    }

    @Test
    void loadFileAsResource_WithValidFileName_ReturnsResource() throws Exception {
        // Arrange
        String fileName = "test.txt";
        Files.write(testPath.resolve(fileName), "test content".getBytes());

        // Act
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Assert
        assertNotNull(resource);
        assertTrue(resource.exists());
        assertEquals(fileName, resource.getFilename());
    }

    @Test
    void loadFileAsResource_WithFileNotFound_ReturnsException() {
        // Arrange
        String fileName = "non_existing.txt";

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            fileStorageService.loadFileAsResource(fileName);
        });
        assertTrue(exception.getMessage().contains("File not found"));
    }

    @Test
    void loadFileAsResource_WithMalformedURLException_ReturnsException() {
        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> {
            fileStorageService.loadFileAsResource("invalid?file:name");
        });

        // Debugging: Print the actual exception message
        System.out.println("Actual exception message: " + exception.getMessage());

        // Assert that the exception message contains "Illegal char" to handle invalid characters
        assertTrue(exception.getMessage().contains("Illegal char"));
    }




    @Test
    void deleteFile_WithValidFileName_DeletesFile() throws Exception {
        // Arrange
        String fileName = "test.txt";
        Files.write(testPath.resolve(fileName), "test content".getBytes());

        // Act
        fileStorageService.deleteFile(fileName);

        // Assert
        assertFalse(Files.exists(testPath.resolve(fileName)));
    }

    @Test
    void deleteFile_WithNonExistingFile_DoesNotThrowException() {
        assertDoesNotThrow(() -> fileStorageService.deleteFile("non_existing.txt"));
    }

}
