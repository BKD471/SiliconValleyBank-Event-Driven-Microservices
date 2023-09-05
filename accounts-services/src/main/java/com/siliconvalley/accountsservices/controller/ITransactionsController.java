package com.siliconvalley.accountsservices.controller;

import com.siliconvalley.accountsservices.dto.baseDtos.BankStatementRequestDto;
import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.TransactionException;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;


@RequestMapping("/api")
public interface ITransactionsController {
    @PostMapping("/v1/transactions/exe")
    ResponseEntity<OutputDto> executeTransactions(@Valid @RequestBody final TransactionsDto transactionsDto) throws TransactionException, AccountsException;

    @GetMapping("/v1/transactions/{num}")
    ResponseEntity<OutputDto> getPastSixMonthsTransaction(@Valid @PathVariable(name="num") final String accountNumber) throws AccountsException;

    @GetMapping("/v1/transactions/generateStatement")
    ResponseEntity<InputStreamResource> generateBankStatementPdf(@RequestBody BankStatementRequestDto bankStatementRequestDto) throws FileNotFoundException;

    @GetMapping("/v2/transactions/generateStatement")
    ResponseEntity<String> generateBankStatementAnyFormat(@RequestBody BankStatementRequestDto bankStatementRequestDto) throws FileNotFoundException, JRException;

}
