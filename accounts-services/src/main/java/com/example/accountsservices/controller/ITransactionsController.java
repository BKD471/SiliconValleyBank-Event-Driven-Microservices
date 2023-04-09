package com.example.accountsservices.controller;

import com.example.accountsservices.dto.TransactionsDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.TransactionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public interface ITransactionsController {

    @PostMapping
    ResponseEntity<TransactionsDto> executeTransactions(@RequestBody TransactionsDto transactionsDto) throws TransactionException, AccountsException;

    @GetMapping("/{num}")
    ResponseEntity<List<TransactionsDto>> getPastSixMonthsTransaction(@PathVariable(name="num") Long accountNumber) throws AccountsException;
}
