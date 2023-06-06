package com.example.accountsservices.dto.outputDtos;

import com.example.accountsservices.dto.*;
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
    private List<AccountsDto> listOfAccounts;
    private List<BeneficiaryDto> beneficiaryList;
    private List<TransactionsDto> transactionsList;
}
