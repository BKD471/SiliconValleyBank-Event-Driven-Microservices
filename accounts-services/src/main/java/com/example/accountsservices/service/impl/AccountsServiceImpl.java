package com.example.accountsservices.service.impl;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.mapper.AccountsMapper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.service.AccountsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Optional;

/**
 * @parent AccountsService
 * @class AccountsServiceImpl
 * @fields accountsRepository
 * @fieldTypes AccountsRepository
 * @overridenMethods  createAccounts,getAccountByCustomerId,
 * updateAccountByCustomerIdAndAccountNumber,updateBeneficiaryDetails
 *@specializedMethods None
 * */

@Service
public class AccountsServiceImpl implements AccountsService {
    private AccountsRepository accountsRepository;

    /**
     * @param accountsRepository
     * @paramType AccountsRepository
     * @returnType NA
     * */
    AccountsServiceImpl(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    /**
     * @param accountsDto
     * @paramType AccountsDto
     * @returnType AccountsDto
     */
    @Override
    public AccountsDto createAccounts(@RequestBody AccountsDto accountsDto) {
        Accounts account = AccountsMapper.mapToAccounts(accountsDto);
        Accounts savedAccounts = accountsRepository.save(account);
        return AccountsMapper.mapToAccountsDto(savedAccounts);
    }

    /**
     * @param id
     * @paramType Long
     * @returnType AccountsDto
     */
    @Override
    public AccountsDto getAccountByCustomerId(Long id) {
        Optional<Accounts> fetchedAccount = Optional.ofNullable(accountsRepository.findByCustomerId(id));
        return AccountsMapper.mapToAccountsDto(fetchedAccount.get());
    }

    /**
     * @param  customerId,accountNumber
     * @paramType Long,Long
     * @returnType AccountsDto
     * */
    @Override
    public AccountsDto updateAccountByCustomerIdAndAccountNumber(Long customerId,Long accountNumber) {
        return null;
    }

    /**
     * @param  customerId
     * @paramType Long
     * @ReturnType AccountsDto
     * */
    @Override
    public  AccountsDto updateBeneficiaryDetails(Long customerId){
        return null;
    }
}
