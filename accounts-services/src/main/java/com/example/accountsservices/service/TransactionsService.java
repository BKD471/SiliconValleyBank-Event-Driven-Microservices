package com.example.accountsservices.service;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.TransactionsDto;

import java.util.List;

public interface TransactionsService {
    TransactionsDto creditMoney(Long customerId, Long accountNumberRecipient, Long accountNumberSender);
    TransactionsDto debitMoney(Long customerId,Long accountNumberSource,Long accountNumberDestination);
    List<TransactionsDto> getAllTransactionsForAnAccount(Long customerId, Long accountNumber);
}
