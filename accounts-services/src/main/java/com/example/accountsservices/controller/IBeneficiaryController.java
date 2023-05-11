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
    ResponseEntity<OutputDto> getRequestBenForChange(@RequestBody InputDto inputDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException;

    @PostMapping("/post")
    ResponseEntity<OutputDto> postRequestBenForChange(@RequestBody InputDto inputDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException;

    @PutMapping("/put")
    ResponseEntity<OutputDto> putRequestBenForChange(@RequestBody InputDto inputDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException;

    @DeleteMapping("/delete")
    ResponseEntity<OutputDto> deleteRequestBenForChange(@RequestBody InputDto inputDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException;
}
