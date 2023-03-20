package com.example.accountsservices.controller;

import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.bankdata.dto.CustomerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountsController {

    private AccountsRepository accountsRepository;
    AccountsController(AccountsRepository accountsRepository){
        this.accountsRepository=accountsRepository;
    }

    @PostMapping
    public ResponseEntity<String> createAccounts(@RequestBody CustomerDto customerDto){
        Accounts accnt=new Accounts();

        //accountsRepository.save()
        return new ResponseEntity<>("to be continues",HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Accounts> getAccounts(@RequestBody CustomerDto customerdto){
        Accounts fetchedAccnt=accountsRepository.findByCustomerId(customerdto.getCustomerId());
        return new ResponseEntity<>(fetchedAccnt,HttpStatus.OK);
    }
}
