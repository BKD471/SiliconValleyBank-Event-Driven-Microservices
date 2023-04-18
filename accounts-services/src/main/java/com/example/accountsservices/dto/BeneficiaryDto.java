package com.example.accountsservices.dto;

import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public  class BeneficiaryDto extends BaseDto {
    protected LocalDate DateOfBirth;
    private Long beneficiaryId;
    private String beneficiaryName;
    private Long beneficiaryAccountNumber;
    private Beneficiary.RELATION relation;
    private Accounts accounts;
}
