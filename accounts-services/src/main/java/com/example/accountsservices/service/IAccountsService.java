package com.example.accountsservices.service;

import com.example.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.CustomerException;


import java.io.IOException;


public interface IAccountsService {
    OutputDto accountSetUp(final PostInputRequestDto postInputRequestDto);
    OutputDto postRequestExecutor(final PostInputRequestDto postInputRequestDto) throws AccountsException, CustomerException, IOException;
    OutputDto putRequestExecutor(final PutInputRequestDto putInputRequestDto) throws AccountsException, CustomerException, IOException;
    OutputDto getRequestExecutor(final GetInputRequestDto getInputRequestDto) throws AccountsException, CustomerException, IOException;
    OutputDto deleteRequestExecutor(final DeleteInputRequestDto deleteInputRequestDto) throws AccountsException;
    OutputDto deleteCustomer(final DeleteInputRequestDto deleteInputRequestDto);
}
