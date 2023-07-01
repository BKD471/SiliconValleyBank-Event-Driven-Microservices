package com.example.accountsservices.dto.baseDtos;

import com.example.accountsservices.helpers.AllEnumConstantHelpers;
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
    private long beneficiaryId;
    private String beneficiaryName;
    private long beneficiaryAccountNumber;

    @JsonIgnore
    @JsonProperty(value = "benUpdateRequest")
    private AllEnumConstantHelpers.BenUpdateRequest benUpdateRequest;
    private AllEnumConstantHelpers.RELATION relation;
    private LocalDate BenDate_Of_Birth;
    private int benAge;
    private AllEnumConstantHelpers.BanksSupported benBank;
    private String bankCode;
    private String benAdharNumber;
    private String benPhoneNumber;
    private String benPanNumber;
    private String beneficiaryEmail;
    private String benVoterId;
    private String benDrivingLicense;
    private String benPassportNumber;
    private String address;
    private String imageName;
}
