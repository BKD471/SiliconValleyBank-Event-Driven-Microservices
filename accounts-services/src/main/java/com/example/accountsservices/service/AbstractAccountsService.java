package com.example.accountsservices.service;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.TransactionsDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;

import java.util.List;

//DESIGN NOTE
//Obeying I of SOLID ,to not pollute a single interface with too much methods
// by splitting it up.
//we want to have a single parent of all service classes so need a class to provide
// dummy implementation of all abstract methods of all interface
// so that we can spilt up service logic into separate concerning classes
// and thus obeying S of SOLID also

//since there is no need of instantiating the AbstractAccountsService ,
// it's just to provide dummy implementation ,so make it abstract
// it can be used for loose coupling

//samajhle phir ,baad main mat bolna, mera code quality ghatiya hay
// or such comments like bro you dont follow good coding standards :)
//ismein tera ghar chala jayenga
public abstract class AbstractAccountsService implements AccountsService, TransactionsService, BeneficiaryService {
    public AccountsDto createAccounts(AccountsDto accountsDto) {
        return null;
    }

    public AccountsDto getAccountInfoByCustomerIdAndAccountNumber(Long customerId, Long accountNumber) throws AccountsException {
        return null;
    }

    public List<AccountsDto> getAllAccountsByCustomerId(Long customerId) throws AccountsException {
        return null;
    }

    public AccountsDto updateAccountByCustomerIdAndAccountNumber(Long customerId, Long accountNumber, AccountsDto accountsDto) throws AccountsException {
        return null;
    }

    public void deleteAccount(Long accountNumber) {
        //dummy implementation
    }

    public AccountsDto addBeneficiary(Long customerId, Long accountNumber, BeneficiaryDto beneficiaryDto) throws AccountsException {
        return null;
    }

    public BeneficiaryDto updateBeneficiaryDetailsOfaCustomerByBeneficiaryId(Long customerId, Long accountNumber, BeneficiaryDto beneficiaryDto) throws AccountsException, BeneficiaryException {
        return null;
    }

    public List<BeneficiaryDto> getAllBeneficiariesOfAnAccountByCustomerIdAndLoanNumber(Long customerId, Long accountNumber) throws BeneficiaryException {
        return null;
    }

    public void deleteBeneficiaries(Long beneficiaryId) {
        //dummy implementation
    }

    public TransactionsDto creditMoney(Long customerId, Long accountNumberRecipient, Long accountNumberSender) {
        return null;
    }

    public TransactionsDto debitMoney(Long customerId, Long accountNumberSource, Long accountNumberDestination) {
        return null;
    }

    public List<TransactionsDto> getAllTransactionsForAnAccount(Long customerId, Long accountNumber) {
        return null;
    }
}
