package com.siliconvalley.accountsservices.service;

import com.siliconvalley.accountsservices.dto.baseDtos.AccountsDto;
import com.siliconvalley.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.siliconvalley.accountsservices.dto.baseDtos.CustomerDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.BadApiRequestException;
import com.siliconvalley.accountsservices.exception.BeneficiaryException;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.helpers.MapperHelper;
import com.siliconvalley.accountsservices.model.Accounts;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static com.siliconvalley.accountsservices.helpers.MapperHelper.mapToAccountsDto;

public interface IValidationService {
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(IValidationService.class);
    static void checkConflictingAccountUpdateConditionForBranch(final Accounts accounts, final String locality) throws AccountsException {
        log.debug("<----------------------------" +
                "checkConflictingAccountUpdateConditionForBranch(Accounts,AccountsDto,String) AccountsServiceImpl started---------------------------------------" +
                "----------------------------------------------------------------------------------------------------------------------->");
        AccountsDto accountsDto= mapToAccountsDto(accounts);
        final String location = String.format("Inside checkConflictingAccountUpdateConditionForBranch(Accounts) in AccountsServiceImpl" +
                "coming from %s", locality);
        final AllConstantHelpers.Branch newhomeBranch =(null == accountsDto) ? accounts.getHomeBranch() : accountsDto.getHomeBranch();
        final AllConstantHelpers.AccountType accountType = accounts.getAccountType();

        //get all accounts for customer
        final Set<Accounts> listOfAccounts = accounts.getCustomer().getAccounts();
        final AllConstantHelpers.Branch finalNewhomeBranch = newhomeBranch;

        Predicate<Accounts> checkConflictingHomeBranch= acc->finalNewhomeBranch.equals(acc.getHomeBranch())
                && accountType.equals(acc.getAccountType());
        boolean isNotPermissible = listOfAccounts.stream().anyMatch(checkConflictingHomeBranch);

        if (isNotPermissible) throw new AccountsException(AccountsException.class,
                String.format("You already have an account with same accountType %s" +
                                "and same HomeBranch %s",
                        accounts.getAccountType(), accounts.getHomeBranch()), location);
        log.debug("<----------checkConflictingAccountUpdateConditionForBranch(Accounts, AccountsDto,String) AccountsServiceImpl ended--" +
                "-------------------------------------------------------------------------------------------------------------------------->");
    }

    void accountsUpdateValidator(final Accounts accounts, final CustomerDto customerDto, final AllConstantHelpers.AccountsValidateType request) throws AccountsException, BadApiRequestException;
    void beneficiaryUpdateValidator(final Accounts accounts, final BeneficiaryDto beneficiaryDto, final AllConstantHelpers.validateBenType type) throws BeneficiaryException;
    void transactionsUpdateValidator(final Accounts accounts, final TransactionsDto transactionsDto,final AllConstantHelpers.ValidateTransactionType type);
}
