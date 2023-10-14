package com.siliconvalley.accountsservices.service;

import com.siliconvalley.accountsservices.dto.baseDtos.BankStatementRequestDto;
import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.TransactionException;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ITransactionsService {
    OutputDto transactionsExecutor(final TransactionsDto transactionsDto) throws TransactionException, AccountsException;
    void getPastSixMonthsTransactionsForAnAccount(final String accountNumber, final AllConstantHelpers.FORMAT_TYPE formatType) throws AccountsException, JRException, FileNotFoundException;
}
