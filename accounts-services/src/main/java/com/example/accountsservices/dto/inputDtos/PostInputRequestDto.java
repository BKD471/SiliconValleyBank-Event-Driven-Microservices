package com.example.accountsservices.dto.inputDtos;

import com.example.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.example.accountsservices.dto.baseDtos.TransactionsDto;
import com.example.accountsservices.helpers.AllConstantHelpers;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.validator.ValidAge;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.example.accountsservices.helpers.RegexMatchersHelper.*;


@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class PostInputRequestDto {
    private final String customerId;

    @Size(min=3,max = 60,message = "Name must be at least 3 and at most 60 chars long")
    private final String name;
    private final int age;
    private final String accountNumber;

    @Pattern(regexp = PATTERN_FOR_DOB,
            message = "Please provide DOB in YYYY-MM-DD format")
    @Pattern(regexp = PATTERN_FOR_NOT_NULL_CHARS,message = "Please mention date")
    @ValidAge
    private final String dateOfBirthInYYYYMMDD;

    private final AllConstantHelpers.AccountType accountType;
    private final AllConstantHelpers.Branch homeBranch;

    @Pattern(regexp = PATTERN_FOR_EMAIL,
            message = "Invalid Email format")
    private final String email;

    @Pattern(regexp = PATTERN_FOR_PASSWORD,message = "Minimum eight characters," +
            " at least one uppercase" +
            " letter, one lowercase letter, " +
            "one number and " +
            "one special character")
    private final String password;

    @Pattern(regexp = PATTERN_FOR_PHONE_NUMBER
    ,message = "Invalid phone Number format")
    private final String phoneNumber;

    @Pattern(regexp = PATTERN_FOR_ADHAR
    ,message = "Invalid Adhar Number")
    private final String adharNumber;

    @Pattern(regexp = PATTERN_FOR_PAN_NUMBER,
    message = "Invalid Pan Number")
    private final String panNumber;

    @Pattern(regexp = PATTERN_FOR_VOTER,message = "Invalid voterId")
    private final String voterId;

    @Pattern(regexp = PATTERN_FOR_DRIVING_LICENSE,
     message = "Invalid Driving License")
    private final String drivingLicense;

    @Pattern(regexp = PATTERN_FOR_PASSPORT,message = "Invalid Passport Number")
    private final String passportNumber;

    private final AllConstantHelpers.BenUpdateRequest benRequest;
    private final String beneficiaryId;

    @Size(min=3,max = 60,message = "Beneficiary Name must be at least 3 and at most 60 chars long")
    private final String beneficiaryName;

    @Size(min=10,max = 1000,message = "Please provide address")
    private final String address;
    private final String imageName;
    private final String beneficiaryAccountNumber;
    private final AllConstantHelpers.RELATION bloodRelation;
    private final int benAge;
    private final AllConstantHelpers.BanksSupported benBank;
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
    private final MultipartFile customerImage;
    private final List<BeneficiaryDto> listOfBeneficiary = new ArrayList<>();
    private final List<TransactionsDto> listOfTransactions = new ArrayList<>();
    private final Customer customer;
    private final List<Accounts> accounts;
    private final Accounts account;
}
