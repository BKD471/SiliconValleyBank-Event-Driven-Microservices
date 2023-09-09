package com.siliconvalley.accountsservices.service;


import com.siliconvalley.accountsservices.exception.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FileServiceTests {
    private static final String PATH_TO_PROPERTIES_FILE = "accounts-services/src/test/java/com/siliconvalley/accountsservices/service/properties/FileServiceTests.properties";
    private final IImageService fileService;
    private final String IMAGE_PATH;
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(new FileInputStream(PATH_TO_PROPERTIES_FILE));
        } catch (IOException e) {
            log.error("Error while reading properties file");
        }
    }

    FileServiceTests(@Qualifier("fileServicePrimary") IImageService fileService) {
        this.fileService = fileService;
        this.IMAGE_PATH=properties.getProperty("customer.profile.images.path");
    }

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
