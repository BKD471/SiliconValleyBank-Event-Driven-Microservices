package com.siliconvalley.accountsservices.controller;

import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.BeneficiaryException;
import com.siliconvalley.accountsservices.exception.CustomerException;
import com.siliconvalley.accountsservices.exception.ResponseException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/acnt/v1/beneficiary")
public interface IBeneficiaryController {
    @GetMapping("/get")
    ResponseEntity<OutputDto> getRequestBenForChange(@Valid @RequestBody final GetInputRequestDto getInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException;
    @PostMapping("/post")
    ResponseEntity<OutputDto> postRequestBenForChange(@Valid @RequestBody final PostInputRequestDto postInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException;
    @PutMapping("/put")
    ResponseEntity<OutputDto> putRequestBenForChange(@Valid @RequestBody final PutInputRequestDto putInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException;
    @DeleteMapping("/delete")
    ResponseEntity<OutputDto> deleteRequestBenForChange(@Valid @RequestBody final DeleteInputRequestDto deleteInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException;
}
