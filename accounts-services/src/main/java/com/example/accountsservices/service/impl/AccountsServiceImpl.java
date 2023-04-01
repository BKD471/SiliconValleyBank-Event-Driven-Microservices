package com.example.accountsservices.service.impl;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.TransactionsDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.mapper.AccountsMapper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Transactions;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.service.AccountsService;
import jakarta.persistence.*;
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
    private AccountsRepository accountsRepository;

    /**
     * @param accountsRepository
     * @paramType AccountsRepository
     * @returnType NA
     */
    AccountsServiceImpl(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }


    private Accounts processAccountInformation(Accounts accounts){
        //calculate & set customer age from Dob
        LocalDate Date_of_birth=accounts.getDateOfBirth();
        int age=LocalDate.now().getYear()-Date_of_birth.getYear();
        accounts.setCustomerAge(age);
        //set customer account opening balance
        accounts.setBalance(0l);
        return accounts;
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
        Accounts processedAccount=processAccountInformation(savedAccounts);
        Accounts savedProcessedAccount=accountsRepository.save(processedAccount);
        return AccountsMapper.mapToAccountsDto(savedProcessedAccount);
    }

    /**
     * @param customerId accountNumber
     * @paramType Long
     * @returnType AccountsDto
     */
    @Override
    public AccountsDto getAccountInfoByCustomerIdAndAccountNumber(Long customerId, Long accountNumber) {
        Optional<Accounts> fetchedAccount = Optional.ofNullable(accountsRepository.
                findByCustomerIdAndAccountNumber(customerId, accountNumber));
        return AccountsMapper.mapToAccountsDto(fetchedAccount.get());
    }

    @Override
    public  List<AccountsDto> getAllAccountsByCustomerId(Long customerId){
       List<Accounts> allAccounts=accountsRepository.findAllByCustomerId(customerId);
       return allAccounts.stream().map(accounts -> AccountsMapper.mapToAccountsDto(accounts)).collect(Collectors.toList());
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
     * @param customerId,accountNumber
     * @paramType Long, Long
     * @returnType AccountsDto
     */
    @Override
    public AccountsDto updateAccountByCustomerIdAndAccountNumber(Long customerId,
                                                                 Long accountNumber, AccountsDto accountsDto) throws AccountsException {
        Accounts accounts = accountsRepository.findByCustomerIdAndAccountNumber(customerId, accountNumber);
        if (Objects.isNull(accounts)) throw new AccountsException("to be worked on");

        Accounts updatedAccount = processAccountUpdate(accountsDto, accounts);
        Accounts savedUpdatedAccount = accountsRepository.save(updatedAccount);
        return AccountsMapper.mapToAccountsDto(savedUpdatedAccount);
    }

    @Override
    public  BeneficiaryDto addBeneficiary(Long customerId,Long accountNumber,BeneficiaryDto beneficiaryDto){
        return null;
    }


    @Override
    public List<BeneficiaryDto> getAllBeneficiariesOfAnAccountByCustomerIdAndLoanNumber(Long customerId,Long accountNumber){
        Accounts fetchedAccount=accountsRepository.findByCustomerIdAndAccountNumber(customerId, accountNumber);
        return fetchedAccount.getListOfBeneficiary().stream().map(beneficiary ->AccountsMapper.mapToBeneficiaryDto(beneficiary)).collect(Collectors.toList());
    }
    /**
     * @param customerId
     * @param BeneficiaryId
     * @param beneficiaryDto
     * @return
     */
    @Override
    public BeneficiaryDto updateBeneficiaryDetailsOfaCustomerByBeneficiaryId(Long customerId,Long accountNumber,Long BeneficiaryId, BeneficiaryDto beneficiaryDto) {
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
    public List<TransactionsDto> getAllTransactionsForAnAccount(Long customerId,Long accountNumber){
        return null;
    }
}
