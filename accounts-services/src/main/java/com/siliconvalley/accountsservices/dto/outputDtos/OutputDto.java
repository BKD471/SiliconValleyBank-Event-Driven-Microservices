package com.siliconvalley.accountsservices.dto.outputDtos;

import com.siliconvalley.accountsservices.dto.baseDtos.AccountsDto;
import com.siliconvalley.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.dto.responseDtos.PageableResponseDto;
import lombok.*;

import java.util.List;
import java.util.Set;

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
    private Set<AccountsDto> listOfAccounts;
    private Set<BeneficiaryDto> beneficiaryList;
    private Set<TransactionsDto> transactionsList;
}
