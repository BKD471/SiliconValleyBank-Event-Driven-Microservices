package com.example.accountsservices.service.impl;

import com.example.accountsservices.dto.*;
import com.example.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto.DIRECTION;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BadApiRequestException;
import com.example.accountsservices.exception.CustomerException;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.CustomerRepository;
import com.example.accountsservices.service.AbstractAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.List;

import static com.example.accountsservices.helpers.CodeRetrieverHelper.getBranchCode;
import static com.example.accountsservices.helpers.MapperHelper.*;
import static com.example.accountsservices.helpers.PagingHelper.*;
import static com.example.accountsservices.model.Accounts.AccountStatus;


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
    private final FIleServiceImpl fIleService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final AccountStatus STATUS_BLOCKED = AccountStatus.BLOCKED;
    private final AccountStatus STATUS_OPEN = AccountStatus.OPEN;
    private final AccountStatus STATUS_CLOSED = AccountStatus.CLOSED;
    public static final String REQUEST_TO_BLOCK = "BLOCK";
    private final String INIT = "INIT";
    private final String UPDATE = "UPDATE";

    @Value("${customer.profile.images.path}")
    private String IMAGE_PATH;

    private enum ValidateType {
        UPDATE_CASH_LIMIT, UPDATE_HOME_BRANCH,
        GENERATE_CREDIT_SCORE, UPDATE_CREDIT_SCORE,
        UPLOAD_PROFILE_IMAGE, CLOSE_ACCOUNT, RE_OPEN_ACCOUNT,
        BLOCK_ACCOUNT, CREATE_ACC, ADD_ACC
    }

    private final ValidateType UPDATE_CASH_LIMIT = ValidateType.UPDATE_CASH_LIMIT;
    private final ValidateType UPDATE_HOME_BRANCH = ValidateType.UPDATE_HOME_BRANCH;
    private final ValidateType CLOSE_ACCOUNT = ValidateType.CLOSE_ACCOUNT;
    private final ValidateType RE_OPEN_ACCOUNT = ValidateType.RE_OPEN_ACCOUNT;
    private final ValidateType BLOCK_ACCOUNT = ValidateType.BLOCK_ACCOUNT;
    private final ValidateType CREATE_ACCOUNT = ValidateType.CREATE_ACC;
    private final ValidateType ADD_ACCOUNT = ValidateType.ADD_ACC;
    private final ValidateType UPLOAD_PROFILE_IMAGE = ValidateType.UPLOAD_PROFILE_IMAGE;


    /**
     * @paramType AccountsRepository
     * @returnType NA
     */
    public AccountsServiceImpl(AccountsRepository accountsRepository, CustomerRepository customerRepository,
                               FIleServiceImpl fIleService) {
        super(accountsRepository, customerRepository);
        this.accountsRepository = accountsRepository;
        this.customerRepository = customerRepository;
        this.fIleService = fIleService;
    }

    private Boolean checkConflictingAccountUpdateConditionForBranch(Accounts accounts, AccountsDto accountsDto, String locality) throws AccountsException {
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
        accounts.setBranchCode(getBranchCode(accounts.getHomeBranch()));
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

    private Customer processCustomerInformation(Customer customer) {
        //set customer age from dob
        LocalDate dob = customer.getDateOfBirth();
        int age = Period.between(dob, LocalDate.now()).getYears();
        customer.setAge(age);
        //encode passwd
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customer;
    }

    private OutputDto createAccount(PostInputRequestDto postInputRequestDto) throws AccountsException {
        Accounts account = inputToAccounts(postInputRequestDto);
        Customer customer = inputToCustomer(postInputRequestDto);
        account.setCustomer(customer);

        updateValidator(account, mapToAccountsDto(account), null, CREATE_ACCOUNT);

        Accounts processedAccount = processAccountInit(account, INIT);
        Customer processedCustomer = processCustomerInformation(customer);

        //add account to listOfAccounts of Customer & register customer as the owner of this account
        List<Accounts> listOfAccounts = new ArrayList<>();
        listOfAccounts.add(processedAccount);
        processedCustomer.setAccounts(listOfAccounts);
        processedAccount.setCustomer(processedCustomer);

        //save customer(parent) only , no need to save accounts(child) its auto saved due to cascadeType.All
        //thus reducing call to db
        Customer savedCustomer = customerRepository.save(processedCustomer);

        //fetch the corresponding account of saved customer
        Long accountNumber = savedCustomer.getAccounts().get(0).getAccountNumber();

        return OutputDto.builder()
                .customer(mapToCustomerOutputDto(mapToCustomerDto(savedCustomer)))
                .accounts(mapToAccountsOutputDto(mapToAccountsDto(savedCustomer.getAccounts().get(0))))
                .defaultMessage(String.format("Account with id %s is created for customer %s", accountNumber, savedCustomer.getCustomerId()))
                .build();
    }


    private OutputDto createAccountForAlreadyCreatedUser(Long customerId, Accounts loadAccount, AccountsDto accountsDto) throws AccountsException {
        String methodName = "createAccountForAlreadyCreatedUser(Long,InoutDto) in AccountsServiceImpl";

        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            throw new AccountsException(AccountsException.class,
                    String.format("No such customers with id %s found", customerId),
                    methodName);
        }
        loadAccount.setCustomer(customer.get());
        //validate
        updateValidator(loadAccount, accountsDto, null, ADD_ACCOUNT);
        //some critical processing
        Accounts accounts = mapToAccounts(accountsDto);
        //register this customer as the owner of this account
        accounts.setCustomer(customer.get());
        Accounts processedAccount = processAccountInit(accounts, UPDATE);
        //save it bebe
        Accounts savedAccount = accountsRepository.save(processedAccount);

        return OutputDto.builder()
                .customer(mapToCustomerOutputDto(mapToCustomerDto(customer.get())))
                .accounts(mapToAccountsOutputDto(mapToAccountsDto(savedAccount)))
                .defaultMessage(String.format("New account with id %s is created for customer with id:%s", savedAccount.getAccountNumber(), customerId))
                .build();
    }

    /**
     * @param accountNumber accountNumber
     * @paramType Long
     * @returnType AccountsDto
     */
    private OutputDto getAccountInfo(Long accountNumber) throws AccountsException {
        Accounts foundAccount = fetchAccountByAccountNumber(accountNumber);
        Customer foundCustomer = foundAccount.getCustomer();

        return OutputDto.builder()
                .customer(mapToCustomerOutputDto(mapToCustomerDto(foundCustomer)))
                .accounts(mapToAccountsOutputDto(mapToAccountsDto(foundAccount)))
                .defaultMessage(String.format("Retrieved info about account with id: %s", foundAccount.getAccountNumber()))
                .build();
    }

    private PageableResponseDto<AccountsDto> getAllActiveAccountsByCustomerId(Long customerId, Pageable pageable) throws AccountsException {
        String methodName = "getAllAccountsByCustomerId(Long) in AccountsServiceImpl";
        Optional<Page<Accounts>> allPagedAccounts = accountsRepository.findAllByCustomer_CustomerId(customerId, pageable);
        if (allPagedAccounts.isEmpty())
            throw new AccountsException(AccountsException.class,
                    String.format("No such accounts present with this customer %s", customerId), methodName);

        return getPageableResponse(allPagedAccounts.get(), AccountsDto.class);
    }

    private Boolean updateValidator(Accounts accounts, AccountsDto accountsDto, CustomerDto customerDto, ValidateType request) throws AccountsException {
        String methodName = "updateValidator(Accounts,ValidateType) in AccountsServiceImpl";
        String location = "";
        switch (request) {
            case CREATE_ACC -> {
                location = "Inside CREATE_ACC";
                //check whether such account owner is already present
                List<Accounts> accountsList = accountsRepository.findAll();
                //if no accounts by far then certainly we can add
                if (accountsList.isEmpty()) return true;

                String adharNumber = accounts.getCustomer().getAdharNumber();
                boolean isNotPossible = accountsList.stream().anyMatch(acc -> adharNumber.equalsIgnoreCase(acc.getCustomer().getAdharNumber()));
                if (isNotPossible)
                    throw new AccountsException(AccountsException.class, String.format("You already have an account with adhar:%s", adharNumber),
                            String.format("%s of %s", location, methodName));
            }
            case ADD_ACC -> {
                location = "Inside ADD_ACC";
                //prevent a customer to create more than 10 accounts
                Customer customer = accounts.getCustomer();
                if (customer.getAccounts().size() >= 7) throw new AccountsException(AccountsException.class,
                        "You can;t have more than 10 accounts",
                        String.format("%s of %s", location, methodName));
            }
            case UPDATE_CASH_LIMIT -> {
                return Period.between(accounts.getCreatedDate(), LocalDate.now()).getMonths() >= 6;
            }
            case UPLOAD_PROFILE_IMAGE -> {
                location = "Inside UPLOAD_PROFILE_IMAGE";
                if (null == customerDto.getCustomerImage()) throw new BadApiRequestException(BadApiRequestException.class,
                        "Please provide image", String.format("%s of %s", methodName, location));
                double FIlE_SIZE_TO_MB_CONVERTER_FACTOR = 0.00000095367432;
                if (customerDto.getCustomerImage().getSize() * FIlE_SIZE_TO_MB_CONVERTER_FACTOR <= 0.0 || customerDto.getCustomerImage().getSize() * FIlE_SIZE_TO_MB_CONVERTER_FACTOR > 100.0)
                    throw new BadApiRequestException(BadApiRequestException.class,
                            "Your file is either corrupted or you are exceeding the max size of 100mb",
                            String.format("%s of %s", methodName, location));
            }
            case UPDATE_HOME_BRANCH -> {
                location = "Inside UPDATE_HOME_BRANCH";
                return checkConflictingAccountUpdateConditionForBranch(accounts,
                        accountsDto, String.format("%s of %s", location, methodName));
            }
            case CLOSE_ACCOUNT -> {
                Accounts.AccountStatus status = accounts.getAccountStatus();
                switch (status) {
                    case CLOSED ->
                            throw new AccountsException(AccountsException.class, String.format("Account: %s is already closed", accounts.getAccountNumber()), location);
                    case BLOCKED ->
                            throw new AccountsException(AccountsException.class, String.format("Cant perform anything on Blocked account:%s", accounts.getAccountNumber()), location);
                    case OPEN -> {
                        return accounts.getAnyActiveLoans();
                    }
                }
            }
            case RE_OPEN_ACCOUNT -> {
                Accounts.AccountStatus status = accounts.getAccountStatus();
                switch (status) {
                    case CLOSED -> {
                        return true;
                    }
                    case BLOCKED ->
                            throw new AccountsException(AccountsException.class, String.format("Cant perform anything on Blocked account:%s", accounts.getAccountNumber()), location);
                    case OPEN ->
                            throw new AccountsException(AccountsException.class, String.format("Status of Account: %s is already Open", accounts.getAccountNumber()), location);
                }
            }
            case BLOCK_ACCOUNT -> {
                if (accounts.getAccountStatus().equals(STATUS_BLOCKED))
                    throw new AccountsException(AccountsException.class,
                            String.format("Status of Account: %s is already Blocked",
                                    accounts.getAccountStatus()), location);
                return true;
            }
        }
        return false;
    }

    private Accounts updateHomeBranch(AccountsDto accountsDto, Accounts accounts) throws AccountsException {
        Accounts.Branch oldHomeBranch = accounts.getHomeBranch();
        Accounts.Branch newHomeBranch = accountsDto.getHomeBranch();
        Accounts savedUpdatedAccount = accounts;

        if (updateValidator(accounts, accountsDto, null, UPDATE_HOME_BRANCH)
                && null != newHomeBranch && !newHomeBranch.equals(oldHomeBranch)) {
            accounts.setHomeBranch(newHomeBranch);
            accounts.setBranchCode(getBranchCode(newHomeBranch));
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
            if (updateValidator(accounts, accountsDto, null, UPDATE_CASH_LIMIT))
                accounts.setTransferLimitPerDay(newCashLimit);
            else
                throw new AccountsException(AccountsException.class,
                        String.format("Yr Account with id %s must be at least " +
                                "six months old ", accounts.getAccountNumber()), methodName);
            savedAccount = accountsRepository.save(accounts);
        }
        return savedAccount;
    }

    private void blockAccount(Accounts foundAccount) throws AccountsException {
        //Note: block is very urgent so no prior validation is required  for ongoing loan
        //but authority reserves right to scrutiny any ongoing loan Emi

        updateValidator(foundAccount, mapToAccountsDto(foundAccount), null, BLOCK_ACCOUNT);
        //Block it
        foundAccount.setAccountStatus(STATUS_BLOCKED);
        //save it
        accountsRepository.save(foundAccount);
    }

    private void closeAccount(Accounts foundAccount) throws AccountsException {
        String methodName = "closeAccount(accountNUmber) in AccountsServiceImpl";
        //check if he has pending loan
        if (updateValidator(foundAccount, mapToAccountsDto(foundAccount), null, CLOSE_ACCOUNT))
            throw new AccountsException(AccountsException.class, String.format("This account with id %s still has " +
                    "running loan. Please consider paying it before closing", foundAccount.getAccountNumber()), methodName);
        //close it
        foundAccount.setAccountStatus(STATUS_CLOSED);
        //save it
        accountsRepository.save(foundAccount);
    }

    private void unCloseAccount(Accounts account) throws AccountsException {
        if (updateValidator(account, mapToAccountsDto(account), null, RE_OPEN_ACCOUNT))
            accountsRepository.save(account);
        account.setAccountStatus(STATUS_OPEN);
        accountsRepository.save(account);
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
        Optional<Customer> foundCustomer = customerRepository.findById(customerId);

        if (foundCustomer.isEmpty())
            throw new AccountsException(AccountsException.class, String.format("No such customer exists with id %s", customerId), methodName);
        //deleting it
        accountsRepository.deleteAllByCustomer_CustomerId(customerId);
    }

    private Customer updateCustomerDetails(Customer oldCustomerRecord, CustomerDto newCustomerRecord) {
        String oldName = oldCustomerRecord.getName();
        String newName = newCustomerRecord.getCustomerName();

        LocalDate oldDateOfBirth = oldCustomerRecord.getDateOfBirth();
        LocalDate newDateOfBirth = newCustomerRecord.getDateOfBirth();

        int newAge = Period.between(newDateOfBirth, LocalDate.now()).getYears();

        String oldEmail = oldCustomerRecord.getEmail();
        String newEmail = newCustomerRecord.getEmail();

        String oldPhoneNumber = oldCustomerRecord.getPhoneNumber();
        String newPhoneNumber = newCustomerRecord.getPhoneNumber();

        String oldAdharNumber = oldCustomerRecord.getAdharNumber();
        String newAdharNumber = newCustomerRecord.getAdharNumber();

        String oldPanNumber = oldCustomerRecord.getPanNumber();
        String newPanNumber = newCustomerRecord.getPanNumber();

        String voterId = oldCustomerRecord.getVoterId();
        String newVoterId = newCustomerRecord.getVoterId();

        String oldDrivingLicense = oldCustomerRecord.getDrivingLicense();
        String newDrivingLicense = newCustomerRecord.getDrivingLicense();

        String oldPassportNumber = oldCustomerRecord.getPassportNumber();
        String newPassportNumber = newCustomerRecord.getPassportNumber();

        if (null!=newName && !oldName.equalsIgnoreCase(newName))
            newCustomerRecord.setCustomerName(newName);
        if (null!=newDateOfBirth && !oldDateOfBirth.equals(newDateOfBirth)) {
            newCustomerRecord.setDateOfBirth(newDateOfBirth);
            newCustomerRecord.setAge(newAge);
        }
        if (null!=newEmail && !oldEmail.equalsIgnoreCase(newEmail))
            newCustomerRecord.setEmail(newEmail);
        if (null!=newPhoneNumber && !oldPhoneNumber.equalsIgnoreCase(newPhoneNumber))
            newCustomerRecord.setCustomerName(newPhoneNumber);
        if (null!=newAdharNumber && !oldAdharNumber.equalsIgnoreCase(newAdharNumber))
            newCustomerRecord.setCustomerName(newAdharNumber);
        if (null!=newPassportNumber && !oldPassportNumber.equalsIgnoreCase(newPassportNumber))
            newCustomerRecord.setCustomerName(newPassportNumber);
        if (null!=newPanNumber && !oldPanNumber.equalsIgnoreCase(newPanNumber))
            newCustomerRecord.setCustomerName(newPanNumber);
        if (null!=newVoterId && !voterId.equalsIgnoreCase(newVoterId))
            newCustomerRecord.setCustomerName(newVoterId);
        if (null!=newDrivingLicense && !oldDrivingLicense.equalsIgnoreCase(newDrivingLicense))
            newCustomerRecord.setDrivingLicense(newDrivingLicense);

        Customer updatedCustomer = mapToCustomer(newCustomerRecord);

        //auditing does not work during update so manually set it
        updatedCustomer.setCreatedBy("Admin");
        return customerRepository.save(updatedCustomer);
    }

    private int getCreditScore(Long accountNumber) {
        ///to be done
        return 0;
    }

    private int updateCreditScore(AccountsDto accountsDto) {
        return 0;
    }

    private PageableResponseDto<AccountsDto> accountsPagination(DIRECTION sortDir,String sortBy,int pageNumber,int pageSize,Long customerId) {
        String methodName="accountsPagination(DIRECTION,String,int,int,Long) in AccountsServiceImpl";
        Sort sort = sortDir.equals(PAGE_SORT_DIRECTION_ASCENDING) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        PageableResponseDto<AccountsDto> pageableResponseDto = getAllActiveAccountsByCustomerId(customerId, pageable);
        if (pageableResponseDto.getContent().size() == 0)
            throw new BadApiRequestException(BadApiRequestException.class,
                    String.format("Customer with id %s have no accounts present", customerId),
                    methodName);

        List<AccountsDto> onlyActiveAccounts = pageableResponseDto.getContent().stream().filter(accounts -> !STATUS_BLOCKED.equals(accounts.getAccountStatus())
                && !STATUS_CLOSED.equals(accounts.getAccountStatus())).toList();

        pageableResponseDto.setContent(onlyActiveAccounts);
        return  pageableResponseDto;
    }

    private void uploadProfileImage(CustomerDto customerDto) throws IOException {
        updateValidator(null, null, customerDto, UPLOAD_PROFILE_IMAGE);
        String imageName = fIleService.uploadFile(customerDto.getCustomerImage(), IMAGE_PATH);
        Customer customer = fetchCustomerByCustomerNumber(customerDto.getCustomerId());
        customer.setImageName(imageName);
        customerRepository.save(customer);
    }


    /**
     * @param postInputRequestDto
     * @return
     */
    @Override
    public OutputDto accountSetUp(PostInputRequestDto postInputRequestDto) {
        return createAccount(postInputRequestDto);
    }

    @Override
    public OutputDto postRequestExecutor(PostInputRequestDto postInputRequestDto) throws AccountsException, CustomerException {
        String methodName = "postRequestExecutor(InputDto) in AccountsServiceImpl";
        //map
        AccountsDto accountsDto = inputToAccountsDto(postInputRequestDto);
        CustomerDto customerDto = inputToCustomerDto(postInputRequestDto);

        //Get the accountNumber & account & customer
        Long accountNumber = accountsDto.getAccountNumber();
        Accounts foundAccount;
        if (null!=accountNumber) foundAccount = fetchAccountByAccountNumber(accountNumber);

        Long customerId = customerDto.getCustomerId();
        Customer foundCustomer;
        if (null!=customerId) foundCustomer = fetchCustomerByCustomerNumber(customerId);
        //check the request type
        if (null == accountsDto.getUpdateRequest())
            throw new AccountsException(AccountsException.class, "update request field must not be blank", methodName);
        AccountsDto.UpdateRequest request = accountsDto.getUpdateRequest();
        switch (request) {
            case LEND_LOAN -> {
                //to be done...
                return OutputDto.builder().defaultMessage("Baad main karenge").build();
            }

            default -> throw new AccountsException(AccountsException.class,
                    String.format("Invalid request type %s for POST requests", request),
                    methodName);
        }
    }

    @Override
    public OutputDto getRequestExecutor(GetInputRequestDto getInputRequestDto) throws AccountsException, CustomerException {
        String methodName = "getRequestExecutor(InputDto) in AccountsServiceImpl";


        //get paging details
        int pageNumber = getInputRequestDto.getPageNumber();
        if (pageNumber < 0) throw new BadApiRequestException(BadApiRequestException.class,
                "pageNumber cant be in negative", methodName);

        int pageSize = getInputRequestDto.getPageSize();
        if (pageSize < 0)
            throw new BadApiRequestException(BadApiRequestException.class, "Page Size can't be in negative", methodName);
        pageSize = (getInputRequestDto.getPageSize() == 0) ? DEFAULT_PAGE_SIZE : getInputRequestDto.getPageSize();

        String sortBy = (null == getInputRequestDto.getSortBy()) ? "balance" : getInputRequestDto.getSortBy();
        DIRECTION sortDir = (null == getInputRequestDto.getSortDir()) ? DIRECTION.asc : getInputRequestDto.getSortDir();


        //map
        AccountsDto accountsDto = getInputToAccountsDto(getInputRequestDto);
        CustomerDto customerDto = getInputToCustomerDto(getInputRequestDto);
        //load accounts & customer
        Long accountNumber = accountsDto.getAccountNumber();
        Accounts foundAccount = null;
        if (Objects.nonNull(accountNumber)) foundAccount = fetchAccountByAccountNumber(accountNumber);

        Long customerId = customerDto.getCustomerId();
        Customer foundCustomer = null;
        if (Objects.nonNull(customerId)) foundCustomer = fetchCustomerByCustomerNumber(customerId);

        //check the request type
        if (null == accountsDto.getUpdateRequest())
            throw new AccountsException(AccountsException.class, "update request field must not be blank", methodName);
        AccountsDto.UpdateRequest request = accountsDto.getUpdateRequest();
        switch (request) {
            case GET_CREDIT_SCORE -> {
                getCreditScore(accountNumber);
                //to be done after implementing credit card microservice
                return OutputDto.builder().defaultMessage("Baad main karenge").build();
            }
            case GET_ACC_INFO -> {
                return getAccountInfo(accountNumber);
            }
            case GET_ALL_ACC -> {
                String locality = String.format("Inside switch ,for GET_ALL_ACC case under method %s", methodName);
                if (null == foundCustomer) throw new CustomerException(CustomerException.class, locality, methodName);

                //validate the genuineness of sorting fields
                Set<String> allPageableFieldsOfAccounts = getAllPageableFieldsOfAccounts();
                if (!allPageableFieldsOfAccounts.contains(sortBy))
                    throw new BadApiRequestException(BadApiRequestException.class,
                            String.format("%s is not a valid field of account", sortBy), String.format("Inside %s of %s", locality, methodName));
                //paging & sorting
                PageableResponseDto<AccountsDto> pageableResponseDto=accountsPagination(sortDir,sortBy,pageNumber,pageSize,customerId);

                return OutputDto.builder()
                        .customer(mapToCustomerOutputDto(mapToCustomerDto(foundCustomer)))
                        .accountsListPages(pageableResponseDto)
                        .defaultMessage(String.format("Fetched all accounts for customer id:%s", customerId))
                        .build();
            }
            default ->
                    throw new AccountsException(AccountsException.class, String.format("Invalid request type %s for GET request", request), methodName);
        }
    }

    @Override
    public OutputDto putRequestExecutor(PutInputRequestDto putInputRequestDto) throws AccountsException, CustomerException, IOException {
        String methodName = "putRequestExecutor(InputDto) in AccountsServiceImpl";
        //map
        AccountsDto accountsDto = putInputRequestToAccountsDto(putInputRequestDto);
        CustomerDto customerDto = putInputRequestToCustomerDto(putInputRequestDto);

        //get paging details
        int pageNumber = putInputRequestDto.getPageNumber();
        if (pageNumber < 0) throw new BadApiRequestException(BadApiRequestException.class,
                "pageNumber cant be in negative", methodName);

        int pageSize = putInputRequestDto.getPageSize();
        if (pageSize < 0)
            throw new BadApiRequestException(BadApiRequestException.class, "Page Size can't be in negative", methodName);
        pageSize = (putInputRequestDto.getPageSize() == 0) ? DEFAULT_PAGE_SIZE : putInputRequestDto.getPageSize();

        String sortBy = (null == putInputRequestDto.getSortBy()) ? "balance" : putInputRequestDto.getSortBy();
        DIRECTION sortDir = (null == putInputRequestDto.getSortDir()) ? DIRECTION.asc : putInputRequestDto.getSortDir();


        //Get the accountNumber & account & customer
        Long accountNumber = accountsDto.getAccountNumber();
        Accounts foundAccount = null;
        if (Objects.nonNull(accountNumber)) foundAccount = fetchAccountByAccountNumber(accountNumber);

        Long customerId = customerDto.getCustomerId();
        Customer foundCustomer = null;
        if (Objects.nonNull(customerId)) foundCustomer = fetchCustomerByCustomerNumber(customerId);

        //check the request type
        if (null == accountsDto.getUpdateRequest())
            throw new AccountsException(AccountsException.class, "update request field must not be blank", methodName);
        AccountsDto.UpdateRequest request = accountsDto.getUpdateRequest();
        switch (request) {
            case ADD_ACCOUNT -> {
                return createAccountForAlreadyCreatedUser(customerDto.getCustomerId(), mapToAccounts(accountsDto), accountsDto);
            }
            case UPDATE_HOME_BRANCH -> {
                Accounts updatedAccount = updateHomeBranch(accountsDto, foundAccount);

                return OutputDto.builder()
                        .customer(mapToCustomerOutputDto(mapToCustomerDto(updatedAccount.getCustomer())))
                        .accounts(mapToAccountsOutputDto(mapToAccountsDto(updatedAccount)))
                        .defaultMessage(String.format("Home branch is changed from %s to %s for customer with id %s",
                                foundAccount.getHomeBranch(), accountsDto.getHomeBranch(), foundAccount.getCustomer().getCustomerId()))
                        .build();
            }
            case UPDATE_CREDIT_SCORE -> {
                //updateCreditScore(accountsDto);
                return OutputDto.builder().defaultMessage("Baad main karenge").build();
            }
            case UPLOAD_CUSTOMER_IMAGE -> {
                uploadProfileImage(customerDto);
                return OutputDto.builder().customer(mapToCustomerOutputDto(mapToCustomerDto(foundCustomer)))
                        .defaultMessage(String.format("Profile Image for customer with id:%s has been updated successfully", customerDto.getCustomerId()))
                        .build();
            }
            case INC_TRANSFER_LIMIT -> {
                Accounts accountWithUpdatedLimit = increaseTransferLimit(accountsDto, foundAccount);
                return OutputDto.builder()
                        .customer(mapToCustomerOutputDto(customerDto))
                        .accounts(mapToAccountsOutputDto(mapToAccountsDto(accountWithUpdatedLimit)))
                        .defaultMessage(String.format("Transfer Limit has been increased from %s to %s", foundAccount.getTransferLimitPerDay(), accountWithUpdatedLimit.getTransferLimitPerDay()))
                        .build();
            }
            case CLOSE_ACC -> {
                closeAccount(foundAccount);
                return OutputDto.builder()
                        .defaultMessage(String.format("Account with id %s is successfully closed", accountsDto.getAccountNumber()))
                        .build();
            }
            case RE_OPEN_ACC -> {
                unCloseAccount(foundAccount);
                return OutputDto.builder().defaultMessage(String.format("Account with id %s has been reopened ", accountNumber)).build();
            }
            case BLOCK_ACC -> {
                //Note: account once blocked , no operations can be performed on it not even get
                //only authority reserves the right to unblock it
                blockAccount(foundAccount);
                return OutputDto.builder().defaultMessage(String.format("Account with id %s has been blocked", accountNumber)).build();
            }
            case INC_APPROVED_LOAN_LIMIT -> {
                //to be done.....

                return OutputDto.builder().defaultMessage("BAAD MAIN KARNGE BSDK").build();
            }
            case UPDATE_CUSTOMER_DETAILS -> {
                String location = String.format("Inside UPDATE_CUSTOMER_DETAILS in %s", methodName);
                if (Objects.isNull(foundCustomer)) throw new CustomerException(CustomerException.class,
                        "Please specify a customer id to update details", location);
                CustomerDto updatedCustomerDto = mapToCustomerDto(updateCustomerDetails(foundCustomer, customerDto));
                PageableResponseDto<AccountsDto> pageableResponseDto=accountsPagination(sortDir,sortBy,pageNumber,pageSize,customerId);


                return OutputDto.builder()
                        .customer(mapToCustomerOutputDto(updatedCustomerDto))
                        .accountsListPages(pageableResponseDto)
                        .defaultMessage(String.format("Customer with id %s has been updated",customerId)).build();
            }
            default -> throw new AccountsException(AccountsException.class,
                    String.format("Invalid request type %s for PUT request", request), methodName);
        }
    }

    @Override
    public OutputDto deleteRequestExecutor(DeleteInputRequestDto deleteInputRequestDto) throws AccountsException {
        String methodName = "requestExecutor(InputDto) in AccountsServiceImpl";
        //map
        AccountsDto accountsDto = deleteRequestInputToAccountsDto(deleteInputRequestDto);
        CustomerDto customerDto = deleteInputRequestToCustomerDto(deleteInputRequestDto);
        //check the request type
        if (null == accountsDto.getUpdateRequest())
            throw new AccountsException(AccountsException.class, "update request field must not be blank", methodName);
        AccountsDto.UpdateRequest request = accountsDto.getUpdateRequest();
        switch (request) {
            case DELETE_ACC -> {
                Long accountNumber = accountsDto.getAccountNumber();
                deleteAccount(accountNumber);
                return OutputDto.builder().defaultMessage(String.format("Account with id %s is successfully deleted", accountNumber)).build();
            }
            case DELETE_ALL_ACC -> {
                deleteAllAccountsByCustomer(customerDto.getCustomerId());
                return OutputDto.builder()
                        .defaultMessage(String.format("All accounts that belonged to customer with id %s has been deleted",
                                customerDto.getCustomerId())).build();
            }
            default -> throw new AccountsException(AccountsException.class,
                    String.format("Invalid request type %s for DELETE request", request), methodName);
        }
    }
}
