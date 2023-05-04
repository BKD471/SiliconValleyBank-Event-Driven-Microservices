package com.example.accountsservices.service.impl;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.CustomerDto;
import com.example.accountsservices.dto.InputDto;
import com.example.accountsservices.dto.OutputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.CustomerException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
@Primary
public class AccountsServiceImpl extends AbstractAccountsService {
    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;
    private final Accounts.AccountStatus STATUS_BLOCKED = Accounts.AccountStatus.BLOCKED;
    private final Accounts.AccountStatus STATUS_OPEN = Accounts.AccountStatus.OPEN;
    public static final String REQUEST_TO_BLOCK = "BLOCK";
    private final String INIT = "INIT";
    private final String UPDATE = "UPDATE";

    private enum ValidateType {
        UPDATE_CASH_LIMIT, UPDATE_HOME_BRANCH, GENERATE_CREDIT_SCORE, UPDATE_CREDIT_SCORE
    }

    private final ValidateType UPDATE_CASH_LIMIT = ValidateType.UPDATE_CASH_LIMIT;
    private final ValidateType UPDATE_HOME_BRANCH = ValidateType.UPDATE_HOME_BRANCH;


    /**
     * @paramType AccountsRepository
     * @returnType NA
     */
    public AccountsServiceImpl(AccountsRepository accountsRepository, CustomerRepository customerRepository) {
        super(accountsRepository);
        this.accountsRepository = accountsRepository;
        this.customerRepository = customerRepository;
    }

    private Boolean checkConflictingAccountUpdateConditionForBranch(Accounts accounts, AccountsDto accountsDto, String locality) throws AccountsException {
        String location = String.format("Inside checkConflictingAccountUpdateConditionForBranch(Accounts) in AccountsServiceImpl" +
                "coming from %s", locality);
        Accounts.Branch newhomeBranch = null;
        newhomeBranch = (null == accountsDto) ? accounts.getHomeBranch() : accountsDto.getHomeBranch();
        Accounts.AccountType accountType = accounts.getAccountType();

        //get all accounts for customer
        List<Accounts> listOfAccounts = accounts.getCustomer().getAccounts();

        Accounts.Branch finalNewhomeBranch = newhomeBranch;
        boolean isNotPermissible = listOfAccounts.stream().
                anyMatch(account -> finalNewhomeBranch.equals(account.getHomeBranch()) && accountType.equals(account.getAccountType()));

        if (isNotPermissible) throw new AccountsException(AccountsException.class,
                String.format("You already have an account with same accountType %s" +
                                "and same HomeBranch %s",
                        accounts.getAccountType(), accounts.getHomeBranch()), location);

        return true;
    }

    private Accounts processAccountInit(Accounts accounts, String req) throws AccountsException {
        String methodName = "processAccountInit(Accounts,String) in AccountsServiceImpl";
        //If request is adding another accounts for a customer already have an account
        //there should not be two accounts with  same accountType in same homeBranch
        if (req.equalsIgnoreCase(UPDATE)) {
            checkConflictingAccountUpdateConditionForBranch(accounts, null, methodName);
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
        if (age < 18)
            throw new AccountsException(AccountsException.class, "Account holder must be at least 18 years", methodName);

        customer.setAge(age);
        return customer;
    }


    private OutputDto createAccount(InputDto inputDto) throws AccountsException {
        Accounts account = Mapper.inputToAccounts(inputDto);
        Customer customer = Mapper.inputToCustomer(inputDto);
        Accounts processedAccount = processAccountInit(account, INIT);
        Customer processedCustomer = processCustomerInformation(customer);

        //add account to listOfAccounts of Customer & register customer as the owner of this accnt
        List<Accounts> listOfAccounts = new ArrayList<>();
        listOfAccounts.add(processedAccount);
        processedCustomer.setAccounts(listOfAccounts);
        processedAccount.setCustomer(processedCustomer);

        //save customer(parent) only , no need to save accounts(child) its auto saved due to cascadeType.All
        //thus reducing call to db
        Customer savedCustomer = customerRepository.save(processedCustomer);

        //fetch the corresponding account of saved customer
        Long accountNumber = savedCustomer.getAccounts().get(0).getAccountNumber();

        return Mapper.mapToOutPutDto(Mapper.mapToCustomerDto(savedCustomer), Mapper.mapToAccountsDto(savedCustomer.getAccounts().get(0))
                , String.format("Account with id %s is created for customer %s", accountNumber,
                        savedCustomer.getCustomerId()));
    }


    private OutputDto createAccountForAlreadyCreatedUser(Long customerId, AccountsDto accountsDto) throws AccountsException {
        String methodName = "createAccountForAlreadyCreatedUser(Long,InoutDto) in AccountsServiceImpl";
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty())
            throw new AccountsException(AccountsException.class, String.format("No such customers with id %s found", customerId), methodName);

        //some critical processing
        Accounts accounts = Mapper.mapToAccounts(accountsDto);
        //register this customer as the owner of this account
        accounts.setCustomer(customer.get());
        Accounts processedAccount = processAccountInit(accounts, UPDATE);

        //save it bebe
        Accounts savedAccount = accountsRepository.save(processedAccount);
        return Mapper.mapToOutPutDto(Mapper.mapToCustomerDto(customer.get()),
                Mapper.mapToAccountsDto(savedAccount), String.format("New account with id %s is created " +
                                "for customer with id:%s",
                        savedAccount.getAccountNumber(), customerId));
    }


    /**
     * @param accountNumber accountNumber
     * @paramType Long
     * @returnType AccountsDto
     */

    private OutputDto getAccountInfo(Long accountNumber) throws AccountsException {
        Accounts foundAccount = fetchAccountByAccountNumber(accountNumber);
        Customer foundCustomer = foundAccount.getCustomer();
        return Mapper.mapToOutPutDto(Mapper.mapToCustomerDto(foundCustomer),
                Mapper.mapToAccountsDto(foundAccount), String.format("Retrieved info about account with id: %s", foundAccount.getAccountNumber()));
    }


    private List<AccountsDto> getAllActiveAccountsByCustomerId(Long customerId) throws AccountsException {
        String methodName = "getAllAccountsByCustomerId(Long) in AccountsServiceImpl";
        Optional<List<Accounts>> allAccounts = accountsRepository.findAllByCustomer_CustomerId(customerId);
        if (allAccounts.isEmpty())
            throw new AccountsException(AccountsException.class, String.format("No such accounts present with this customer %s", customerId), methodName);
        return allAccounts.get().stream().filter(accounts -> !STATUS_BLOCKED.equals(accounts.getAccountStatus())).map(Mapper::mapToAccountsDto).collect(Collectors.toList());
    }

    private Boolean updateValidator(Accounts accounts, AccountsDto accountsDto, ValidateType request) throws AccountsException {
        String location = "updateValidator(Accounts,ValidateType) in AccountsServiceImpl";

        switch (request) {
            case UPDATE_CASH_LIMIT -> {
                return Period.between(accounts.getCreatedDate(), LocalDate.now()).getMonths() >= 6;
            }
            case UPDATE_HOME_BRANCH -> {
                String locality = String.format("Inside UPDATE_HOME_BRANCH of %s", location);
                return checkConflictingAccountUpdateConditionForBranch(accounts, accountsDto, locality);
            }

        }
        return false;
    }

    private Accounts updateHomeBranch(AccountsDto accountsDto, Accounts accounts) throws AccountsException {
        Accounts.Branch oldHomeBranch = accounts.getHomeBranch();
        Accounts.Branch newHomeBranch = accountsDto.getHomeBranch();
        Accounts savedUpdatedAccount = accounts;

        if (updateValidator(accounts, accountsDto, UPDATE_HOME_BRANCH) && null != newHomeBranch && !newHomeBranch.equals(oldHomeBranch)) {
            accounts.setHomeBranch(newHomeBranch);
            accounts.setBranchCode(BranchCodeRetrieverHelper.getBranchCode(newHomeBranch));
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
            if (updateValidator(accounts, accountsDto, UPDATE_CASH_LIMIT))
                accounts.setTransferLimitPerDay(newCashLimit);
            else
                throw new AccountsException(AccountsException.class, String.format("Yr Account with id %s must be at least " +
                        "six months old ", accounts.getAccountNumber()), methodName);
            savedAccount = accountsRepository.save(accounts);
        }
        return savedAccount;
    }

    private void blockAccount(Long accountNumber) throws AccountsException {
        //Get account
        Accounts foundAccount = fetchAccountByAccountNumber(accountNumber, REQUEST_TO_BLOCK);
        //Block it
        foundAccount.setAccountStatus(STATUS_BLOCKED);
    }

    private void deleteAccount(Long accountNumber) throws AccountsException {
        //checking whether account exist or not
        fetchAccountByAccountNumber(accountNumber);
        //deleting it
        accountsRepository.deleteByAccountNumber(accountNumber);
    }

    private void deleteAllAccountsByCustomer(Long customerId) throws AccountsException {
        String methodName = "deleteAllAccountsByCustomer(Long ) in AccountsServiceImpl";
        //checking whether customer exist
//        Optional<List<Accounts>> foundCustomer = Optional.ofNullable(accountsRepository.findAllByCustomerId(customerId));
//        if (foundCustomer.isEmpty())
//            throw new AccountsException(String.format("No such customer with id %s", customerId), methodName);
//        //deleting it
//        accountsRepository.deleteAllByCustomerId(customerId);
    }

    private int getCreditScore(Long accountNumber) {
        ///to be done
        return 0;
    }


    private int updateCreditScore(AccountsDto accountsDto) {
        return 0;
    }


    @Override
    public OutputDto postRequestExecutor(InputDto inputDto) throws AccountsException {
        String methodName = "requestExecutor(InputDto) in AccountsServiceImpl";
        //map
        AccountsDto accountsDto = Mapper.inputToAccountsDto(inputDto);
        CustomerDto customerDto = Mapper.inputToCustomerDto(inputDto);
        //check the request type
        if (null == accountsDto.getUpdateRequest())
            throw new AccountsException(AccountsException.class, "update request field must not be blank", methodName);
        AccountsDto.UpdateRequest request = accountsDto.getUpdateRequest();
        switch (request) {
            case CREATE_ACC -> {
                return createAccount(inputDto);
            }
            case ADD_ACCOUNT -> {
                return createAccountForAlreadyCreatedUser(customerDto.getCustomerId(), accountsDto);
            }
            case LEND_LOAN -> {
                //to be done...
                return new OutputDto("Baad main karenge");
            }
            default ->
                    throw new AccountsException(AccountsException.class, String.format("Invalid request type %s for POST requests", request), methodName);
        }
    }

    @Override
    public OutputDto getRequestExecutor(InputDto inputDto) throws AccountsException, CustomerException {
        String methodName = "requestExecutor(InputDto) in AccountsServiceImpl";
        //map
        AccountsDto accountsDto = Mapper.inputToAccountsDto(inputDto);
        CustomerDto customerDto = Mapper.inputToCustomerDto(inputDto);
        //check the request type
        if (null == accountsDto.getUpdateRequest())
            throw new AccountsException(AccountsException.class, "update request field must not be blank", methodName);
        AccountsDto.UpdateRequest request = accountsDto.getUpdateRequest();
        switch (request) {

            case GET_CREDIT_SCORE -> {
                Long accountNumber = accountsDto.getAccountNumber();
                getCreditScore(accountNumber);
                //to be done after implementing credit card microservice
                return new OutputDto("Baad main karnge");
            }
            case GET_ACC_INFO -> {
                Long accountNumber = accountsDto.getAccountNumber();
                return getAccountInfo(accountNumber);
            }
            case GET_ALL_ACC -> {
                String locality = String.format("Inside switch ,for GET_ALL_ACC case under method %s", methodName);
                Long customerId = customerDto.getCustomerId();
                Optional<Customer> foundCustomer = customerRepository.findById(customerId);
                if (foundCustomer.isEmpty()) throw new CustomerException(CustomerException.class, locality, methodName);

                List<AccountsDto> listOfAccounts = getAllActiveAccountsByCustomerId(customerId);
                return new OutputDto(Mapper.mapToCustomerOutputDto(Mapper.mapToCustomerDto(foundCustomer.get())), listOfAccounts,
                        String.format("Fetched all accounts for customer id:%s", customerId));
            }
            default ->
                    throw new AccountsException(AccountsException.class, String.format("Invalid request type %s for GET request", request), methodName);
        }
    }

    @Override
    public OutputDto putRequestExecutor(InputDto inputDto) throws AccountsException {
        String methodName = "requestExecutor(InputDto) in AccountsServiceImpl";
        //map
        AccountsDto accountsDto = Mapper.inputToAccountsDto(inputDto);
        CustomerDto customerDto = Mapper.inputToCustomerDto(inputDto);
        //check the request type
        if (null == accountsDto.getUpdateRequest())
            throw new AccountsException(AccountsException.class, "update request field must not be blank", methodName);
        AccountsDto.UpdateRequest request = accountsDto.getUpdateRequest();
        switch (request) {
            case UPDATE_HOME_BRANCH -> {
                //get the account
                Long accountNumber = accountsDto.getAccountNumber();
                Accounts foundAccount = fetchAccountByAccountNumber(accountNumber);
                Accounts updatedAccount = updateHomeBranch(accountsDto, foundAccount);
                return Mapper.mapToOutPutDto(Mapper.mapToCustomerDto(updatedAccount.getCustomer()), Mapper.mapToAccountsDto(updatedAccount),
                        String.format("Home branch for is changed from %s to %s for customer with id %s",
                                foundAccount.getHomeBranch(), accountsDto.getHomeBranch(),
                                foundAccount.getCustomer().getCustomerId()));
            }
            case UPDATE_CREDIT_SCORE -> {
                updateCreditScore(accountsDto);
                return new OutputDto("Baad main karenge");
            }
            case INC_TRANSFER_LIMIT -> {
                //get the account
                Long accountNumber = accountsDto.getAccountNumber();
                Accounts foundAccount = fetchAccountByAccountNumber(accountNumber);
                Accounts updatedAccount = increaseTransferLimit(accountsDto, foundAccount);
                return Mapper.mapToOutPutDto(customerDto, Mapper.mapToAccountsDto(updatedAccount),
                        String.format("Transfer Limit has been increased from %s to %s", foundAccount.getTransferLimitPerDay(), accountsDto.getTransferLimitPerDay()));
            }
            case BLOCK_ACC -> {
                Long accountNumber = accountsDto.getAccountNumber();
                blockAccount(accountNumber);
                return new OutputDto(String.format("Account with id %s is successfully blocked", accountNumber));
            }
            case INC_APPROVED_LOAN_LIMIT -> {
                //to be done.....

                return new OutputDto("Baad main karenge");
            }
            default ->
                    throw new AccountsException(AccountsException.class, String.format("Invalid request type %s for PUT request", request), methodName);
        }
    }

    @Override
    public OutputDto deleteRequestExecutor(InputDto inputDto) throws AccountsException {
        String methodName = "requestExecutor(InputDto) in AccountsServiceImpl";
        //map
        AccountsDto accountsDto = Mapper.inputToAccountsDto(inputDto);
        CustomerDto customerDto = Mapper.inputToCustomerDto(inputDto);
        //check the request type
        if (null == accountsDto.getUpdateRequest())
            throw new AccountsException(AccountsException.class, "update request field must not be blank", methodName);
        AccountsDto.UpdateRequest request = accountsDto.getUpdateRequest();
        switch (request) {
            case DELETE_ACC -> {
                Long accountNumber = accountsDto.getAccountNumber();
                deleteAccount(accountNumber);
                return new OutputDto(String.format("Account with id %s is successfully deleted", accountNumber));
            }
            case DELETE_ALL_ACC -> {
                deleteAllAccountsByCustomer(customerDto.getCustomerId());
                return new OutputDto(String.format("All accounts that belonged to customer with id %s has " +
                        "been deleted", customerDto.getCustomerId()));
            }
            default ->
                    throw new AccountsException(AccountsException.class, String.format("Invalid request type %s for DELETE request", request), methodName);
        }
    }
}
