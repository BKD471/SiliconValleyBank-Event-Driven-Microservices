package com.siliconvalley.accountsservices.dto.inputDtos;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.Beneficiary;
import com.siliconvalley.accountsservices.model.Customer;
import com.siliconvalley.accountsservices.model.Transactions;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PutInputRequestDto {
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
    private Set<Beneficiary> listOfBeneficiary;
    private Set<Transactions> listOfTransactions;
    private Customer customer;
    private Set<Accounts> accounts;
    private Accounts account;
}
