package com.example.accountsservices.service;

import com.example.accountsservices.dto.*;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.CustomerException;

import java.util.List;

public interface IAccountsService {
    OutputDto postRequestExecutor(InputDto postInputDto) throws AccountsException, CustomerException;
    OutputDto putRequestExecutor(InputDto putInputDto) throws AccountsException, CustomerException;
    OutputDto getRequestExecutor(InputDto getInputDto) throws AccountsException, CustomerException;
    OutputDto deleteRequestExecutor(InputDto deleteInputDto) throws AccountsException;
}
