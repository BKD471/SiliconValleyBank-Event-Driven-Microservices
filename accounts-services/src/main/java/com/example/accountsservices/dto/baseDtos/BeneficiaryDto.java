package com.example.accountsservices.dto.baseDtos;

import com.example.accountsservices.helpers.AllConstantHelpers;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class BeneficiaryDto {
    private final String beneficiaryId;
    private final String beneficiaryName;
    private final String beneficiaryAccountNumber;

    @JsonIgnore
    @JsonProperty(value = "benUpdateRequest")
    private final AllConstantHelpers.BenUpdateRequest benUpdateRequest;
    private final AllConstantHelpers.RELATION relation;
    private final LocalDate BenDate_Of_Birth;
    private final int benAge;
    private final AllConstantHelpers.BanksSupported benBank;
    private final String bankCode;
    private final String benAdharNumber;
    private final String benPhoneNumber;
    private final String benPanNumber;
    private final String beneficiaryEmail;
    private final String benVoterId;
    private final String benDrivingLicense;
    private final String benPassportNumber;
    private final String address;
    private final String imageName;
}
