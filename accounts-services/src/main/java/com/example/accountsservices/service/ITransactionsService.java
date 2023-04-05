package com.example.accountsservices.service;

import com.example.accountsservices.dto.TransactionsDto;

import java.util.List;

public interface ITransactionsService {
    TransactionsDto depositMoney(Long accountNUmber,Long accountNumberSender,Long amount);
    TransactionsDto transferMoneyToOtherAccounts(Long accountNumber,Long accountNumberDestination,Long amount);
    List<TransactionsDto> getPastSixMonthsTransactionsForAnAccount(Long accountNumber);

    TransactionsDto payBills(Long accountNumber,TransactionsDto transactionsDto);
}
