package com.example.accountsservices.controller;

import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.dto.baseDtos.TransactionsDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.TransactionException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/transactions")
public interface ITransactionsController {
    @PostMapping("/exe")
    ResponseEntity<OutputDto> executeTransactions(@Valid @RequestBody final TransactionsDto transactionsDto) throws TransactionException, AccountsException;

    @GetMapping("/{num}")
    ResponseEntity<OutputDto> getPastSixMonthsTransaction(@Valid @PathVariable(name="num") final String accountNumber) throws AccountsException;
}
