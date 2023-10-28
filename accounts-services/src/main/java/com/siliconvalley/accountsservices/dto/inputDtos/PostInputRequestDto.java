package com.siliconvalley.accountsservices.dto.inputDtos;

import com.siliconvalley.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.Customer;
import com.siliconvalley.accountsservices.validator.ValidAge;
import com.siliconvalley.accountsservices.helpers.RegexMatchersHelper;
import com.siliconvalley.loansservices.helpers.AllConstantsHelper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.Set;
import java.math.BigDecimal;


public record PostInputRequestDto(String customerId,
                                  @Size(min=3,max = 60,message = "Name must be at least 3 and at most 60 chars long")
        @Schema(name = "username",accessMode = Schema.AccessMode.READ_ONLY,description="name of user")
        String name, int age, String accountNumber,
                                  @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_DOB,
                message = "Please provide DOB in YYYY-MM-DD format")
        @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_NOT_NULL_CHARS,message = "Please mention date")
        @ValidAge
         String dateOfBirthInYYYYMMDD, AllConstantHelpers.AccountType accountType, AllConstantHelpers.Branch homeBranch,

                                  @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_EMAIL,
                message = "Invalid Email format")
         String email,

                                  @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_PASSWORD,message = "Minimum eight characters," +
                " at least one uppercase" +
                " letter, one lowercase letter, " +
                "one number and " +
                "one special character")
         String password,

                                  @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_PHONE_NUMBER
                ,message = "Invalid phone Number format")
         String phoneNumber,

                                  @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_ADHAR
                ,message = "Invalid Adhar Number")
         String adharNumber,

                                  @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_PAN_NUMBER,
                message = "Invalid Pan Number")
         String panNumber,

                                  @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_VOTER,message = "Invalid voterId")
         String voterId,

                                  @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_DRIVING_LICENSE,
                message = "Invalid Driving License")
         String drivingLicense,

                                  @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_PASSPORT,message = "Invalid Passport Number")
         String passportNumber,
                                  AllConstantHelpers.BenUpdateRequest benRequest, String beneficiaryId,
                                  @Size(min=3,max = 60,message = "Beneficiary Name must be at least 3 and at most 60 chars long")
         String beneficiaryName,

                                  @Size(min=10,max = 1000,message = "Please provide address")
         String address, String imageName, String beneficiaryAccountNumber, AllConstantHelpers.RELATION bloodRelation,
                                  int benAge, AllConstantHelpers.BanksSupported benBank, BigDecimal balance, AllConstantHelpers.UpdateRequest updateRequest,
                                  String branchCode, BigDecimal transferLimitPerDay, int creditScore, AllConstantHelpers.AccountStatus accountStatus,
                                  BigDecimal approvedLoanLimitBasedOnCreditScore, Boolean anyActiveLoans, BigDecimal totLoanIssuedSoFar, BigDecimal totalOutStandingAmountPayableToBank,
                                  MultipartFile customerImage, Set<BeneficiaryDto> listOfBeneficiary, Set<TransactionsDto> listOfTransactions,
                                  Customer customer, Set<Accounts> accountsSet, Accounts account){

    public static final class Builder{
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
        private AllConstantHelpers.BenUpdateRequest benRequest;
        private String beneficiaryId;
        private String beneficiaryName;
        private String address;
        private String imageName;
        private String beneficiaryAccountNumber;
        private AllConstantHelpers.RELATION bloodRelation;
        private int benAge;
        private AllConstantHelpers.BanksSupported benBank;
        private BigDecimal balance;
        private AllConstantHelpers.UpdateRequest updateRequest;
        private String branchCode;
        private BigDecimal transferLimitPerDay;
        private int creditScore;
        private AllConstantHelpers.AccountStatus accountStatus;
        private BigDecimal approvedLoanLimitBasedOnCreditScore;
        private Boolean anyActiveLoans;
        private BigDecimal totLoanIssuedSoFar;
        private BigDecimal totalOutStandingAmountPayableToBank;
        private MultipartFile customerImage;
        private Set<BeneficiaryDto> listOfBeneficiary ;
        private Set<TransactionsDto> listOfTransactions ;
        private Customer customer;
        private Set<Accounts> accountsSet;
        private Accounts account;

        public Builder(){}

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
        public Builder address(String address){
            this.address=address;
            return this;
        }
        public Builder imageName(String imageName){
            this.imageName=imageName;
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
        public Builder benAge(int benAge){
            this.benAge=benAge;
            return this;
        }
        public Builder benBank(AllConstantHelpers.BanksSupported benBank){
            this.benBank=benBank;
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
        public Builder listOfBeneficiary(Set<BeneficiaryDto> listOfBeneficiary){
            this.listOfBeneficiary=listOfBeneficiary;
            return this;
        }
        public Builder listOfTransactions(Set<TransactionsDto> listOfTransactions){
            this.listOfTransactions=listOfTransactions;
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
        public Builder account(Accounts account){
            this.account=account;
            return this;
        }


        public PostInputRequestDto build(){
            return new PostInputRequestDto(customerId,name,age,accountNumber,dateOfBirthInYYYYMMDD,accountType,homeBranch,email,password,phoneNumber,
                    adharNumber,panNumber,voterId,drivingLicense,passportNumber,benRequest,beneficiaryId,beneficiaryName,address,imageName,
                    beneficiaryAccountNumber,bloodRelation,benAge,benBank,balance,updateRequest,branchCode,transferLimitPerDay,creditScore,
                    accountStatus,approvedLoanLimitBasedOnCreditScore,anyActiveLoans,totLoanIssuedSoFar,totalOutStandingAmountPayableToBank,
                    customerImage,listOfBeneficiary,listOfTransactions,customer,accountsSet,account);
        }
    }
}

