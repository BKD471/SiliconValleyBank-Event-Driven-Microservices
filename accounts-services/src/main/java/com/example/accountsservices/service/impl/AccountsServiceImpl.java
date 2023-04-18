package com.example.accountsservices.service.impl;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.InputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.mapper.Mapper;
import com.example.accountsservices.model.Accounts;

import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.CustomerRepository;
import com.example.accountsservices.service.AbstractAccountsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final CustomerRepository customerRepository;
    private final Accounts.AccountStatus STATUS_BLOCKED = Accounts.AccountStatus.BLOCKED;
    private final Accounts.AccountStatus STATUS_OPEN = Accounts.AccountStatus.OPEN;
    private final String REQUEST_TO_BLOCK = "block";
    private final String UPDATE_CASH_LIMIT="update_cash_limit";
    private final String GENERATE_CREDIT_SCORE="gen_credit_score";


    /**
     * @paramType AccountsRepository
     * @returnType NA
     */
    public AccountsServiceImpl(AccountsRepository accountsRepository,CustomerRepository customerRepository) {
        super(accountsRepository);
        this.accountsRepository = accountsRepository;
        this.customerRepository=customerRepository;
    }


    private Accounts processAccountInformation(Accounts accounts) {
        //calculate & set customer age from Dob
        LocalDate Date_of_birth = accounts.getCustomer().getDateOfBirth();
        int age = LocalDate.now().getYear() - Date_of_birth.getYear();
        accounts.getCustomer().setAge(age);
        //set customer account opening balance
        accounts.setBalance(0L);
        //set account status OPEN
        accounts.setAccountStatus(STATUS_OPEN);
        //set cash limit
        accounts.setCashLimitPerDay(25000L);
        return accounts;
    }

    /**
     * @paramType AccountsDto
     * @returnType AccountsDto
     */
    @Override
    public InputDto createAccounts(@RequestBody InputDto inputDto) {
        InputDto a=inputDto;
        String[] date=inputDto.getDateOfBirthInYMD().split("-");
        LocalDate b=LocalDate.of(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]));
        InputDto c=a;
        // Accounts account = Mapper.mapToAccounts(accountsDto);
//        customerRepository.save(account.getCustomer());
//        Accounts savedAccounts = accountsRepository.save(account);
//        Accounts processedAccount = processAccountInformation(savedAccounts);
//        Accounts savedProcessedAccount = accountsRepository.save(processedAccount);
//        customerRepository.save(savedProcessedAccount.getCustomer());
//        return Mapper.mapToAccountsDto(savedProcessedAccount);

        return null;
    }


    /**
     * @param accountNumber accountNumber
     * @paramType Long
     * @returnType AccountsDto
     */
    @Override
    public AccountsDto getAccountInfo(Long accountNumber) throws AccountsException {
        Accounts foundAccount = fetchAccountByAccountNumber(accountNumber);
        return Mapper.mapToAccountsDto(foundAccount);
    }

    @Override
    public List<AccountsDto> getAllActiveAccountsByCustomerId(Long customerId) throws AccountsException {
        String methodName = "getAllAccountsByCustomerId(Long) in AccountsServiceImpl";
        //Optional<List<Accounts>> allAccounts = Optional.ofNullable(accountsRepository.findAllByCustomerId(customerId));
        //if (allAccounts.isEmpty())
            //throw new AccountsException(String.format("No such accounts present with this customer %s", customerId), methodName);
        //return allAccounts.get().stream().filter(accounts -> !STATUS_BLOCKED.equals(accounts.getAccountStatus())).map(Mapper::mapToAccountsDto).collect(Collectors.toList());
       return null;
    }

    private boolean updateValidator(Accounts accounts,String ...request) {

        return false;
    }

    private Accounts processAccountUpdate(AccountsDto accountsDto, Accounts accounts) throws AccountsException {
        String methodName = "processAccountUpdate(AccountsDto,Accounts) in AccountsServiceImpl";
        Accounts.Branch oldHomeBranch = accounts.getHomeBranch();
        Accounts.Branch newHomeBranch = accountsDto.getHomeBranch();

        Long oldCashLimit = accounts.getCashLimitPerDay();
        Long newCashLimit = accountsDto.getCashLimitPerDay();


        if (null != newHomeBranch && !newHomeBranch.equals(oldHomeBranch))
            accounts.setHomeBranch(newHomeBranch);
        if (null != newCashLimit && !newCashLimit.equals(oldCashLimit)) {
            if (updateValidator(accounts,UPDATE_CASH_LIMIT)) accounts.setCashLimitPerDay(newCashLimit);
            else throw new AccountsException(String.format("Yr Account with id %s must be at least " +
                    "six months old ", accounts.getAccountNumber()), methodName);
        }
        return accounts;
    }

    /**
     * @paramType Long, Long
     * @returnType AccountsDto
     */
    @Override
    public AccountsDto updateAccountDetails(AccountsDto accountsDto) throws AccountsException {
        //get the account
        Long accountNumber = accountsDto.getAccountNumber();
        Accounts foundAccount = fetchAccountByAccountNumber(accountNumber);
        //update
        Accounts updatedAccount = processAccountUpdate(accountsDto, foundAccount);
        Accounts savedUpdatedAccount = accountsRepository.save(updatedAccount);
        return Mapper.mapToAccountsDto(savedUpdatedAccount);
    }

    @Override
    public AccountsDto getCreditScore(Long accountNUmber){
        return null;
    }

    @Override
    public AccountsDto updateCreditScore(AccountsDto accountsDto) {
        return null;
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
        String methodName = "deleteAllAccountsByCustomer(Long ) in AccountsServiceImpl";
        //checking whether customer exist
//        Optional<List<Accounts>> foundCustomer = Optional.ofNullable(accountsRepository.findAllByCustomerId(customerId));
//        if (foundCustomer.isEmpty())
//            throw new AccountsException(String.format("No such customer with id %s", customerId), methodName);
//        //deleting it
//        accountsRepository.deleteAllByCustomerId(customerId);
    }

    @Override
    public void blockAccount(Long accountNumber) throws AccountsException {
        //Get account
        Accounts foundAccount = fetchAccountByAccountNumber(accountNumber, REQUEST_TO_BLOCK);
        //Block it
        foundAccount.setAccountStatus(STATUS_BLOCKED);
    }

}
