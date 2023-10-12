package com.siliconvalley.accountsservices.dto.baseDtos;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


import java.time.LocalDate;

public record BeneficiaryDto(String beneficiaryId,String beneficiaryName,
                             String beneficiaryAccountNumber,
                             AllConstantHelpers.BenUpdateRequest benUpdateRequest,
                             AllConstantHelpers.RELATION relation,LocalDate BenDate_Of_Birth,
                             int benAge,AllConstantHelpers.BanksSupported benBank,
                             String bankCode,String benAdharNumber,String benPhoneNumber,
                             String benPanNumber,String beneficiaryEmail,String benVoterId,
                             String benPassportNumber,String benDrivingLicense,String address,String imageName){

    public static final class Builder{
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
        private String benPassportNumber;
        private String benDrivingLicense;
        private String address; private String imageName;


        public Builder(){}

        public Builder beneficiaryId(String beneficiaryId){
            this.beneficiaryId=beneficiaryId;
            return this;
        }

        public Builder beneficiaryName(String beneficiaryName){
            this.beneficiaryName=beneficiaryName;
            return this;
        }

        public Builder beneficiaryAccountNumber(String beneficiaryAccountNumber){
            this.beneficiaryAccountNumber=beneficiaryAccountNumber;
            return this;
        }

        public Builder benUpdateRequest(AllConstantHelpers.BenUpdateRequest benUpdateRequest){
            this.benUpdateRequest=benUpdateRequest;
            return this;
        }

        public Builder relation(AllConstantHelpers.RELATION relation){
            this.relation=relation;
            return this;
        }

        public Builder BenDate_Of_Birth(LocalDate BenDate_Of_Birth){
            this.BenDate_Of_Birth=BenDate_Of_Birth;
            return this;
        }

        public Builder benAge(int benAge){
            this.benAge=benAge;
            return this;
        }

        public Builder benBank(AllConstantHelpers.BanksSupported benBank){
            this.benBank=benBank;
            return this;
        }

        public Builder bankCode(String bankCode){
            this.bankCode=bankCode;
            return this;
        }

        public Builder benPhoneNumber(String benPhoneNumber){
            this.benPhoneNumber=benPhoneNumber;
            return this;
        }

        public Builder benAdharNumber(String benAdharNumber){
            this.benAdharNumber=benAdharNumber;
            return this;
        }

        public Builder benPanNumber(String benPanNumber){
            this.benPanNumber=benPanNumber;
            return this;
        }

        public Builder beneficiaryEmail(String beneficiaryEmail){
            this.beneficiaryEmail=beneficiaryEmail;
            return this;
        }

        public Builder benVoterId(String benVoterId){
            this.benVoterId=benVoterId;
            return this;
        }

        public Builder benPassportNumber(String benPassportNumber){
            this.benPassportNumber=benPassportNumber;
            return this;
        }

        public Builder benDrivingLicense(String benDrivingLicense){
            this.benDrivingLicense=benDrivingLicense;
            return this;
        }

        public Builder address(String address){
            this.address=address;
            return this;
        }

        public Builder imageName(String imageName){
            this.imageName=imageName;
            return this;
        }

        public BeneficiaryDto build(){
            return new BeneficiaryDto(beneficiaryId,beneficiaryName,
                    beneficiaryAccountNumber,benUpdateRequest,relation,BenDate_Of_Birth,benAge,
                    benBank,bankCode,benAdharNumber,benPhoneNumber,benPanNumber,beneficiaryEmail,
                    benVoterId,benPassportNumber,benDrivingLicense,address,imageName);
        }
    }
}

