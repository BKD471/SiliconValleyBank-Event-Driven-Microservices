package com.siliconvalley.accountsservices.service.impl;

import com.siliconvalley.accountsservices.exception.BadApiRequestException;
import com.siliconvalley.accountsservices.exception.ResponseException;
import com.siliconvalley.accountsservices.exception.builders.ExceptionBuilder;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.repository.IAccountsRepository;
import com.siliconvalley.accountsservices.repository.ICustomerRepository;
import com.siliconvalley.accountsservices.service.AbstractService;
import com.siliconvalley.accountsservices.service.IImageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.ExceptionCodes.RES_EXC;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service("fileServicePrimary")
public final class ImageServiceImpl extends AbstractService implements IImageService {
    private ImageServiceImpl(final IAccountsRepository accountsRepository, final ICustomerRepository customerRepository) {
        super(accountsRepository, customerRepository);
    }

    @Override
    public String uploadFile(final MultipartFile file, final String path) throws ResponseException, IOException {
        final String methodName="uploadFile(MultipartFile,String) in FileServiceImpl";
        final String originalFileName = file.getOriginalFilename();

        if(isBlank(originalFileName)) throw new BadApiRequestException(BadApiRequestException.class,"Faced issue while fetching the image"
                ,methodName);
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
        }else throw (ResponseException) ExceptionBuilder.builder().className(ResponseException.class).reason(String.format("%s type not supported yet",extension)).methodName(methodName).build(RES_EXC);
    }

    @Override
    public InputStream getResource(final String path,final String name) throws FileNotFoundException {
        final String fullPath=path+File.separator+name;
        return new FileInputStream(fullPath);
    }
}
