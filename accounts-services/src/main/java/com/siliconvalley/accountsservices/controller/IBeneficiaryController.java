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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RequestMapping("/api/acnt")
public interface IBeneficiaryController {
    @GetMapping("/beneficiary/v1/get")
    ResponseEntity<OutputDto> getRequestBenForChange(@Valid @RequestBody final GetInputRequestDto getInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException;
    @PostMapping("/beneficiary/v1/post")
    ResponseEntity<OutputDto> postRequestBenForChange(@Valid @RequestBody final PostInputRequestDto postInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException;
    @PutMapping("/beneficiary/v1/put")
    ResponseEntity<OutputDto> putRequestBenForChange(@Valid @RequestBody final PutInputRequestDto putInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException;
    @DeleteMapping("/beneficiary/v1/delete")
    ResponseEntity<OutputDto> deleteRequestBenForChange(@Valid @RequestBody final DeleteInputRequestDto deleteInputRequestDto) throws AccountsException, ResponseException, CustomerException, BeneficiaryException;
}
