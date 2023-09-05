package com.siliconvalley.accountsservices.service;

import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.TransactionException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ITransactionsService {
    OutputDto transactionsExecutor(final TransactionsDto transactionsDto) throws TransactionException, AccountsException;
    OutputDto getPastSixMonthsTransactionsForAnAccount(final String accountNumber) throws AccountsException;
}
