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
    public enum BenUpdateRequest{
        ADD_BEN,
        UPDATE_BEN,
        GET_ALL_BEN,
        GET_BEN,
        DELETE_BEN,
        DELETE_ALL_BEN
    }
    private Beneficiary.RELATION relation;
    private Accounts accounts;
}
