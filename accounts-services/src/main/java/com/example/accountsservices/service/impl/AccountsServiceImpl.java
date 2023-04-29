package com.example.accountsservices.service.impl;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.CustomerDto;
import com.example.accountsservices.dto.InputDto;
import com.example.accountsservices.dto.OutputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.mapper.Mapper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.CustomerRepository;
import com.example.accountsservices.service.AbstractAccountsService;
import com.example.accountsservices.util.BranchCodeRetrieverHelper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
@Primary
public class AccountsServiceImpl extends AbstractAccountsService {
    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;
    private final Accounts.AccountStatus STATUS_BLOCKED = Accounts.AccountStatus.BLOCKED;
    private final Accounts.AccountStatus STATUS_OPEN = Accounts.AccountStatus.OPEN;
    public static final String REQUEST_TO_BLOCK = "BLOCK";
    private final String UPDATE_CASH_LIMIT = "UPDATE_CASH_LIMIT";
    private final String GENERATE_CREDIT_SCORE = "gen_credit_score";
    private final String INIT = "INIT";
    private final String UPDATE = "UPDATE";


    /**
     * @paramType AccountsRepository
     * @returnType NA
     */
    public AccountsServiceImpl(AccountsRepository accountsRepository, CustomerRepository customerRepository) {
        super(accountsRepository);
        this.accountsRepository = accountsRepository;
        this.customerRepository = customerRepository;
    }


    private Accounts processAccountInit(Accounts accounts, String req) throws AccountsException {
        String methodName = "processAccountInit(Accounts,String) in AccountsServiceImpl";
        //If request is adding another accounts for a customer already have an account
        //there should not be two accounts with  same accountType in same homeBranch
        if (req.equalsIgnoreCase(UPDATE)) {
            //new homeBranch & type
            Accounts.Branch newHomeBranch = accounts.getHomeBranch();
            Accounts.AccountType newAccountType = accounts.getAccountType();

            //get all accounts for that customer
            List<Accounts> listOfAccounts = accounts.getCustomer().getAccounts();
            boolean isNotPermissible=listOfAccounts.stream().anyMatch(account -> (account.getHomeBranch().equals(newHomeBranch)
                            && account.getAccountType().equals(newAccountType)));

            if(isNotPermissible) throw  new AccountsException(String.format("You already have an %s" +
                    " account in %s branch",newAccountType,newHomeBranch),methodName);
        }

        //initialize customer account opening balance
        accounts.setBalance(0L);
        //initialize branchCode
        accounts.setBranchCode(BranchCodeRetrieverHelper.getBranchCode(accounts.getHomeBranch()));
        //initialize account status OPEN
        accounts.setAccountStatus(STATUS_OPEN);
        //initialize cash limit
        accounts.setTransferLimitPerDay(100000L);

        //initialize loan fields
        accounts.setTotLoanIssuedSoFar(0L);
        accounts.setTotalOutStandingAmountPayableToBank(0L);
        accounts.setAnyActiveLoans(false);
        //credit score is 0 so approved limit should also be zero
        accounts.setApprovedLoanLimitBasedOnCreditScore(0L);
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
    public OutputDto createAccountForNewUser(InputDto inputDto) throws AccountsException {
        Accounts account = Mapper.inputToAccounts(inputDto);
        Customer customer = Mapper.inputToCustomer(inputDto);
        Accounts processedAccount = processAccountInit(account, INIT);
        Customer processedCustomer = processCustomerInformation(customer);

        //save account & customer
        Customer savedCustomer = customerRepository.save(processedCustomer);
        processedAccount.setCustomer(savedCustomer);
        Accounts savedAccount = accountsRepository.save(processedAccount);
        return Mapper.mapToOutPut(Mapper.mapToCustomerDto(savedCustomer),Mapper.mapToAccountsDto(savedAccount) );
    }


    private OutputDto createAccountForAlreadyCreatedUser(Long customerId, AccountsDto accountsDto) throws AccountsException {
        String methodName = "createAccountForAlreadyCreatedUser(Long,InoutDto) in AccountsServiceImpl";
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty())
            throw new AccountsException(String.format("No such customers with id %s found", customerId), methodName);

        //some critical processing
        Accounts accounts = Mapper.mapToAccounts(accountsDto);
        //register this customer as the on=wner of this account
        accounts.setCustomer(customer.get());
        Accounts processedAccount = processAccountInit(accounts, UPDATE);

        //save it bebe
        Accounts savedAccount = accountsRepository.save(processedAccount);
        return Mapper.mapToOutPut(Mapper.mapToCustomerDto(customer.get()),Mapper.mapToAccountsDto(savedAccount));
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
        switch (request) {
            case UPDATE_CASH_LIMIT:
                return Period.between(accounts.getCreatedDate(), LocalDate.now()).getMonths() >= 6;
        }
        return false;
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
        String methodName = "increaseTransferLimit(AccountsDto,Accounts) in AccountsServiceImpl";
        Long oldCashLimit = accounts.getTransferLimitPerDay();
        Long newCashLimit = accountsDto.getTransferLimitPerDay();

        Accounts savedAccount = accounts;
        if (null != newCashLimit && !newCashLimit.equals(oldCashLimit)) {
            if (updateValidator(accounts, UPDATE_CASH_LIMIT)) accounts.setTransferLimitPerDay(newCashLimit);
            else throw new AccountsException(String.format("Yr Account with id %s must be at least " +
                    "six months old ", accounts.getAccountNumber()), methodName);
            savedAccount = accountsRepository.save(accounts);
        }
        return savedAccount;
    }

    /**
     * @paramType Long, Long
     * @returnType AccountsDto
     */
    @Override
    public OutputDto updateAccountDetails(InputDto inputDto) throws AccountsException {
        String methodName = "updateAccountDetails(AccountsDto) in AccountsServiceImpl";
        //map
        AccountsDto accountsDto = Mapper.inputToAccountsDto(inputDto);
        CustomerDto customerDto = Mapper.inputToCustomerDto(inputDto);
        //check the request type
        if (null == accountsDto.getUpdateRequest())
            throw new AccountsException("update request field must not be blank", methodName);
        AccountsDto.UpdateRequest request = accountsDto.getUpdateRequest();
        switch (request) {
            case CHANGE_HOME_BRANCH -> {
                //get the account
                Long accountNumber = accountsDto.getAccountNumber();
                Accounts foundAccount = fetchAccountByAccountNumber(accountNumber);
                Accounts updatedAccount = updateHomeBranch(accountsDto, foundAccount);
                return Mapper.mapToOutPut(customerDto,Mapper.mapToAccountsDto(updatedAccount));
            }
            case GET_CREDIT_SCORE -> {
                //to be done after implementing credit card microservice
            }
            case INC_TRANSFER_LIMIT -> {
                //get the account
                Long accountNumber = accountsDto.getAccountNumber();
                Accounts foundAccount = fetchAccountByAccountNumber(accountNumber);
                Accounts updatedAccount = increaseTransferLimit(accountsDto, foundAccount);
                return Mapper.mapToOutPut(customerDto,Mapper.mapToAccountsDto(updatedAccount));
            }
            case ADD_ACCOUNT -> {
                return createAccountForAlreadyCreatedUser(customerDto.getCustomerId(),accountsDto);
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

        return new OutputDto();
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
