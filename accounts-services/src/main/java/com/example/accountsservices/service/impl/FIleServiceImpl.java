package com.example.accountsservices.service.impl;

import com.example.accountsservices.exception.ResponseException;
import com.example.accountsservices.repository.IAccountsRepository;
import com.example.accountsservices.repository.ICustomerRepository;
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
    protected FIleServiceImpl(IAccountsRepository accountsRepository, ICustomerRepository customerRepository) {
        super(accountsRepository, customerRepository);
    }

    @Override
    public String uploadFile(final MultipartFile file,final String path) throws ResponseException, IOException {
        final String methodName="uploadFile(MultipartFile,String) in FileServiceImpl";

        final String originalFileName = file.getOriginalFilename();
        final String fileName = UUID.randomUUID().toString();
        final String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        final String fileNameWithExtension = fileName + extension;
        final String fullPathWithFileName = path + File.separator + fileNameWithExtension;
        if(extension.equalsIgnoreCase(".jpg") ||
           extension.equalsIgnoreCase(".jpeg") ||
           extension.equalsIgnoreCase(".png") ||
           extension.equalsIgnoreCase(".avif")){

            File folder=new File(path);
            if(!folder.exists()){
                folder.mkdirs();
            }
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        }else{
            throw  new ResponseException(ResponseException.class,String.format("%s type not supported yet",extension),methodName);
        }
    }

    @Override
    public InputStream getResource(final String path,final String name) throws FileNotFoundException {
        final String fullPath=path+File.separator+name;
        return new FileInputStream(fullPath);
    }
}
