package com.example.accountsservices.controller;

import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/beneficiary")
public interface IBeneficiaryController {
    @PostMapping("/new")
    ResponseEntity<BeneficiaryDto> addBeneficiary(Long accountNumber, BeneficiaryDto beneficiaryDto) throws AccountsException;

    @GetMapping("/{num}")
    ResponseEntity<List<BeneficiaryDto>> getAllBeneficiariesOfAnAccount(@PathVariable(name="num") Long accountNumber) throws AccountsException;

    @PutMapping("/update/{num}")
    ResponseEntity<BeneficiaryDto> updateBeneficiaryDetails(@PathVariable(name="num") Long accountNumber,@RequestBody BeneficiaryDto beneficiaryDto) throws AccountsException, BeneficiaryException;

    @DeleteMapping("/delete") //Long accountNumber,Long beneficiaryId
    ResponseEntity<String> deleteBeneficiaryForAnAccount(@RequestBody BeneficiaryDto beneficiaryDto) throws AccountsException,BeneficiaryException;

    @DeleteMapping("delete/all/{num}")
    ResponseEntity<String> deleteAllBeneficiaries(@PathVariable(name="num") Long accountNumber) throws  AccountsException;

}
