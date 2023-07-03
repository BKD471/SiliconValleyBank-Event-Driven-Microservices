package com.example.accountsservices.service;

import com.example.accountsservices.dto.baseDtos.AccountsDto;
import com.example.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.example.accountsservices.dto.baseDtos.CustomerDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BadApiRequestException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.helpers.AllConstantHelpers;
import com.example.accountsservices.model.Accounts;
import java.util.List;

public interface IValidationService {
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(IValidationService.class);
    default Boolean checkConflictingAccountUpdateConditionForBranch(Accounts accounts, AccountsDto accountsDto, String locality) throws AccountsException {
        log.debug("<----------------------------" +
                "checkConflictingAccountUpdateConditionForBranch(Accounts,AccountsDto,String) AccountsServiceImpl started---------------------------------------" +
                "----------------------------------------------------------------------------------------------------------------------->");
        String location = String.format("Inside checkConflictingAccountUpdateConditionForBranch(Accounts) in AccountsServiceImpl" +
                "coming from %s", locality);
        AllConstantHelpers.Branch newhomeBranch ;
        newhomeBranch = (null == accountsDto) ? accounts.getHomeBranch() : accountsDto.getHomeBranch();
        AllConstantHelpers.AccountType accountType = accounts.getAccountType();

        //get all accounts for customer
        List<Accounts> listOfAccounts = accounts.getCustomer().getAccounts();

        AllConstantHelpers.Branch finalNewhomeBranch = newhomeBranch;
        boolean isNotPermissible = listOfAccounts.stream().
                anyMatch(account -> finalNewhomeBranch.equals(account.getHomeBranch())
                        && accountType.equals(account.getAccountType()));

        if (isNotPermissible) throw new AccountsException(AccountsException.class,
                String.format("You already have an account with same accountType %s" +
                                "and same HomeBranch %s",
                        accounts.getAccountType(), accounts.getHomeBranch()), location);
        log.debug("<----------checkConflictingAccountUpdateConditionForBranch(Accounts, AccountsDto,String) AccountsServiceImpl ended--" +
                "-------------------------------------------------------------------------------------------------------------------------->");
        return true;
    }

    Boolean accountsUpdateValidator(Accounts accounts, AccountsDto accountsDto, CustomerDto customerDto, AllConstantHelpers.AccountsValidateType request) throws AccountsException, BadApiRequestException ;
    void beneficiaryUpdateValidator(Accounts accounts, BeneficiaryDto beneficiaryDto, AllConstantHelpers.validateBenType type) throws BeneficiaryException ;
}
