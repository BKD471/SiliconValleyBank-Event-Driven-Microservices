package com.example.accountsservices.dto.inputDtos;

import com.example.accountsservices.dto.AbstractParentDto;
import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
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
public class PutInputRequestDto extends AbstractParentDto {
    private Long customerId;
    private String name;
    private int age;
    private Long accountNumber;
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

    private int pageNumber;
    private int pageSize;
    private String sortBy;
    private GetInputRequestDto.DIRECTION sortDir;

    private BeneficiaryDto.BenUpdateRequest benRequest;
    private Long beneficiaryId;
    private String beneficiaryName;
    private Long beneficiaryAccountNumber;
    private Beneficiary.RELATION bloodRelation;
    private String ben_date_of_birthInYYYYMMDD;
    private int benAge;
    private Beneficiary.BanksSupported benBank;
    private String benAdharNumber;
    private String benPhoneNumber;
    private String benPanNumber;
    private String benPassportNumber;
    private String beneficiaryEmail;
    private String benVoterId;
    private String benDrivingLicense;
    private Long balance;
    private AccountsDto.UpdateRequest updateRequest;
    private String branchCode;
    private Long transferLimitPerDay;
    private int creditScore;
    private String address;
    private String imageName;
    private MultipartFile customerImage;
    private Accounts.AccountStatus accountStatus;
    private Long approvedLoanLimitBasedOnCreditScore;
    private Boolean anyActiveLoans;
    private Long totLoanIssuedSoFar;
    private Long totalOutStandingAmountPayableToBank;
    private List<Beneficiary> listOfBeneficiary = new ArrayList<>();
    private List<Transactions> listOfTransactions = new ArrayList<>();
    private Customer customer;
    private List<Accounts> accounts;
    private Accounts account;
}
