package com.example.accountsservices.dto.inputDtos;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.TransactionsDto;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.model.Transactions;
import com.example.accountsservices.validator.ValidAge;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.accountsservices.helpers.RegexMatchersHelper.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostInputRequestDto {
    private Long customerId;

    @Size(min=3,max = 60,message = "Name must be at least 3 and at most 60 chars long")
    private String name;
    private int age;
    private Long accountNumber;

    @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$",
            message = "Please provide DOB in YYYY-MM-DD format")
    @ValidAge
    private String dateOfBirthInYYYYMMDD;

    private Accounts.AccountType accountType;
    private Accounts.Branch homeBranch;

    @Pattern(regexp = PATTERN_FOR_EMAIL,
            message = "Invalid Email format")
    private String email;

    @Pattern(regexp = PATTERN_FOR_PHONE_NUMBER
    ,message = "Invalid phone Number format")
    private String phoneNumber;

    @Pattern(regexp = PATTERN_FOR_ADHAR
    ,message = "Invalid Aadhar Number")
    private String adharNumber;

    @Pattern(regexp = PATTERN_FOR_PAN_NUMBER,
    message = "Invalid Pan Number")
    private String panNumber;

    @Pattern(regexp = PATTERN_FOR_VOTER,message = "Invalid voterId")
    private String voterId;

    @Pattern(regexp = PATTERN_FOR_DRIVING_LICENSE,
     message = "Invalid Driving License")
    private String drivingLicense;

    @Pattern(regexp = PATTERN_FOR_PASSPORT,message = "Invalid Passport Number")
    private String passportNumber;

    private BeneficiaryDto.BenUpdateRequest benRequest;
    private Long beneficiaryId;

    @Size(min=3,max = 60,message = "Beneficiary Name must be at least 3 and at most 60 chars long")
    private String beneficiaryName;

    private Long beneficiaryAccountNumber;

    private Beneficiary.RELATION bloodRelation;

    private int benAge;
    private Beneficiary.BanksSupported benBank;

    private Long balance;
    private AccountsDto.UpdateRequest updateRequest;
    private String branchCode;
    private Long transferLimitPerDay;
    private int creditScore;
    private Accounts.AccountStatus accountStatus;
    private Long approvedLoanLimitBasedOnCreditScore;
    private Boolean anyActiveLoans;
    private Long totLoanIssuedSoFar;
    private Long totalOutStandingAmountPayableToBank;
    private List<BeneficiaryDto> listOfBeneficiary = new ArrayList<>();
    private List<TransactionsDto> listOfTransactions = new ArrayList<>();
    private Customer customer;
    private List<Accounts> accounts;
    private Accounts account;
}
