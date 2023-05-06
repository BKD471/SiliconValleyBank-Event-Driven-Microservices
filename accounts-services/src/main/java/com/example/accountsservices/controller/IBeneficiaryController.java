package com.example.accountsservices.controller;

import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.InputDto;
import com.example.accountsservices.dto.OutputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.exception.CustomerException;
import com.example.accountsservices.exception.ResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/v1/beneficiary")
public interface IBeneficiaryController {
    @GetMapping("/get")
    ResponseEntity<OutputDto> getRequestForChange(@RequestBody InputDto inputDto) throws AccountsException, ResponseException, CustomerException;

    @PostMapping("/post")
    ResponseEntity<OutputDto> postRequestForChange(@RequestBody InputDto inputDto) throws AccountsException, ResponseException, CustomerException;

    @PutMapping("/put")
    ResponseEntity<OutputDto> putRequestForChange(@RequestBody InputDto inputDto) throws AccountsException, ResponseException, CustomerException;

    @DeleteMapping("/delete")
    ResponseEntity<OutputDto> deleteRequestForChange(@RequestBody InputDto inputDto) throws AccountsException, ResponseException, CustomerException;
}
