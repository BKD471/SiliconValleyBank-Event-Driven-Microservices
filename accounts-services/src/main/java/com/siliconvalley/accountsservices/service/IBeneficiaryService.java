package com.siliconvalley.accountsservices.service;

import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.BadApiRequestException;
import com.siliconvalley.accountsservices.exception.BeneficiaryException;

public interface IBeneficiaryService {
    OutputDto postRequestBenExecutor(final PostInputRequestDto postInputRequestDto) throws BeneficiaryException, AccountsException;
    OutputDto putRequestBenExecutor(final PutInputRequestDto putInputRequestDto) throws BeneficiaryException, AccountsException;
    OutputDto getRequestBenExecutor(final GetInputRequestDto getInputRequestDto) throws AccountsException, BeneficiaryException, BadApiRequestException;
    OutputDto deleteRequestBenExecutor(final DeleteInputRequestDto deleteInputRequestDto) throws BeneficiaryException, AccountsException;
}
