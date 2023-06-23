package com.example.accountsservices.service.impl;

import com.example.accountsservices.exception.ResponseException;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.CustomerRepository;
import com.example.accountsservices.service.AbstractAccountsService;
import com.example.accountsservices.service.IFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service("fileServicePrimary")
public class FIleServiceImpl extends AbstractAccountsService implements IFileService {
    protected FIleServiceImpl(AccountsRepository accountsRepository, CustomerRepository customerRepository) {
        super(accountsRepository, customerRepository);
    }

    @Override
    public String uploadFile(MultipartFile file, String path) throws ResponseException, IOException {
        String methodName="uploadFile(MultipartFile,String) in FileServiceImpl";

        String originalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        String fileNameWithExtension = fileName + extension;
        String fullPathWithFileName = path + File.separator + fileNameWithExtension;
        if(extension.equalsIgnoreCase(".jpg") ||
           extension.equalsIgnoreCase(".jpeg") ||
           extension.equalsIgnoreCase(".png") ||
           extension.equalsIgnoreCase(".avif")){

            File folder=new File(path);
            boolean isCreated;
            if(!folder.exists()){
                isCreated=folder.mkdirs();
            }
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        }else{
            throw  new ResponseException(ResponseException.class,String.format("%s type not supported yet",extension),methodName);
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath=path+File.separator+name;
        return new FileInputStream(fullPath);
    }
}
