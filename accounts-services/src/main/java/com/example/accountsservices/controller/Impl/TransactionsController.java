package com.example.accountsservices.controller.Impl;

import com.example.accountsservices.controller.AbstractParentController;
import com.example.accountsservices.dto.TransactionsDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.TransactionException;
import com.example.accountsservices.service.impl.TransactionsAccountServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionsController extends AbstractParentController {
    private final TransactionsAccountServiceImpl transactionsAccountService;
    TransactionsController(TransactionsAccountServiceImpl transactionsAccountService){
        this.transactionsAccountService=transactionsAccountService;
    }


    /**
     * @param transactionsDto
     * @return
     * @throws TransactionException
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<TransactionsDto> executeTransactions(TransactionsDto transactionsDto) throws TransactionException, AccountsException {
        return null;
    }

    /**
     * @param accountNumber
     * @return
     * @throws AccountsException
     */
    @Override
    public ResponseEntity<List<TransactionsDto>> getPastSixMonthsTransaction(Long accountNumber) throws AccountsException {
        return null;
    }
}
