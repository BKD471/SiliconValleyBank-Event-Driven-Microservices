package com.example.accountsservices.dto.inputDtos;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.model.Transactions;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteInputRequestDto {
    private long customerId;
    private String name;
    private int age;
    private long accountNumber;
    private String dateOfBirthInYYYYMMDD;
    private Accounts.AccountType accountType;
    private Accounts.Branch homeBranch;
    private String email;
    private String password;
    private String phoneNumber;
    private String adharNumber;
    private String panNumber;
    private String voterId;
    private String drivingLicense;
    private String passportNumber;
    private DeleteRequest deleteRequest;
    public enum DeleteRequest{
        DELETE_CUSTOMER
    }
    private BeneficiaryDto.BenUpdateRequest benRequest;

    private long beneficiaryId;
    private String beneficiaryName;
    private long beneficiaryAccountNumber;
    private Beneficiary.RELATION bloodRelation;
    private String ben_date_of_birthINYYYYMMDD;
    private int benAge;
    private Beneficiary.BanksSupported benBank;
    private String benAdharNumber;
    private String benPhoneNumber;
    private String benPanNumber;
    private String benPassportNumber;
    private String beneficiaryEmail;
    private String benVoterId;
    private String benDrivingLicense;
    private long balance;
    private AccountsDto.UpdateRequest updateRequest;
    private String branchCode;
    private long transferLimitPerDay;
    private int creditScore;
    private Accounts.AccountStatus accountStatus;
    private long approvedLoanLimitBasedOnCreditScore;
    private Boolean anyActiveLoans;
    private long totLoanIssuedSoFar;
    private long totalOutStandingAmountPayableToBank;
    private String address;
    private String imageName;
    private List<Beneficiary> listOfBeneficiary = new ArrayList<>();
    private List<Transactions> listOfTransactions = new ArrayList<>();
    private Customer customer;
    private List<Accounts> accounts;
    private Accounts account;
}
