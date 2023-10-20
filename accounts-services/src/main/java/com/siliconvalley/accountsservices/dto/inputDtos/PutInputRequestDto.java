package com.siliconvalley.accountsservices.dto.inputDtos;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.Beneficiary;
import com.siliconvalley.accountsservices.model.Customer;
import com.siliconvalley.accountsservices.model.Transactions;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record PutInputRequestDto(String customerId,String name, int age,String accountNumber,String dateOfBirthInYYYYMMDD,
                                 AllConstantHelpers.AccountType accountType,AllConstantHelpers.Branch homeBranch,
                                 String email,String password, String phoneNumber, String adharNumber, String panNumber,
                                 String voterId,String drivingLicense, String passportNumber, int pageNumber,
                                 int pageSize, String sortBy, AllConstantHelpers.DIRECTION sortDir,
                                 AllConstantHelpers.BenUpdateRequest benRequest, String beneficiaryId, String beneficiaryName,
                                 String beneficiaryAccountNumber, AllConstantHelpers.RELATION bloodRelation, String ben_date_of_birthInYYYYMMDD,
                                 int benAge,AllConstantHelpers.BanksSupported benBank, String benAdharNumber,
                                 String benPhoneNumber,String benPanNumber, String benPassportNumber, String beneficiaryEmail,
                                 String benVoterId,String benDrivingLicense,BigDecimal balance,AllConstantHelpers.UpdateRequest updateRequest,
                                 String branchCode,BigDecimal transferLimitPerDay,int creditScore,String address,
                                 String imageName,AllConstantHelpers.AccountStatus accountStatus,BigDecimal approvedLoanLimitBasedOnCreditScore,
                                 Boolean anyActiveLoans,BigDecimal totLoanIssuedSoFar,BigDecimal totalOutStandingAmountPayableToBank,
                                 MultipartFile customerImage,Set<Beneficiary> beneficiarySet,Set<Transactions> transactionsSet,
                                 Customer customer,Set<Accounts> accountsSet,Accounts accounts) {
    public static final class Builder {
        private String customerId;
        private String name;
        private int age;
        private String accountNumber;
        private String dateOfBirthInYYYYMMDD;
        private AllConstantHelpers.AccountType accountType;
        private AllConstantHelpers.Branch homeBranch;
        private String email;
        private String password;
        private String phoneNumber;
        private String adharNumber;
        private String panNumber;
        private String voterId;
        private String drivingLicense;
        private String passportNumber;
        private int pageNumber;
        private int pageSize;
        private String sortBy;
        private AllConstantHelpers.DIRECTION sortDir;
        private AllConstantHelpers.BenUpdateRequest benRequest;
        private String beneficiaryId;
        private String beneficiaryName;
        private String beneficiaryAccountNumber;
        private AllConstantHelpers.RELATION bloodRelation;
        private String ben_date_of_birthInYYYYMMDD;
        private int benAge;
        private AllConstantHelpers.BanksSupported benBank;
        private String benAdharNumber;
        private String benPhoneNumber;
        private String benPanNumber;
        private String benPassportNumber;
        private String beneficiaryEmail;
        private String benVoterId;
        private String benDrivingLicense;
        private BigDecimal balance;
        private AllConstantHelpers.UpdateRequest updateRequest;
        private String branchCode;
        private BigDecimal transferLimitPerDay;
        private int creditScore;
        private String address;
        private String imageName;
        private AllConstantHelpers.AccountStatus accountStatus;
        private BigDecimal approvedLoanLimitBasedOnCreditScore;
        private Boolean anyActiveLoans;
        private BigDecimal totLoanIssuedSoFar;
        private BigDecimal totalOutStandingAmountPayableToBank;
        private MultipartFile customerImage;
        private Set<Beneficiary> beneficiarySet;
        private Set<Transactions> transactionsSet;
        private Customer customer;
        private Set<Accounts> accountsSet;
        private Accounts accounts;

        public Builder (){};

        public Builder customerId(String customerId){
            this.customerId=customerId;
            return this;
        }
        public Builder name(String name){
            this.name=name;
            return this;
        }
        public Builder age(int age){
            this.age=age;
            return this;
        }
        public Builder accountNumber(String accountNumber){
            this.accountNumber=accountNumber;
            return this;
        }
        public Builder dateOfBirthInYYYYMMDD(String dateOfBirthInYYYYMMDD){
            this.dateOfBirthInYYYYMMDD=dateOfBirthInYYYYMMDD;
            return this;
        }
        public Builder accountType(AllConstantHelpers.AccountType accountType){
            this.accountType=accountType;
            return this;
        }
        public Builder homeBranch(AllConstantHelpers.Branch homeBranch){
            this.homeBranch=homeBranch;
            return this;
        }
        public Builder email(String email){
            this.email=email;
            return this;
        }
        public Builder password(String password){
            this.password=password;
            return this;
        }
        public Builder phoneNumber(String phoneNumber){
            this.phoneNumber=phoneNumber;
            return this;
        }
        public Builder adharNumber(String adharNumber){
            this.adharNumber=adharNumber;
            return this;
        }
        public Builder panNumber(String panNumber){
            this.panNumber=panNumber;
            return this;
        }
        public Builder voterId(String voterId){
            this.voterId=voterId;
            return this;
        }
        public Builder drivingLicense(String drivingLicense){
            this.drivingLicense=drivingLicense;
            return this;
        }
        public Builder passportNumber(String passportNumber){
            this.passportNumber=passportNumber;
            return this;
        }

        public Builder pageNumber(int pageNumber){
            this.pageNumber=pageNumber;
            return this;
        }
        public Builder pageSize(int pageSize){
            this.pageSize=pageSize;
            return this;
        }
        public Builder sortBy(String sortBy){
            this.sortBy=sortBy;
            return this;
        }
        public Builder sortDir(AllConstantHelpers.DIRECTION sortDir){
            this.sortDir=sortDir;
            return this;
        }
        public Builder benRequest(AllConstantHelpers.BenUpdateRequest benRequest){
            this.benRequest=benRequest;
            return this;
        }
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
        public Builder bloodRelation(AllConstantHelpers.RELATION bloodRelation){
            this.bloodRelation=bloodRelation;
            return this;
        }
        public Builder ben_date_of_birthInYYYYMMDD(String ben_date_of_birthInYYYYMMDD){
            this.ben_date_of_birthInYYYYMMDD=ben_date_of_birthInYYYYMMDD;
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
        public Builder benAdharNumber(String benAdharNumber){
            this.benAdharNumber=benAdharNumber;
            return this;
        }
        public Builder benPhoneNumber(String benPhoneNumber){
            this.benPhoneNumber=benPhoneNumber;
            return this;
        }
        public Builder benPanNumber(String benPanNumber){
            this.benPanNumber=benPanNumber;
            return this;
        }
        public Builder benPassportNumber(String benPassportNumber){
            this.benPassportNumber=benPassportNumber;
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
        public Builder benDrivingLicense(String benDrivingLicense){
            this.benDrivingLicense=benDrivingLicense;
            return this;
        }
        public Builder balance(BigDecimal balance){
            this.balance=balance;
            return this;
        }
        public Builder updateRequest(AllConstantHelpers.UpdateRequest updateRequest){
            this.updateRequest=updateRequest;
            return this;
        }
        public Builder branchCode(String branchCode){
            this.branchCode=branchCode;
            return this;
        }
        public Builder transferLimitPerDay(BigDecimal transferLimitPerDay){
            this.transferLimitPerDay=transferLimitPerDay;
            return this;
        }
        public Builder creditScore(int creditScore){
            this.creditScore=creditScore;
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
        public Builder accountStatus(AllConstantHelpers.AccountStatus accountStatus){
            this.accountStatus=accountStatus;
            return this;
        }
        public Builder approvedLoanLimitBasedOnCreditScore(BigDecimal approvedLoanLimitBasedOnCreditScore){
            this.approvedLoanLimitBasedOnCreditScore=approvedLoanLimitBasedOnCreditScore;
            return this;
        }
        public Builder anyActiveLoans(Boolean anyActiveLoans){
            this.anyActiveLoans=anyActiveLoans;
            return this;
        }
        public Builder totLoanIssuedSoFar(BigDecimal totLoanIssuedSoFar){
            this.totLoanIssuedSoFar=totLoanIssuedSoFar;
            return this;
        }
        public Builder totalOutStandingAmountPayableToBank(BigDecimal totalOutStandingAmountPayableToBank){
            this.totalOutStandingAmountPayableToBank=totalOutStandingAmountPayableToBank;
            return this;
        }
        public Builder customerImage(MultipartFile customerImage){
            this.customerImage=customerImage;
            return this;
        }
        public Builder beneficiarySet(Set<Beneficiary> beneficiarySet){
            this.beneficiarySet=beneficiarySet;
            return this;
        }
        public Builder transactionsSet(Set<Transactions> transactionsSet){
            this.transactionsSet=transactionsSet;
            return this;
        }
        public Builder customer(Customer customer){
            this.customer=customer;
            return this;
        }
        public Builder accountsSet(Set<Accounts> accountsSet){
            this.accountsSet=accountsSet;
            return this;
        }
        public Builder accounts(Accounts accounts){
            this.accounts=accounts;
            return this;
        }

        public PutInputRequestDto build(){
            return new PutInputRequestDto(customerId,name,age,accountNumber,dateOfBirthInYYYYMMDD,accountType,homeBranch,email,
              password,phoneNumber,adharNumber,panNumber,voterId,drivingLicense,passportNumber,pageNumber,pageSize,sortBy,sortDir,
              benRequest,beneficiaryId,beneficiaryName,beneficiaryAccountNumber,bloodRelation,ben_date_of_birthInYYYYMMDD,benAge,
              benBank,benAdharNumber,benPhoneNumber,benPanNumber,benPassportNumber,beneficiaryEmail,benVoterId, benDrivingLicense,
              balance,updateRequest,branchCode,transferLimitPerDay,creditScore,address,imageName,accountStatus,approvedLoanLimitBasedOnCreditScore,
              anyActiveLoans,totLoanIssuedSoFar,totalOutStandingAmountPayableToBank,customerImage,beneficiarySet,transactionsSet,customer,
              accountsSet,accounts);
        }
    }
}
