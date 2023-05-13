package com.example.accountsservices.dto;

import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.model.Transactions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputDto {
    private Long customerId;

    @Size(min=3,max = 60,message = "Name must be at least 3 and at most 60 chars long")
    private String name;

    private int age;


    private Long accountNumber;

    @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$",
            message = "Please provide DOB in YYYY-MM-DD format")
    private String dateOfBirthInYYYYMMDD;

    private Accounts.AccountType accountType;
    private Accounts.Branch homeBranch;

    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$",
            message = "Invalid Email format")
    private String email;

    @Pattern(regexp = "^(\\+\\d{1,2}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$"
    ,message = "Invalid phone Number format")
    private String phoneNumber;

    @Pattern(regexp = "(^[0-9]{4}[0-9]{4}[0-9]{4}$)|(^[0-9]{4}\\s[0-9]{4}\\s[0-9]{4}$)|(^[0-9]{4}-[0-9]{4}-[0-9]{4}$)"
    ,message = "Invalid Aadhar Number")
    private String adharNumber;

    @Pattern(regexp = "^([a-zA-Z]){5}([0-9]){4}([a-zA-Z]){1}?$",
    message = "Invalid Pan Number")
    private String panNumber;

    @Pattern(regexp = "^[A-Z]{3}[0-9]{7}$",message = "Invalid voterId")
    private String voterId;

    @Pattern(regexp = "(([A-Z]{2}[0-9]{2})( )|([A-Z]{2}-[0-9]{2}))((19|20)[0-9][0-9])[0-9]{7}$",
     message = "Invalid Driving License")
    private String drivingLicense;

    @Pattern(regexp = "^[A-PR-WYa-pr-wy][1-9]\\d\\s?\\d{4}[1-9]$",message = "Invalid Passport Number")
    private String passportNumber;

    private BeneficiaryDto.BenUpdateRequest benRequest;
    private Long beneficiaryId;

    @Size(min=3,max = 60,message = "Beneficiary Name must be at least 3 and at most 60 chars long")
    private String beneficiaryName;

    private Long beneficiaryAccountNumber;

    private Beneficiary.RELATION bloodRelation;

    @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$",
            message = "Please provide Beneficiary DOB in YYYY-MM-DD format")
    private String ben_date_of_birthINYYYYMMDD;
    private int benAge;
    private Beneficiary.BanksSupported benBank;

    @Pattern(regexp = "(^[0-9]{4}[0-9]{4}[0-9]{4}$)|(^[0-9]{4}\\s[0-9]{4}\\s[0-9]{4}$)|(^[0-9]{4}-[0-9]{4}-[0-9]{4}$)"
            ,message = "Invalid Aadhar Number")
    private String benAdharNumber;

    @Pattern(regexp = "^(\\+\\d{1,2}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$"
            ,message = "Invalid phone Number format")
    private String benPhoneNumber;

    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}",
            message = "Invalid Pan Number")
    private String benPanNumber;

    @Pattern(regexp = "^[A-PR-WYa-pr-wy][1-9]\\\\d\\\\s?\\\\d{4}[1-9]$",message = "Invalid Passport Number")
    private String benPassportNumber;

    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$",
            message = "Invalid Beneficiary Email format")
    private String beneficiaryEmail;

    @Pattern(regexp = "^[A-Z]{3}[0-9]{7}$",message = "Invalid voterId")
    private String benVoterId;

    @Pattern(regexp = "^(([A-Z]{2}[0-9]{2})( )|([A-Z]{2}-[0-9]{2}))((19|20)[0-9][0-9])[0-9]{7}$",
            message = "Invalid Driving License")
    private String benDrivingLicense;

    //    @JsonIgnore

    //   @JsonIgnore
    private Long balance;
    // @JsonIgnore
    private AccountsDto.UpdateRequest updateRequest;
    private String branchCode;
    //    @JsonIgnore
    private Long transferLimitPerDay;
    //   @JsonIgnore
    private int creditScore;
    //    @JsonIgnore
    private Accounts.AccountStatus accountStatus;
    private Long approvedLoanLimitBasedOnCreditScore;
    private Boolean anyActiveLoans;
    private Long totLoanIssuedSoFar;
    private Long totalOutStandingAmountPayableToBank;
    //   @JsonIgnore
    private List<Beneficiary> listOfBeneficiary = new ArrayList<>();
    //   @JsonIgnore
    private List<Transactions> listOfTransactions = new ArrayList<>();
    private Customer customer;
    private List<Accounts> accounts;
    private Accounts account;
}
