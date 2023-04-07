package com.example.accountsservices.service.impl;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.mapper.Mapper;
import com.example.accountsservices.model.Accounts;

import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.service.AbstractAccountsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;
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
    private  static final Accounts.AccountStatus STATUS_BLOCKED= Accounts.AccountStatus.BLOCKED;
    private  static final Accounts.AccountStatus STATUS_OPEN= Accounts.AccountStatus.OPEN;
    private  static final String REQUEST_TO_BLOCK="block";


    /**
     * @paramType AccountsRepository
     * @returnType NA
     */
    AccountsServiceImpl(AccountsRepository accountsRepository) {
        super(accountsRepository);
        this.accountsRepository = accountsRepository;
    }


    private Accounts processAccountInformation(Accounts accounts) {
        //calculate & set customer age from Dob
        LocalDate Date_of_birth = accounts.getDateOfBirth();
        int age = LocalDate.now().getYear() - Date_of_birth.getYear();
        accounts.setAge(age);
        //set customer account opening balance
        accounts.setBalance(0L);
        //set account status OPEN
        accounts.setAccountStatus(STATUS_OPEN);
        return accounts;
    }

    /**
     * @paramType AccountsDto
     * @returnType AccountsDto
     */
    @Override
    public AccountsDto createAccounts(@RequestBody AccountsDto accountsDto) {
        Accounts account = Mapper.mapToAccounts(accountsDto);
        Accounts savedAccounts = accountsRepository.save(account);
        Accounts processedAccount = processAccountInformation(savedAccounts);
        Accounts savedProcessedAccount = accountsRepository.save(processedAccount);
        return Mapper.mapToAccountsDto(savedProcessedAccount);
    }



    /**
     * @param customerId accountNumber
     * @paramType Long
     * @returnType AccountsDto
     */
    @Override
    public AccountsDto getAccountInfoByCustomerIdAndAccountNumber(Long customerId, Long accountNumber) throws AccountsException {
        Accounts foundAccount = fetchAccountByAccountNumber(accountNumber);
        return Mapper.mapToAccountsDto(foundAccount);
    }

    @Override
    public List<AccountsDto> getAllAccountsByCustomerId(Long customerId) throws AccountsException {
        Optional<List<Accounts>> allAccounts = Optional.ofNullable(accountsRepository.findAllByCustomerId(customerId));
        if (allAccounts.isEmpty()) throw new AccountsException(String.format("No such accounts present with this customer %s",customerId));
        return allAccounts.get().stream().filter(accounts -> !STATUS_BLOCKED.equals(accounts.getAccountStatus())).map(Mapper::mapToAccountsDto).collect(Collectors.toList());
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

        if (null != newDateOfBirth && !newDateOfBirth.equals(oldDateOfBirth))
            accounts.setDateOfBirth(newDateOfBirth);
        if (null != newBranchAddress && !newBranchAddress.equalsIgnoreCase(oldBranchAddress))
            accounts.setBranchAddress(newBranchAddress);
        if (null != newPhoneNumber && !newPhoneNumber.equalsIgnoreCase(oldPhoneNumber))
            accounts.setPhoneNumber(newPhoneNumber);
        if (null != newAdharNumber && !newAdharNumber.equalsIgnoreCase(oldAdharNumber))
            accounts.setPhoneNumber(newAdharNumber);
        if (null != newPanNumber && !newPanNumber.equalsIgnoreCase(oldPanNumber))
            accounts.setPanNumber(newPanNumber);
        if (null != newVoterId && !newVoterId.equalsIgnoreCase(oldVoterId))
            accounts.setVoterId(newVoterId);
        if (null != newDrivingLicense && !newDrivingLicense.equalsIgnoreCase(oldDrivingLicense))
            accounts.setDrivingLicense(newDrivingLicense);
        if (null != newPassportNumber && !newPassportNumber.equalsIgnoreCase(oldPassportNumber))
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
        //get the account
        Accounts foundAccount = fetchAccountByAccountNumber(accountNumber);
        //update
        Accounts updatedAccount = processAccountUpdate(accountsDto, foundAccount);
        Accounts savedUpdatedAccount = accountsRepository.save(updatedAccount);
        return Mapper.mapToAccountsDto(savedUpdatedAccount);
    }

    @Override
    public void deleteAccount(Long accountNumber) throws AccountsException {
        //checking whether account exist or not
        fetchAccountByAccountNumber(accountNumber);
        //deleting it
        accountsRepository.deleteByAccountNumber(accountNumber);
    }

    @Override
    public void deleteAllAccountsByCustomer(Long customerId) throws AccountsException {
        //checking whether customer exist
        Optional<List<Accounts>> foundCustomer = Optional.ofNullable(accountsRepository.findAllByCustomerId(customerId));
        if (foundCustomer.isEmpty())
            throw new AccountsException(String.format("No such customer with id %s", customerId));
        //deleting it
        accountsRepository.deleteAllByCustomerId(customerId);
    }

    @Override
    public  void blockAccount(Long accountNumber) throws  AccountsException{
        //Get account
        Accounts foundAccount=fetchAccountByAccountNumber(accountNumber,REQUEST_TO_BLOCK);
        //Block it
        foundAccount.setAccountStatus(STATUS_BLOCKED);
    }

}
