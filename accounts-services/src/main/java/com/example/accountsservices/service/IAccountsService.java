package com.example.accountsservices.service;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.InputDto;
import com.example.accountsservices.dto.OutputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.CustomerException;

import java.util.List;

public interface IAccountsService {
    OutputDto postRequestExecutor(InputDto inputDto) throws AccountsException;
    OutputDto putRequestExecutor(InputDto inputDto) throws AccountsException;
    OutputDto getRequestExecutor(InputDto inputDto) throws AccountsException, CustomerException;
    OutputDto deleteRequestExecutor(InputDto inputDto) throws AccountsException;
}
