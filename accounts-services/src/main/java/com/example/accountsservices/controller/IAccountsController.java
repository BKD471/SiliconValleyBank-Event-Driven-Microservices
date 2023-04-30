package com.example.accountsservices.controller;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.InputDto;
import com.example.accountsservices.dto.OutputDto;
import com.example.accountsservices.exception.AccountsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RequestMapping("/api/v1/accounts")
public interface IAccountsController {

    @PutMapping("/update")
    ResponseEntity<OutputDto> requestForChange(@RequestBody InputDto inputDto) throws AccountsException;
    @GetMapping("/{id}")
    ResponseEntity<List<AccountsDto>> getAllAccountsByCustomerId
            (@PathVariable(name = "id") Long customerId) throws AccountsException;
    @GetMapping("/info/{num}")
    ResponseEntity<AccountsDto> getAccountInformation(@PathVariable(name="num") Long accountNumber) throws AccountsException;
}
