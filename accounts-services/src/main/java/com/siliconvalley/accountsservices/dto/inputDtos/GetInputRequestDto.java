package com.siliconvalley.accountsservices.dto.inputDtos;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.Beneficiary;
import com.siliconvalley.accountsservices.model.Customer;
import com.siliconvalley.accountsservices.model.Transactions;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetInputRequestDto {
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
  private AllConstantHelpers.BenUpdateRequest benRequest;
  private String beneficiaryId;
  private String beneficiaryName;
  private String beneficiaryAccountNumber;
  private AllConstantHelpers.RELATION bloodRelation;
  private String ben_date_of_birthINYYYYMMDD;
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
  private AllConstantHelpers.AccountStatus accountStatus;
  private BigDecimal approvedLoanLimitBasedOnCreditScore;
  private Boolean anyActiveLoans;
  private BigDecimal totLoanIssuedSoFar;
  private BigDecimal totalOutStandingAmountPayableToBank;
  private String address;
  private String imageName;

  private int pageNumber;
  private int pageSize;
  private String sortBy;
  private AllConstantHelpers.DIRECTION sortDir;

  private List<Beneficiary> listOfBeneficiary = new ArrayList<>();
  private List<Transactions> listOfTransactions = new ArrayList<>();
  private Customer customer;
  private HttpServletResponse response;
  private List<Accounts> accounts;
  private Accounts account;
}
