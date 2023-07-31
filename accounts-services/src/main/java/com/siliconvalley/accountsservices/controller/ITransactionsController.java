package com.siliconvalley.accountsservices.controller;

import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.TransactionException;
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