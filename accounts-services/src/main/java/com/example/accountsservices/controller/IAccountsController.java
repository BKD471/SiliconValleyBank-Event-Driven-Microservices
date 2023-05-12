package com.example.accountsservices.controller;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.InputDto;
import com.example.accountsservices.dto.OutputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.CustomerException;
import com.example.accountsservices.exception.ResponseException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RequestMapping("/api/v1/accounts")
public interface IAccountsController {
    @GetMapping("/get")
    ResponseEntity<OutputDto> getRequestForChange(@Valid @RequestBody InputDto inputDto) throws AccountsException, ResponseException, CustomerException;
    @PostMapping("/post")
    ResponseEntity<OutputDto> postRequestForChange(@Valid @RequestBody InputDto inputDto) throws AccountsException, ResponseException, CustomerException;
    @PutMapping("/put")
    ResponseEntity<OutputDto>  putRequestForChange(@Valid @RequestBody InputDto inputDto) throws AccountsException, ResponseException, CustomerException;
    @DeleteMapping("/delete")
    ResponseEntity<OutputDto>  deleteRequestForChange(@Valid @RequestBody InputDto inputDto) throws AccountsException, ResponseException, CustomerException;
}
