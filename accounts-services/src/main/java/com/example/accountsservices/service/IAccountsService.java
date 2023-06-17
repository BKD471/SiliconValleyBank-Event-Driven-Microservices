package com.example.accountsservices.service;

import com.example.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.CustomerException;
import org.springframework.stereotype.Component;

import java.io.IOException;


public interface IAccountsService {
    OutputDto accountSetUp(PostInputRequestDto postInputRequestDto);
    OutputDto postRequestExecutor(PostInputRequestDto postInputRequestDto) throws AccountsException, CustomerException, IOException;
    OutputDto putRequestExecutor(PutInputRequestDto putInputRequestDto) throws AccountsException, CustomerException, IOException;
    OutputDto getRequestExecutor(GetInputRequestDto getInputRequestDto) throws AccountsException, CustomerException, IOException;
    OutputDto deleteRequestExecutor(DeleteInputRequestDto deleteInputRequestDto) throws AccountsException;
}
