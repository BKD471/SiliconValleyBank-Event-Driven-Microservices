package com.example.accountsservices.service;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.CustomerDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BadApiRequestException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.service.impl.AccountsServiceImpl;
import com.example.accountsservices.service.impl.BeneficiaryServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.regex.Pattern;

import static com.example.accountsservices.helpers.RegexMatchersHelper.*;
import static com.example.accountsservices.helpers.RegexMatchersHelper.PATTERN_FOR_DRIVING_LICENSE;

public interface IValidationService {
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(IValidationService.class);
    default Boolean checkConflictingAccountUpdateConditionForBranch(Accounts accounts, AccountsDto accountsDto, String locality) throws AccountsException {
        log.debug("<----------------------------" +
                "checkConflictingAccountUpdateConditionForBranch(Accounts,AccountsDto,String) AccountsServiceImpl started---------------------------------------" +
                "----------------------------------------------------------------------------------------------------------------------->");
        String location = String.format("Inside checkConflictingAccountUpdateConditionForBranch(Accounts) in AccountsServiceImpl" +
                "coming from %s", locality);
        Accounts.Branch newhomeBranch ;
        newhomeBranch = (null == accountsDto) ? accounts.getHomeBranch() : accountsDto.getHomeBranch();
        Accounts.AccountType accountType = accounts.getAccountType();

        //get all accounts for customer
        List<Accounts> listOfAccounts = accounts.getCustomer().getAccounts();

        Accounts.Branch finalNewhomeBranch = newhomeBranch;
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

    Boolean accountsUpdateValidator(Accounts accounts, AccountsDto accountsDto, CustomerDto customerDto, AccountsServiceImpl.ValidateType request) throws AccountsException, BadApiRequestException ;
    void beneficiaryUpdateValidator(Accounts accounts, BeneficiaryDto beneficiaryDto, BeneficiaryServiceImpl.validateBenType type) throws BeneficiaryException ;
}
