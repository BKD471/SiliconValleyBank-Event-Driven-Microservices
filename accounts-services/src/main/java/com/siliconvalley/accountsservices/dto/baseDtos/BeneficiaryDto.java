package com.siliconvalley.accountsservices.dto.baseDtos;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
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
    private String beneficiaryId;
    private String beneficiaryName;
    private String beneficiaryAccountNumber;

    @JsonIgnore
    @JsonProperty(value = "benUpdateRequest")
    private AllConstantHelpers.BenUpdateRequest benUpdateRequest;
    private AllConstantHelpers.RELATION relation;
    private LocalDate BenDate_Of_Birth;
    private int benAge;
    private AllConstantHelpers.BanksSupported benBank;
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
