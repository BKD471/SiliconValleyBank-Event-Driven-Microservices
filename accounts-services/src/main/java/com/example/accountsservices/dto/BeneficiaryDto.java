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
    private AccountsDto.UpdateRequest updateRequest;
    public enum BenUpdateRequest{
        UPDATE_BEN_NAME,CHANGE_DOB,ADD_BEN,UPDATE_BEN,CHANGE_REL,CHANGE_MAIL,CHANGE_PHONE,CHANGE_ADHAR,
        CHANGE_PAN,CHANGE_VOTER,CHANGE_DRV_LC,CHANGE_PASSPORT
    }
    private Beneficiary.RELATION relation;
    private Accounts accounts;
}
