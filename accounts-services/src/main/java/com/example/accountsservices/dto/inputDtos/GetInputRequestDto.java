package com.example.accountsservices.dto.inputDtos;

import com.example.accountsservices.dto.baseDtos.AccountsDto;
import com.example.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.example.accountsservices.helpers.AllEnumConstantHelpers;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.model.Transactions;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetInputRequestDto {
  private long customerId;
  private String name;
  private int age;
  private long accountNumber;
  private String dateOfBirthInYYYYMMDD;
  private AllEnumConstantHelpers.AccountType accountType;
  private AllEnumConstantHelpers.Branch homeBranch;
  private String email;
  private String password;
  private String phoneNumber;
  private String adharNumber;
  private String panNumber;
  private String voterId;
  private String drivingLicense;
  private String passportNumber;
  private AllEnumConstantHelpers.BenUpdateRequest benRequest;
  private long beneficiaryId;
  private String beneficiaryName;
  private long beneficiaryAccountNumber;
  private AllEnumConstantHelpers.RELATION bloodRelation;
  private String ben_date_of_birthINYYYYMMDD;
  private int benAge;
  private AllEnumConstantHelpers.BanksSupported benBank;
  private String benAdharNumber;
  private String benPhoneNumber;
  private String benPanNumber;
  private String benPassportNumber;
  private String beneficiaryEmail;
  private String benVoterId;
  private String benDrivingLicense;
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
  private String address;
  private String imageName;

  private int pageNumber;
  private int pageSize;
  private String sortBy;
  private AllEnumConstantHelpers.DIRECTION sortDir;

  private List<Beneficiary> listOfBeneficiary = new ArrayList<>();
  private List<Transactions> listOfTransactions = new ArrayList<>();
  private Customer customer;
  private HttpServletResponse response;
  private List<Accounts> accounts;
  private Accounts account;
}
