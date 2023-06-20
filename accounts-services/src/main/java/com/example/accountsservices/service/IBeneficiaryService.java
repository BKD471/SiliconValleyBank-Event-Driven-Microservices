package com.example.accountsservices.service;

import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BadApiRequestException;
import com.example.accountsservices.exception.BeneficiaryException;

public interface IBeneficiaryService {
    OutputDto postRequestBenExecutor(PostInputRequestDto postInputRequestDto) throws BeneficiaryException, AccountsException;
    OutputDto putRequestBenExecutor(PutInputRequestDto putInputRequestDto) throws BeneficiaryException, AccountsException;
    OutputDto getRequestBenExecutor(GetInputRequestDto getInputRequestDto) throws AccountsException, BeneficiaryException, BadApiRequestException;
    OutputDto deleteRequestBenExecutor(DeleteInputRequestDto deleteInputRequestDto) throws BeneficiaryException, AccountsException;
}
