package com.example.accountsservices.controller.Impl;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.service.impl.AccountsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountsController {
    private final AccountsServiceImpl accountsService;

    AccountsController(AccountsServiceImpl accountsService) {
        this.accountsService = accountsService;
    }

    /**
     * @param accountsDto
     * @paramType AccountsDto
     * @ReturnType ResponseEntity<AccountsDto>
     */
    @PostMapping
    public ResponseEntity<AccountsDto> createAccounts(@RequestBody AccountsDto accountsDto) {
        AccountsDto createdAccount = accountsService.createAccounts(accountsDto);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }


    /**
     * @param customerId
     * @paramType Long
     * @ReturnType ResponseEntity<AccountsDto>
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<AccountsDto>> getAllAccountsByCustomerId
            (@PathVariable(name = "id") Long customerId) throws AccountsException {

        List<AccountsDto> fetchedAccountDto = accountsService.getAllAccountsByCustomerId(customerId);
        return new ResponseEntity<>(fetchedAccountDto, HttpStatus.OK);
    }
}
