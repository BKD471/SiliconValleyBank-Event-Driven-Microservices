package com.example.accountsservices.service;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.model.Accounts;

public interface AccountsService {
    AccountsDto createAccounts(AccountsDto accountsDto);
    AccountsDto getAccountByCustomerId(Long id);
    AccountsDto updateAccountByCustomerIdAndAccountNumber(Long customerId,Long accountNumber);
    BeneficiaryDto updateBeneficiaryDetailsByBeneficiaryId(Long customerId,Long BeneficiaryId,BeneficiaryDto beneficiaryDto);

    AccountsDto creditMoney(Long customerId, Long accountNumberRecipient, Long accountNumberSender);
    AccountsDto debitMoney(Long customerId,Long accountNumberSource,Long accountNumberDestination);


}
