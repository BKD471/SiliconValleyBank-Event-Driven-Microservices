package com.example.accountsservices.service;


import com.example.accountsservices.exception.ResponseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FileServiceTests {

    @Autowired
    @Qualifier("fileServicePrimary")
    private IFileService fileService;

    @Value("${customer.profile.images.path}")
    private String IMAGE_PATH;


    @Test
    public void invalidImageExtensionTest() throws IOException {
        MockMultipartFile invalidImgFile =
                new MockMultipartFile("data", "uploadedFile.webp", "text/plain", "some kml".getBytes());
        assertThrows(ResponseException.class, () -> {
            fileService.uploadFile(invalidImgFile, IMAGE_PATH);
        });
    }

    @Test
    public void getResourceFailedTest() throws IOException {
        assertThrows(FileNotFoundException.class, () -> {
            fileService.getResource(IMAGE_PATH, "notFoundImage.png");
        });
    }
}
