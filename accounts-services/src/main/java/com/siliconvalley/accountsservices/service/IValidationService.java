package com.siliconvalley.accountsservices.service;

import com.siliconvalley.accountsservices.dto.baseDtos.AccountsDto;
import com.siliconvalley.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.siliconvalley.accountsservices.dto.baseDtos.CustomerDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.BadApiRequestException;
import com.siliconvalley.accountsservices.exception.BeneficiaryException;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.model.Accounts;

import java.util.List;

public interface IValidationService {
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(IValidationService.class);
    static Boolean checkConflictingAccountUpdateConditionForBranch(final Accounts accounts, final AccountsDto accountsDto, final String locality) throws AccountsException {
        log.debug("<----------------------------" +
                "checkConflictingAccountUpdateConditionForBranch(Accounts,AccountsDto,String) AccountsServiceImpl started---------------------------------------" +
                "----------------------------------------------------------------------------------------------------------------------->");
        final String location = String.format("Inside checkConflictingAccountUpdateConditionForBranch(Accounts) in AccountsServiceImpl" +
                "coming from %s", locality);
        final AllConstantHelpers.Branch newhomeBranch =(null == accountsDto) ? accounts.getHomeBranch() : accountsDto.getHomeBranch();
        final AllConstantHelpers.AccountType accountType = accounts.getAccountType();

        //get all accounts for customer
        final List<Accounts> listOfAccounts = accounts.getCustomer().getAccounts();
        final AllConstantHelpers.Branch finalNewhomeBranch = newhomeBranch;
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

    Boolean accountsUpdateValidator(final Accounts accounts, final AccountsDto accountsDto, final CustomerDto customerDto, final AllConstantHelpers.AccountsValidateType request) throws AccountsException, BadApiRequestException;
    void beneficiaryUpdateValidator(final Accounts accounts, final BeneficiaryDto beneficiaryDto, final AllConstantHelpers.validateBenType type) throws BeneficiaryException;
}
