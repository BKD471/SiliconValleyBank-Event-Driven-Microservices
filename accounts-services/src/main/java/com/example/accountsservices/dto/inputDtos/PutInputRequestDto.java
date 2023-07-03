package com.example.accountsservices.dto.inputDtos;

import com.example.accountsservices.helpers.AllConstantHelpers;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.model.Transactions;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PutInputRequestDto {
    private long customerId;
    private String name;
    private int age;
    private long accountNumber;
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
    private long beneficiaryId;
    private String beneficiaryName;
    private long beneficiaryAccountNumber;
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
    private long balance;
    private AllConstantHelpers.UpdateRequest updateRequest;
    private String branchCode;
    private long transferLimitPerDay;
    private int creditScore;
    private String address;
    private String imageName;
    private AllConstantHelpers.AccountStatus accountStatus;
    private long approvedLoanLimitBasedOnCreditScore;
    private Boolean anyActiveLoans;
    private long totLoanIssuedSoFar;
    private long totalOutStandingAmountPayableToBank;
    private MultipartFile customerImage;
    private List<Beneficiary> listOfBeneficiary = new ArrayList<>();
    private List<Transactions> listOfTransactions = new ArrayList<>();
    private Customer customer;
    private List<Accounts> accounts;
    private Accounts account;
}
