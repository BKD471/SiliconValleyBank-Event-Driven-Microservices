package com.example.accountsservices.service;


import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public interface IFileService {
    String uploadFile(final MultipartFile image,final String path) throws IOException;
    InputStream getResource(final String path,final String name) throws FileNotFoundException;
}
