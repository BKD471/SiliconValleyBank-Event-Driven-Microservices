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
    OutputDto postRequestBenExecutor(final PostInputRequestDto postInputRequestDto) throws BeneficiaryException, AccountsException;
    OutputDto putRequestBenExecutor(final PutInputRequestDto putInputRequestDto) throws BeneficiaryException, AccountsException;
    OutputDto getRequestBenExecutor(final GetInputRequestDto getInputRequestDto) throws AccountsException, BeneficiaryException, BadApiRequestException;
    OutputDto deleteRequestBenExecutor(final DeleteInputRequestDto deleteInputRequestDto) throws BeneficiaryException, AccountsException;
}
