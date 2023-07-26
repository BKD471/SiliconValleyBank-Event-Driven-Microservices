package com.siliconvalley.accountsservices.service;

import com.siliconvalley.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.CustomerException;


import java.io.IOException;


public interface IAccountsService {
    OutputDto accountSetUp(final PostInputRequestDto postInputRequestDto);
    OutputDto postRequestExecutor(final PostInputRequestDto postInputRequestDto) throws AccountsException, CustomerException, IOException;
    OutputDto putRequestExecutor(final PutInputRequestDto putInputRequestDto) throws AccountsException, CustomerException, IOException;
    OutputDto getRequestExecutor(final GetInputRequestDto getInputRequestDto) throws AccountsException, CustomerException, IOException;
    OutputDto deleteRequestExecutor(final DeleteInputRequestDto deleteInputRequestDto) throws AccountsException;
    OutputDto deleteCustomer(final DeleteInputRequestDto deleteInputRequestDto);
}
