package com.example.accountsservices.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface IFileService {
    String uploadFile(MultipartFile image,String path);
    InputStream getResource(String path,String name);
}
