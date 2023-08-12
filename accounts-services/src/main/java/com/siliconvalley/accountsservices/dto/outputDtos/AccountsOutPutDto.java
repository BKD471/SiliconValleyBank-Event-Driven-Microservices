package com.siliconvalley.accountsservices.dto.outputDtos;

import com.siliconvalley.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountsOutPutDto {
    private String accountNumber;
    private BigDecimal balance;
    private AllConstantHelpers.Branch homeBranch;
    private AllConstantHelpers.AccountType accountType;
    private AllConstantHelpers.AccountStatus accountStatus;
    private String branchCode;
    private BigDecimal transferLimitPerDay;
    private int creditScore;
    private BigDecimal approvedLoanLimitBasedOnCreditScore;
    private Boolean anyActiveLoans;
    private BigDecimal totalOutstandingAmountPayableToBank;
    private BigDecimal totLoanIssuedSoFar;
    private Set<BeneficiaryDto> listOfBeneficiary;
    private Set<TransactionsDto> listOfTransactions;
}
