package com.siliconvalley.accountsservices.dto.outputDtos;

import com.siliconvalley.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountsOutPutDto {
    private String accountNumber;
    private long balance;
    private AllConstantHelpers.Branch homeBranch;
    private AllConstantHelpers.AccountType accountType;
    private AllConstantHelpers.AccountStatus accountStatus;
    private String branchCode;
    private long transferLimitPerDay;
    private int creditScore;
    private long approvedLoanLimitBasedOnCreditScore;
    private Boolean anyActiveLoans;
    private long totalOutstandingAmountPayableToBank;
    private long totLoanIssuedSoFar;
    private List<BeneficiaryDto> listOfBeneficiary;
    private List<TransactionsDto> listOfTransactions;
}
