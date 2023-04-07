package com.example.accountsservices.dto;

import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public  class BeneficiaryDto extends AbstractBaseDto {
    private Long beneficiaryId;
    private String beneficiaryName;
    private Long beneficiaryAccountNumber;
    private Beneficiary.RELATION relation;
    private LocalDate Date_Of_Birth;
    private Accounts accounts;
}
