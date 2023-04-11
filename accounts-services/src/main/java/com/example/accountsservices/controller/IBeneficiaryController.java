package com.example.accountsservices.controller;

import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RequestMapping("/api/v1/beneficiary")
public interface IBeneficiaryController {
    @PostMapping("/create/{num}")
    ResponseEntity<BeneficiaryDto> addBeneficiary(@PathVariable(name="num") Long accountNumber,@RequestBody BeneficiaryDto beneficiaryDto) throws AccountsException;

    @GetMapping("/all/{num}")
    ResponseEntity<List<BeneficiaryDto>> getAllBeneficiariesOfAnAccount(@PathVariable(name="num") Long accountNumber) throws AccountsException;

    @PutMapping("/update/{num}")
    ResponseEntity<BeneficiaryDto> updateBeneficiaryDetails(@PathVariable(name="num") Long accountNumber,@RequestBody BeneficiaryDto beneficiaryDto) throws AccountsException, BeneficiaryException;

    @DeleteMapping("/delete/{num}") //Long accountNumber,Long beneficiaryId
    ResponseEntity<String> deleteBeneficiaryForAnAccount(@PathVariable(name="num") Long accountNumber,@RequestBody BeneficiaryDto beneficiaryDto) throws AccountsException,BeneficiaryException;

    @DeleteMapping("/delete-all/{num}")
    ResponseEntity<String> deleteAllBeneficiaries(@PathVariable(name="num") Long accountNumber) throws  AccountsException;

}
