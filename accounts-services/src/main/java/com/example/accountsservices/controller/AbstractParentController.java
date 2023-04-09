package com.example.accountsservices.controller;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.TransactionsDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.exception.TransactionException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public abstract class AbstractParentController implements  IAccountsController,
        IBeneficiaryController,ITransactionsController{

    @Override
    public ResponseEntity<AccountsDto> createAccounts(AccountsDto accountsDto) {return null;}
    @Override
    public ResponseEntity<AccountsDto> updateAccount(AccountsDto accountsDto) throws AccountsException {return null;}
    @Override
    public ResponseEntity<List<AccountsDto>> getAllAccountsByCustomerId(Long customerId) throws AccountsException {return null;}
    @Override
    public ResponseEntity<AccountsDto> getAccountInformation(Long accountNumber) throws AccountsException {return null;}
    @Override
    public ResponseEntity<String> deleteAccount(Long accountNumber) throws AccountsException {return null;}
    @Override
    public ResponseEntity<String> deleteAllAccountsByCustomer(Long customerId) throws AccountsException {return null;}
    @Override
    public ResponseEntity<String> blockAccount(Long accountNumber) throws AccountsException {return null;}
    @Override
    public ResponseEntity<BeneficiaryDto> addBeneficiary(Long accountNumber, BeneficiaryDto beneficiaryDto) throws AccountsException {return null;}
    @Override
    public ResponseEntity<List<BeneficiaryDto>> getAllBeneficiariesOfAnAccount(Long accountNumber) throws AccountsException {return null;}
    @Override
    public ResponseEntity<BeneficiaryDto> updateBeneficiaryDetails(Long accountNumber, BeneficiaryDto beneficiaryDto) throws AccountsException, BeneficiaryException {return null;}
    @Override
    public ResponseEntity<String> deleteBeneficiaryForAnAccount(BeneficiaryDto beneficiaryDto) throws AccountsException, BeneficiaryException {return null;}
    @Override
    public ResponseEntity<String> deleteAllBeneficiaries(Long accountNumber) throws AccountsException {return null;}
    @Override
    public ResponseEntity<TransactionsDto> executeTransactions(TransactionsDto transactionsDto) throws TransactionException,AccountsException{ return null;}
    @Override
    public  ResponseEntity<List<TransactionsDto>> getPastSixMonthsTransaction(Long accountNumber) throws AccountsException{ return null;}
}
