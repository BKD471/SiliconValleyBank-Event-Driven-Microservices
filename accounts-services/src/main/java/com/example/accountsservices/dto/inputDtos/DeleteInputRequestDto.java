package com.example.accountsservices.dto.inputDtos;

import com.example.accountsservices.helpers.AllConstantHelpers;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.model.Transactions;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class DeleteInputRequestDto {
    private final String customerId;
    private final String name;
    private final int age;
    private final String accountNumber;
    private final String dateOfBirthInYYYYMMDD;
    private final AllConstantHelpers.AccountType accountType;
    private final AllConstantHelpers.Branch homeBranch;
    private final String email;
    private final String password;
    private final String phoneNumber;
    private final String adharNumber;
    private final String panNumber;
    private final String voterId;
    private final String drivingLicense;
    private final String passportNumber;
    private final AllConstantHelpers.DeleteRequest deleteRequest;
    private final AllConstantHelpers.BenUpdateRequest benRequest;
    private final String beneficiaryId;
    private final String beneficiaryName;
    private final String beneficiaryAccountNumber;
    private final AllConstantHelpers.RELATION bloodRelation;
    private final String ben_date_of_birthINYYYYMMDD;
    private final int benAge;
    private final AllConstantHelpers.BanksSupported benBank;
    private final String benAdharNumber;
    private final String benPhoneNumber;
    private final String benPanNumber;
    private final String benPassportNumber;
    private final String beneficiaryEmail;
    private final String benVoterId;
    private final String benDrivingLicense;
    private final long balance;
    private final AllConstantHelpers.UpdateRequest updateRequest;
    private final String branchCode;
    private final long transferLimitPerDay;
    private final int creditScore;
    private final AllConstantHelpers.AccountStatus accountStatus;
    private final long approvedLoanLimitBasedOnCreditScore;
    private final Boolean anyActiveLoans;
    private final long totLoanIssuedSoFar;
    private final long totalOutStandingAmountPayableToBank;
    private final String address;
    private final String imageName;
    private final List<Beneficiary> listOfBeneficiary = new ArrayList<>();
    private final List<Transactions> listOfTransactions = new ArrayList<>();
    private final Customer customer;
    private final List<Accounts> accounts;
    private final Accounts account;
}
