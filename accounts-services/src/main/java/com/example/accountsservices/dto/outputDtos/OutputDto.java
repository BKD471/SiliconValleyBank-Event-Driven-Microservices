package com.example.accountsservices.dto.outputDtos;

import com.example.accountsservices.dto.baseDtos.AccountsDto;
import com.example.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.example.accountsservices.dto.baseDtos.TransactionsDto;
import com.example.accountsservices.dto.responseDtos.PageableResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class OutputDto {
    private final String defaultMessage;
    private final CustomerOutPutDto customer;
    private final AccountsOutPutDto accounts;
    private final BeneficiaryDto beneficiary;
    private final TransactionsDto transactions;
    private final PageableResponseDto<AccountsDto> accountsListPages;
    private final PageableResponseDto<BeneficiaryDto> beneficiaryListPages;
    private final List<AccountsDto> listOfAccounts;
    private final List<BeneficiaryDto> beneficiaryList;
    private final List<TransactionsDto> transactionsList;
}
