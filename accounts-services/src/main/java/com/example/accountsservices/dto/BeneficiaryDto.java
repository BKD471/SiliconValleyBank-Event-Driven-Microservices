package com.example.accountsservices.dto;

import com.example.accountsservices.model.Beneficiary;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiaryDto {
    private Long beneficiaryId;
    private String beneficiaryName;
    private Long beneficiaryAccountNumber;

    @JsonIgnore
    @JsonProperty(value = "benUpdateRequest")
    private BenUpdateRequest benUpdateRequest;

    public enum BenUpdateRequest {
        ADD_BEN,
        UPDATE_BEN,
        GET_ALL_BEN,
        GET_BEN,
        DELETE_BEN,
        DELETE_ALL_BEN
    }

    private Beneficiary.RELATION relation;
    private LocalDate BenDate_Of_Birth;
    private int benAge;
    private Beneficiary.BanksSupported benBank;
    private String bankCode;
    private String benAdharNumber;
    private String benPhoneNumber;
    private String benPanNumber;
    private String beneficiaryEmail;
    private String benVoterId;
    private String benDrivingLicense;
    private String benPassportNumber;

}
