package com.example.accountsservices.service;

import com.example.accountsservices.dto.TransactionsDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.TransactionException;

import java.util.List;

public interface ITransactionsService {
    TransactionsDto transactionsExecutor(TransactionsDto transactionsDto) throws TransactionException, AccountsException;
    List<TransactionsDto> getPastSixMonthsTransactionsForAnAccount(Long accountNumber) throws AccountsException;
}
