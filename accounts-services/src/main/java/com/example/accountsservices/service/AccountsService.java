package com.example.accountsservices.service;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.TransactionsDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.model.Accounts;

import java.util.List;

public interface AccountsService {
    AccountsDto createAccounts(AccountsDto accountsDto);
    AccountsDto getAccountInfoByCustomerIdAndAccountNumber(Long customerId,Long accountNumber) throws AccountsException;
    List<AccountsDto> getAllAccountsByCustomerId(Long customerId) throws AccountsException;
    AccountsDto updateAccountByCustomerIdAndAccountNumber(Long customerId,Long accountNumber,AccountsDto accountsDto) throws AccountsException;

    AccountsDto addBeneficiary(Long customerId,Long accountNumber,BeneficiaryDto beneficiaryDto) throws AccountsException;
    BeneficiaryDto updateBeneficiaryDetailsOfaCustomerByBeneficiaryId(Long customerId,Long accountNumber,Long BeneficiaryId,BeneficiaryDto beneficiaryDto);
    List<BeneficiaryDto> getAllBeneficiariesOfAnAccountByCustomerIdAndLoanNumber(Long customerId,Long accountNumber) throws BeneficiaryException;
    AccountsDto creditMoney(Long customerId, Long accountNumberRecipient, Long accountNumberSender);
    AccountsDto debitMoney(Long customerId,Long accountNumberSource,Long accountNumberDestination);
    List<TransactionsDto> getAllTransactionsForAnAccount(Long customerId,Long accountNumber);
}
