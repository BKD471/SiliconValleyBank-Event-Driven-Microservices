package com.example.accountsservices.service;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;

import java.util.List;

public interface BeneficiaryService {
    AccountsDto addBeneficiary(Long customerId, Long accountNumber, BeneficiaryDto beneficiaryDto) throws AccountsException;
    BeneficiaryDto updateBeneficiaryDetailsOfaCustomerByBeneficiaryId(Long customerId,Long accountNumber,BeneficiaryDto beneficiaryDto) throws  AccountsException, BeneficiaryException;
    List<BeneficiaryDto> getAllBeneficiariesOfAnAccountByCustomerIdAndLoanNumber(Long customerId, Long accountNumber) throws BeneficiaryException;
    void  deleteBeneficiaries(Long beneficiaryId);
}
