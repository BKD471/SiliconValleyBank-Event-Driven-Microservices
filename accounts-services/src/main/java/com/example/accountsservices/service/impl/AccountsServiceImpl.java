package com.example.accountsservices.service.impl;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.InputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.mapper.Mapper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.CustomerRepository;
import com.example.accountsservices.service.AbstractAccountsService;
import com.example.accountsservices.util.BranchCodeRetrieverHelper;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private final String UPDATE_CASH_LIMIT = "UPDATE_CASH_LIMIT";
    private final String GENERATE_CREDIT_SCORE = "gen_credit_score";


    /**
     * @paramType AccountsRepository
     * @returnType NA
     */
    public AccountsServiceImpl(AccountsRepository accountsRepository, CustomerRepository customerRepository) {
        super(accountsRepository);
        this.accountsRepository = accountsRepository;
        this.customerRepository = customerRepository;
    }


    private Accounts processAccountInformation(Accounts accounts) throws AccountsException {
        //set customer account opening balance
        accounts.setBalance(0L);
        //set branchCode
        accounts.setBranchCode(BranchCodeRetrieverHelper.getBranchCode(accounts.getHomeBranch()));
        //set account status OPEN
        accounts.setAccountStatus(STATUS_OPEN);
        //set cash limit
        accounts.setTransferLimitPerDay(100000L);
        return accounts;
    }


    private Customer processCustomerInformation(Customer customer) throws AccountsException {
        String methodName = "processCustomerInformation(Customer) in AccountsServiceIMpl";
        //set customer age from dob
        LocalDate dob = customer.getDateOfBirth();
        int age = Period.between(dob, LocalDate.now()).getYears();
        if (age < 18) throw new AccountsException("Account holder must be at least 18 years", methodName);

        customer.setAge(age);
        return customer;
    }

    /**
     * @paramType AccountsDto
     * @returnType AccountsDto
     */
    @Override
    public InputDto createAccountForNewUser(InputDto inputDto) throws AccountsException {
        Accounts account = Mapper.inputToAccounts(inputDto);
        Customer customer = Mapper.inputToCustomer(inputDto);
        Accounts processedAccount = processAccountInformation(account);
        Customer processedCustomer = processCustomerInformation(customer);

        //save account & customer
        Customer savedCustomer = customerRepository.save(processedCustomer);
        processedAccount.setCustomer(savedCustomer);
        Accounts savedAccount = accountsRepository.save(processedAccount);
        return Mapper.mapToinputDto(savedCustomer, savedAccount);
    }

    @Override
    public InputDto createAccountForAlreadyCreatedUser(Long customerId, InputDto inputDto) throws AccountsException {
        String methodName = "createAccountForAlreadyCreatedUser(Long,InoutDto) in AccountsServiceImpl";
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty())
            throw new AccountsException(String.format("No such customers with id %s found", customerId), methodName);

        //some critical processing
        Accounts accounts = Mapper.inputToAccounts(inputDto);
        Accounts processedAccount = processAccountInformation(accounts);

        //register this customer as the on=wner of this account
        processedAccount.setCustomer(customer.get());

        //save it bebe
        Accounts savedAccount = accountsRepository.save(processedAccount);
        return Mapper.mapToinputDto(customer.get(), savedAccount);
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

    private boolean updateValidator(Accounts accounts, String request) {

        switch (request){
            case UPDATE_CASH_LIMIT: return Period.between(accounts.getCreatedDate(), LocalDate.now()).getMonths() >= 6;
        }
        return false;
    }

    private Accounts processAccountUpdate(AccountsDto accountsDto, Accounts accounts) throws AccountsException {
        String methodName = "processAccountUpdate(AccountsDto,Accounts) in AccountsServiceImpl";
        Accounts.Branch oldHomeBranch = accounts.getHomeBranch();
        Accounts.Branch newHomeBranch = accountsDto.getHomeBranch();

        Long oldCashLimit = accounts.getTransferLimitPerDay();
        Long newCashLimit = accountsDto.getTransferLimitPerDay();


        if (null != newHomeBranch && !newHomeBranch.equals(oldHomeBranch))
            accounts.setHomeBranch(newHomeBranch);
        if (null != newCashLimit && !newCashLimit.equals(oldCashLimit)) {
            if (updateValidator(accounts, UPDATE_CASH_LIMIT)) accounts.setTransferLimitPerDay(newCashLimit);
            else throw new AccountsException(String.format("Yr Account with id %s must be at least " +
                    "six months old ", accounts.getAccountNumber()), methodName);
        }
        return accounts;
    }

    private Accounts updateHomeBranch(AccountsDto accountsDto, Accounts accounts) {
        Accounts.Branch oldHomeBranch = accounts.getHomeBranch();
        Accounts.Branch newHomeBranch = accountsDto.getHomeBranch();

        Accounts savedUpdatedAccount = accounts;
        if (null != newHomeBranch && !newHomeBranch.equals(oldHomeBranch)) {
            accounts.setHomeBranch(newHomeBranch);
            savedUpdatedAccount = accountsRepository.save(accounts);
        }
        return savedUpdatedAccount;
    }

    private Accounts increaseTransferLimit(AccountsDto accountsDto, Accounts accounts) throws AccountsException {
        String methodName="increaseTransferLimit(AccountsDto,Accounts) in AccountsServiceImpl";
        Long oldCashLimit = accounts.getTransferLimitPerDay();
        Long newCashLimit = accountsDto.getTransferLimitPerDay();

        Accounts savedAccount=accounts;
        if (null != newCashLimit && !newCashLimit.equals(oldCashLimit)) {
            if (updateValidator(accounts, UPDATE_CASH_LIMIT)) accounts.setTransferLimitPerDay(newCashLimit);
            else throw new AccountsException(String.format("Yr Account with id %s must be at least " +
                    "six months old ", accounts.getAccountNumber()), methodName);
           savedAccount=accountsRepository.save(accounts);
        }
        return savedAccount;
    }

    /**
     * @paramType Long, Long
     * @returnType AccountsDto
     */
    @Override
    public AccountsDto updateAccountDetails(AccountsDto accountsDto) throws AccountsException {
        String methodName = "updateAccountDetails(AccountsDto) in AccountsServiceImpl";
        //get the account
        Long accountNumber = accountsDto.getAccountNumber();
        Accounts foundAccount = fetchAccountByAccountNumber(accountNumber);

        //check the request type
        if (null == accountsDto.getUpdateRequest())
            throw new AccountsException("update request field must not be blank", methodName);
        AccountsDto.UpdateRequest request = accountsDto.getUpdateRequest();
        switch (request) {
            case CHANGE_HOME_BRANCH -> {
                Accounts updatedAccount = updateHomeBranch(accountsDto, foundAccount);
                return Mapper.mapToAccountsDto(updatedAccount);
            }
            case GET_CREDIT_SCORE -> {
                //to be done after implementing credit card microservice
            }
            case INC_TRANSFER_LIMIT -> {
                Accounts updatedAccount = increaseTransferLimit(accountsDto, foundAccount);
                return Mapper.mapToAccountsDto(updatedAccount);
            }
            case INC_APPROVED_LOAN_LIMIT -> {
                //to be done.....
            }
            case LEND_LOAN -> {
                //to be done...
            }
            case BLOCK_ACC -> {
            }
            case DELETE_ACC -> {
            }
        }

       return accountsDto;
    }


    @Override
    public AccountsDto getCreditScore(Long accountNUmber) {
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
