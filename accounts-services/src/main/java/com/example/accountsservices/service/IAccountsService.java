package com.example.accountsservices.service;

import com.example.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.CustomerException;

public interface IAccountsService {
    OutputDto postRequestExecutor(PostInputRequestDto postInputRequestDto) throws AccountsException, CustomerException;
    OutputDto putRequestExecutor(PutInputRequestDto putInputRequestDto) throws AccountsException, CustomerException;
    OutputDto getRequestExecutor(GetInputRequestDto getInputRequestDto) throws AccountsException, CustomerException;
    OutputDto deleteRequestExecutor(DeleteInputRequestDto deleteInputRequestDto) throws AccountsException;
}
