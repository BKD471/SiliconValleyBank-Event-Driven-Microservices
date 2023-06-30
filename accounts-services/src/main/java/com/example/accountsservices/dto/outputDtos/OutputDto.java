package com.example.accountsservices.dto.outputDtos;

import com.example.accountsservices.dto.baseDtos.AccountsDto;
import com.example.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.example.accountsservices.dto.baseDtos.TransactionsDto;
import com.example.accountsservices.dto.responseDtos.PageableResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutputDto {
    private String defaultMessage;
    private CustomerOutPutDto customer;
    private AccountsOutPutDto accounts;
    private BeneficiaryDto beneficiary;
    private TransactionsDto transactions;
    private PageableResponseDto<AccountsDto> accountsListPages;
    private PageableResponseDto<BeneficiaryDto> beneficiaryListPages;
    private List<AccountsDto> listOfAccounts;
    private List<BeneficiaryDto> beneficiaryList;
    private List<TransactionsDto> transactionsList;
}
