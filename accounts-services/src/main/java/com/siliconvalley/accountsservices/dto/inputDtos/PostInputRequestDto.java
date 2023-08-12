package com.siliconvalley.accountsservices.dto.inputDtos;

import com.siliconvalley.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.Customer;
import com.siliconvalley.accountsservices.validator.ValidAge;
import com.siliconvalley.accountsservices.helpers.RegexMatchersHelper;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostInputRequestDto {
    private String customerId;

    @Size(min=3,max = 60,message = "Name must be at least 3 and at most 60 chars long")
    private String name;
    private int age;
    private String accountNumber;

    @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_DOB,
            message = "Please provide DOB in YYYY-MM-DD format")
    @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_NOT_NULL_CHARS,message = "Please mention date")
    @ValidAge
    private String dateOfBirthInYYYYMMDD;

    private AllConstantHelpers.AccountType accountType;
    private AllConstantHelpers.Branch homeBranch;

    @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_EMAIL,
            message = "Invalid Email format")
    private String email;

    @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_PASSWORD,message = "Minimum eight characters," +
            " at least one uppercase" +
            " letter, one lowercase letter, " +
            "one number and " +
            "one special character")
    private String password;

    @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_PHONE_NUMBER
    ,message = "Invalid phone Number format")
    private String phoneNumber;

    @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_ADHAR
    ,message = "Invalid Adhar Number")
    private String adharNumber;

    @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_PAN_NUMBER,
    message = "Invalid Pan Number")
    private String panNumber;

    @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_VOTER,message = "Invalid voterId")
    private String voterId;

    @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_DRIVING_LICENSE,
     message = "Invalid Driving License")
    private String drivingLicense;

    @Pattern(regexp = RegexMatchersHelper.PATTERN_FOR_PASSPORT,message = "Invalid Passport Number")
    private String passportNumber;

    private AllConstantHelpers.BenUpdateRequest benRequest;
    private String beneficiaryId;

    @Size(min=3,max = 60,message = "Beneficiary Name must be at least 3 and at most 60 chars long")
    private String beneficiaryName;

    @Size(min=10,max = 1000,message = "Please provide address")
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
    private Set<Accounts> accounts;
    private Accounts account;
}
