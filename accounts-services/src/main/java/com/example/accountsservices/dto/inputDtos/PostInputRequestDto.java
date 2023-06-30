package com.example.accountsservices.dto.inputDtos;

import com.example.accountsservices.dto.baseDtos.AccountsDto;
import com.example.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.example.accountsservices.dto.baseDtos.TransactionsDto;
import com.example.accountsservices.helpers.AllEnumConstantHelpers;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostInputRequestDto {
    private long customerId;

    @Size(min=3,max = 60,message = "Name must be at least 3 and at most 60 chars long")
    private String name;
    private int age;
    private long accountNumber;

    @Pattern(regexp = PATTERN_FOR_DOB,
            message = "Please provide DOB in YYYY-MM-DD format")
    @Pattern(regexp = PATTERN_FOR_NOT_NULL_CHARS,message = "Please mention date")
    @ValidAge
    private String dateOfBirthInYYYYMMDD;

    private AllEnumConstantHelpers.AccountType accountType;
    private AllEnumConstantHelpers.Branch homeBranch;

    @Pattern(regexp = PATTERN_FOR_EMAIL,
            message = "Invalid Email format")
    private String email;

    @Pattern(regexp = PATTERN_FOR_PASSWORD,message = "Minimum eight characters," +
            " at least one uppercase" +
            " letter, one lowercase letter, " +
            "one number and " +
            "one special character")
    private String password;

    @Pattern(regexp = PATTERN_FOR_PHONE_NUMBER
    ,message = "Invalid phone Number format")
    private String phoneNumber;

    @Pattern(regexp = PATTERN_FOR_ADHAR
    ,message = "Invalid Adhar Number")
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

    private AllEnumConstantHelpers.BenUpdateRequest benRequest;
    private long beneficiaryId;

    @Size(min=3,max = 60,message = "Beneficiary Name must be at least 3 and at most 60 chars long")
    private String beneficiaryName;

    @Size(min=10,max = 1000,message = "Please provide address")
    private String address;
    private String imageName;
    private long beneficiaryAccountNumber;
    private Beneficiary.RELATION bloodRelation;
    private int benAge;
    private AllEnumConstantHelpers.BanksSupported benBank;
    private long balance;
    private AllEnumConstantHelpers.UpdateRequest updateRequest;
    private String branchCode;
    private long transferLimitPerDay;
    private int creditScore;
    private AllEnumConstantHelpers.AccountStatus accountStatus;
    private long approvedLoanLimitBasedOnCreditScore;
    private Boolean anyActiveLoans;
    private long totLoanIssuedSoFar;
    private long totalOutStandingAmountPayableToBank;
    private MultipartFile customerImage;
    private List<BeneficiaryDto> listOfBeneficiary = new ArrayList<>();
    private List<TransactionsDto> listOfTransactions = new ArrayList<>();
    private Customer customer;
    private List<Accounts> accounts;
    private Accounts account;
}
