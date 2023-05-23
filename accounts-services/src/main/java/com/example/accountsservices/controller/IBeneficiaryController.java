package com.example.accountsservices.controller;

import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.exception.CustomerException;
import com.example.accountsservices.exception.ResponseException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/beneficiary")
public interface IBeneficiaryController {
    @GetMapping("/get")
    ResponseEntity<OutputDto> getRequestBenForChange(@Valid @RequestBody GetInputRequestDto getInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException;

    @PostMapping("/post")
    ResponseEntity<OutputDto> postRequestBenForChange(@Valid @RequestBody PostInputRequestDto postInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException;

    @PutMapping("/put")
    ResponseEntity<OutputDto> putRequestBenForChange(@Valid @RequestBody PutInputRequestDto putInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException;

    @DeleteMapping("/delete")
    ResponseEntity<OutputDto> deleteRequestBenForChange(@Valid @RequestBody DeleteInputRequestDto deleteInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException;
}
