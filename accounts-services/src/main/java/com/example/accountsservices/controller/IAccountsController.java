package com.example.accountsservices.controller;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.exception.AccountsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RequestMapping("/api/v1/accounts")
public interface IAccountsController {
    @PostMapping("/create")
    ResponseEntity<AccountsDto> createAccounts(@RequestBody AccountsDto accountsDto);

    @PutMapping("/update/{num}")
    ResponseEntity<AccountsDto> updateAccount(@RequestBody AccountsDto accountsDto) throws AccountsException;

    @GetMapping("/{id}")
    ResponseEntity<List<AccountsDto>> getAllAccountsByCustomerId
            (@PathVariable(name = "id") Long customerId) throws AccountsException;

    @GetMapping("/info/{num}")
    ResponseEntity<AccountsDto> getAccountInformation(@PathVariable(name="num") Long accountNumber) throws AccountsException;

    @DeleteMapping("/{num}")
    ResponseEntity<String> deleteAccount(@PathVariable(name="num") Long accountNumber) throws  AccountsException;

    @DeleteMapping("/delete-all/{id}")
    ResponseEntity<String> deleteAllAccountsByCustomer(@PathVariable(name="id") Long customerId) throws AccountsException;

    @PutMapping("/block/{num}")
    ResponseEntity<String> blockAccount(@PathVariable(name="num") Long accountNumber) throws AccountsException;
}
