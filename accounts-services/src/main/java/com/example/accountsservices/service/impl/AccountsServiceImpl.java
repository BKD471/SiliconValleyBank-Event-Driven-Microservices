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
public class AccountsServiceImpl implements AccountsService {
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

        if (!Objects.isNull(newDateOfBirth) && newDateOfBirth.equals(oldDateOfBirth))
            accounts.setDateOfBirth(newDateOfBirth);
        if (!Objects.isNull(newBranchAddress) && newBranchAddress.equals(oldBranchAddress))
            accounts.setBranchAddress(newBranchAddress);
        if (!Objects.isNull(newPhoneNumber) && newPhoneNumber.equals(oldPhoneNumber))
            accounts.setPhoneNumber(newPhoneNumber);
        if (!Objects.isNull(newAdharNumber) && newAdharNumber.equals(oldAdharNumber))
            accounts.setPhoneNumber(newAdharNumber);
        if (!Objects.isNull(newPanNumber) && newPanNumber.equals(oldPanNumber))
            accounts.setPanNumber(newPanNumber);
        if (!Objects.isNull(newVoterId) && newVoterId.equals(oldVoterId))
            accounts.setVoterId(newVoterId);
        if (!Objects.isNull(newDrivingLicense) && newDrivingLicense.equals(oldDrivingLicense))
            accounts.setDrivingLicense(newDrivingLicense);
        if (!Objects.isNull(newPassportNumber) && newPassportNumber.equals(oldPassportNumber))
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

    private Beneficiary setBeneficiaryAgeFromDOB(Beneficiary beneficiary){
        //initialize the age of beneficiaries
        int dobYear= beneficiary.getDate_Of_Birth().getYear();
        int now= LocalDate.now().getYear();
        beneficiary.setAge(now-dobYear);
        return beneficiary;
    }
    @Override
    public AccountsDto addBeneficiary(Long customerId, Long accountNumber, BeneficiaryDto beneficiaryDto) throws AccountsException {

        //Updating the beneficiary info & saving it
        Beneficiary beneficiaryAccount=AccountsMapper.mapToBeneficiary(beneficiaryDto);
        Beneficiary savedBeneficiaryAccount=beneficiaryRepository.save(beneficiaryAccount);
        Beneficiary processedBeneficiaryAccount=setBeneficiaryAgeFromDOB(savedBeneficiaryAccount);
        Beneficiary  savedAndProcessedBeneficiaryAccount=beneficiaryRepository.save(processedBeneficiaryAccount);

        //Get the account in which we add beneficiary
        Optional<Accounts> fetchedAccounts=Optional.ofNullable(accountsRepository.findByCustomerIdAndAccountNumber(customerId,accountNumber));
        if(fetchedAccounts.isEmpty()) throw  new AccountsException("No such accounts");

        //add the beneficiary to account
        List<Beneficiary> beneficiaries=new ArrayList<>();
        beneficiaries.add(savedAndProcessedBeneficiaryAccount);
        fetchedAccounts.get().setListOfBeneficiary(beneficiaries);
        return AccountsMapper.mapToAccountsDto(fetchedAccounts.get());
    }


    @Override
    public List<BeneficiaryDto> getAllBeneficiariesOfAnAccountByCustomerIdAndLoanNumber(Long customerId, Long accountNumber) throws BeneficiaryException {
        Optional<Accounts> fetchedAccount = Optional.ofNullable(accountsRepository.findByCustomerIdAndAccountNumber(customerId, accountNumber));
        if (fetchedAccount.isEmpty()) throw new BeneficiaryException("No beneficiaries present for this account");
        return fetchedAccount.get().getListOfBeneficiary().stream().map(AccountsMapper::mapToBeneficiaryDto).collect(Collectors.toList());
    }

    /**
     * @param customerId
     * @param BeneficiaryId
     * @param beneficiaryDto
     * @return
     */
    @Override
    public BeneficiaryDto updateBeneficiaryDetailsOfaCustomerByBeneficiaryId(Long customerId, Long accountNumber, Long BeneficiaryId, BeneficiaryDto beneficiaryDto) {
        return null;
    }

    /**
     * @param customerId
     * @param accountNumberRecipient
     * @param accountNumberSender
     * @returnType AccountsDto
     */
    @Override
    public AccountsDto creditMoney(Long customerId, Long accountNumberRecipient, Long accountNumberSender) {
        return null;
    }

    @Override
    public AccountsDto debitMoney(Long customerId, Long accountNumberSource,
                                  Long accountNumberDestination) {
        return null;
    }

    @Override
    public List<TransactionsDto> getAllTransactionsForAnAccount(Long customerId, Long accountNumber) {
        return null;
    }
}
