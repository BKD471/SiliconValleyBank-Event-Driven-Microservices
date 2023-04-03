package com.example.accountsservices.service.impl;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.TransactionsDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.mapper.AccountsMapper;
import com.example.accountsservices.model.Accounts;

import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.BeneficiaryRepository;
import com.example.accountsservices.repository.TransactionsRepository;
import com.example.accountsservices.service.AbstractAccountsService;
import com.example.accountsservices.service.AccountsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @parent AccountsService
 * @class AccountsServiceImpl
 * @fields accountsRepository
 * @fieldTypes AccountsRepository
 * @overridenMethods createAccounts, getAccountByCustomerId,
 * updateAccountByCustomerIdAndAccountNumber,updateBeneficiaryDetails
 * @specializedMethods None
 */

@Service
public class AccountsServiceImpl extends AbstractAccountsService {
    private final AccountsRepository accountsRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final TransactionsRepository transactionsRepository;

    /**
     * @paramType AccountsRepository
     * @returnType NA
     */
    AccountsServiceImpl(AccountsRepository accountsRepository,
                        BeneficiaryRepository beneficiaryRepository,
                        TransactionsRepository transactionsRepository) {
        this.accountsRepository = accountsRepository;
        this.beneficiaryRepository = beneficiaryRepository;
        this.transactionsRepository = transactionsRepository;
    }


    private Accounts processAccountInformation(Accounts accounts) {
        //calculate & set customer age from Dob
        LocalDate Date_of_birth = accounts.getDateOfBirth();
        int age = LocalDate.now().getYear() - Date_of_birth.getYear();
        accounts.setCustomerAge(age);
        //set customer account opening balance
        accounts.setBalance(0L);
        return accounts;
    }

    /**
     * @paramType AccountsDto
     * @returnType AccountsDto
     */
    @Override
    public AccountsDto createAccounts(@RequestBody AccountsDto accountsDto) {
        Accounts account = AccountsMapper.mapToAccounts(accountsDto);
        Accounts savedAccounts = accountsRepository.save(account);
        Accounts processedAccount = processAccountInformation(savedAccounts);
        Accounts savedProcessedAccount = accountsRepository.save(processedAccount);
        return AccountsMapper.mapToAccountsDto(savedProcessedAccount);
    }

    /**
     * @param customerId accountNumber
     * @paramType Long
     * @returnType AccountsDto
     */
    @Override
    public AccountsDto getAccountInfoByCustomerIdAndAccountNumber(Long customerId, Long accountNumber) throws AccountsException {
        Optional<Accounts> fetchedAccount = Optional.ofNullable(accountsRepository.
                findByCustomerIdAndAccountNumber(customerId, accountNumber));
        if (fetchedAccount.isEmpty()) throw new AccountsException("No such accounts found");
        return AccountsMapper.mapToAccountsDto(fetchedAccount.get());
    }

    @Override
    public List<AccountsDto> getAllAccountsByCustomerId(Long customerId) throws AccountsException {
        Optional<List<Accounts>> allAccounts = Optional.ofNullable(accountsRepository.findAllByCustomerId(customerId));
        if (allAccounts.isEmpty()) throw new AccountsException("No such accounts present with this cust id");
        return allAccounts.get().stream().map(AccountsMapper::mapToAccountsDto).collect(Collectors.toList());
    }

    private Accounts processAccountUpdate(AccountsDto accountsDto, Accounts accounts) {
        LocalDate oldDateOfBirth = accounts.getDateOfBirth();
        LocalDate newDateOfBirth = accountsDto.getDateOfBirth();
        String oldBranchAddress = accounts.getBranchAddress();
        String newBranchAddress = accountsDto.getBranchAddress();
        String oldPhoneNumber = accounts.getPhoneNumber();
        String newPhoneNumber = accountsDto.getPhoneNumber();
        String oldAdharNumber = accounts.getAdharNumber();
        String newAdharNumber = accountsDto.getAdharNumber();
        String oldPanNumber = accounts.getPanNumber();
        String newPanNumber = accountsDto.getPanNumber();
        String oldVoterId = accounts.getVoterId();
        String newVoterId = accountsDto.getVoterId();
        String oldDrivingLicense = accounts.getDrivingLicense();
        String newDrivingLicense = accountsDto.getDrivingLicense();
        String oldPassportNumber = accounts.getPassportNumber();
        String newPassportNumber = accountsDto.getPassportNumber();

        if (null!=newDateOfBirth && !newDateOfBirth.equals(oldDateOfBirth))
            accounts.setDateOfBirth(newDateOfBirth);
        if (null!=newBranchAddress && !newBranchAddress.equalsIgnoreCase(oldBranchAddress))
            accounts.setBranchAddress(newBranchAddress);
        if (null!=newPhoneNumber && !newPhoneNumber.equalsIgnoreCase(oldPhoneNumber))
            accounts.setPhoneNumber(newPhoneNumber);
        if (null!=newAdharNumber && !newAdharNumber.equalsIgnoreCase(oldAdharNumber))
            accounts.setPhoneNumber(newAdharNumber);
        if (null!=newPanNumber && !newPanNumber.equalsIgnoreCase(oldPanNumber))
            accounts.setPanNumber(newPanNumber);
        if (null!=newVoterId && !newVoterId.equalsIgnoreCase(oldVoterId))
            accounts.setVoterId(newVoterId);
        if (null!=newDrivingLicense && !newDrivingLicense.equalsIgnoreCase(oldDrivingLicense))
            accounts.setDrivingLicense(newDrivingLicense);
        if (null!=newPassportNumber && !newPassportNumber.equalsIgnoreCase(oldPassportNumber))
            accounts.setPassportNumber(newPassportNumber);

        return accounts;
    }

    /**
     * @paramType Long, Long
     * @returnType AccountsDto
     */
    @Override
    public AccountsDto updateAccountByCustomerIdAndAccountNumber(Long customerId,
                                                                 Long accountNumber, AccountsDto accountsDto) throws AccountsException {
        Optional<Accounts> accounts = Optional.ofNullable(accountsRepository.findByCustomerIdAndAccountNumber(customerId, accountNumber));
        if (accounts.isEmpty()) throw new AccountsException("No such account");

        Accounts updatedAccount = processAccountUpdate(accountsDto, accounts.get());
        Accounts savedUpdatedAccount = accountsRepository.save(updatedAccount);
        return AccountsMapper.mapToAccountsDto(savedUpdatedAccount);
    }

    @Override
    public void deleteAccount(Long accountNumber){
        //...........
    }

}
