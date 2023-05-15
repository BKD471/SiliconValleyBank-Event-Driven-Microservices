package com.example.accountsservices.controller;

import com.example.accountsservices.dto.OutputDto;
import com.example.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.CustomerException;
import com.example.accountsservices.exception.ResponseException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/accounts")
public interface IAccountsController {
    @GetMapping("/get")
    ResponseEntity<OutputDto> getRequestForChange(@Valid @RequestBody GetInputRequestDto getInputRequestDto) throws AccountsException, ResponseException, CustomerException;

    @PostMapping("/post")
    ResponseEntity<OutputDto> postRequestForChange(@Valid @RequestBody PostInputRequestDto postInputDto) throws AccountsException, ResponseException, CustomerException;

    @PutMapping("/put")
    ResponseEntity<OutputDto> putRequestForChange(@Valid @RequestBody PutInputRequestDto putInputRequestDto) throws AccountsException, ResponseException, CustomerException;

    @DeleteMapping("/delete")
    ResponseEntity<OutputDto> deleteRequestForChange(@Valid @RequestBody DeleteInputRequestDto deleteInputRequestDto) throws AccountsException, ResponseException, CustomerException;
}
