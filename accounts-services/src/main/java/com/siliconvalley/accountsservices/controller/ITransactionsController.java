package com.siliconvalley.accountsservices.controller;

import com.siliconvalley.accountsservices.dto.baseDtos.BankStatementRequestDto;
import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.TransactionException;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.FileNotFoundException;
import java.io.IOException;


@RequestMapping("/api/acnt")
public interface ITransactionsController {
    @PostMapping("/transactions/v1/exe")
    ResponseEntity<OutputDto> executeTransactions(@Valid @RequestBody final TransactionsDto transactionsDto) throws TransactionException, AccountsException;

    @GetMapping("/transactions/v1/pastSixMonthsStmt")
    ResponseEntity<Resource> getPastSixMonthsTransaction(@RequestBody final TransactionsDto transactionsDto) throws AccountsException, JRException, IOException;

    @GetMapping("/transactions/v1/generateStatement")
    ResponseEntity<InputStreamResource> generateBankStatementPdf(@RequestBody BankStatementRequestDto bankStatementRequestDto) throws FileNotFoundException;

    @GetMapping("/transactions/v2/generateStatement")
    ResponseEntity<Resource> generateBankStatementAnyFormat(@RequestBody BankStatementRequestDto bankStatementRequestDto) throws IOException, JRException;

}
