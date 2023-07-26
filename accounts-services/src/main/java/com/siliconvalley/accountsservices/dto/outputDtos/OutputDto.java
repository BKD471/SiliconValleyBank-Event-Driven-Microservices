package com.siliconvalley.accountsservices.dto.outputDtos;

import com.siliconvalley.accountsservices.dto.baseDtos.AccountsDto;
import com.siliconvalley.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.dto.responseDtos.PageableResponseDto;
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
