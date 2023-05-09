package com.example.accountsservices.service;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.InputDto;
import com.example.accountsservices.dto.OutputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;

import java.util.List;

public interface IBeneficiaryService {
//    BeneficiaryDto addBeneficiary(Long accountNumber, BeneficiaryDto beneficiaryDto) throws AccountsException;
//    BeneficiaryDto updateBeneficiaryDetailsOfanAccount(Long accountNumber,BeneficiaryDto beneficiaryDto) throws  AccountsException, BeneficiaryException;
//    List<BeneficiaryDto> getAllBeneficiariesOfAnAccountByAccountNumber(Long accountNumber) throws AccountsException;
//    void  deleteBeneficiariesForAnAccount(Long accountNumber,Long beneficiaryId) throws AccountsException ,BeneficiaryException;
//    void deleteAllBeneficiaries(Long accountNumber) throws AccountsException;


    OutputDto postRequestBenExecutor(InputDto inputDto) throws BeneficiaryException, AccountsException;
    OutputDto putRequestBenExecutor(InputDto inputDto) throws BeneficiaryException, AccountsException;
    OutputDto getRequestBenExecutor(InputDto inputDto) throws AccountsException, BeneficiaryException;
    OutputDto deleteRequestBenExecutor(InputDto inputDto) throws BeneficiaryException, AccountsException;

}
