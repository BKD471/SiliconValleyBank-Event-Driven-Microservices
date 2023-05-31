package com.example.accountsservices.service.impl;

import com.example.accountsservices.dto.*;
import com.example.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BadRequestException;
import com.example.accountsservices.exception.CustomerException;
import com.example.accountsservices.helpers.MapperHelper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.CustomerRepository;
import com.example.accountsservices.service.AbstractAccountsService;
import com.example.accountsservices.helpers.BranchCodeRetrieverHelper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.accountsservices.helpers.MapperHelper.*;


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
    private final Accounts.AccountStatus STATUS_BLOCKED = Accounts.AccountStatus.BLOCKED;
    private final Accounts.AccountStatus STATUS_OPEN = Accounts.AccountStatus.OPEN;
    private final Accounts.AccountStatus STATUS_CLOSED = Accounts.AccountStatus.CLOSED;
    public static final String REQUEST_TO_BLOCK = "BLOCK";
    private final String INIT = "INIT";
    private final String UPDATE = "UPDATE";

    @Value("${customer.profile.images.path}")
    private String IMAGE_PATH;

    private enum ValidateType {
        UPDATE_CASH_LIMIT, UPDATE_HOME_BRANCH, GENERATE_CREDIT_SCORE, UPDATE_CREDIT_SCORE,
        UPLOAD_PROFILE_IMAGE, CLOSE_ACCOUNT, RE_OPEN_ACCOUNT,
        DISPLAY_PROFILE_IMAGE, BLOCK_ACCOUNT, CREATE_ACC, ADD_ACC
    }

    private final ValidateType UPDATE_CASH_LIMIT = ValidateType.UPDATE_CASH_LIMIT;
    private final ValidateType UPDATE_HOME_BRANCH = ValidateType.UPDATE_HOME_BRANCH;
    private final ValidateType CLOSE_ACCOUNT = ValidateType.CLOSE_ACCOUNT;
    private final ValidateType RE_OPEN_ACCOUNT = ValidateType.RE_OPEN_ACCOUNT;
    private final ValidateType BLOCK_ACCOUNT = ValidateType.BLOCK_ACCOUNT;
    private final ValidateType CREATE_ACCOUNT = ValidateType.CREATE_ACC;
    private final ValidateType ADD_ACCOUNT = ValidateType.ADD_ACC;
    private final ValidateType UPLOAD_PROFILE_IMAGE = ValidateType.UPLOAD_PROFILE_IMAGE;
    private final ValidateType DISPLAY_PROFILE_IMAGE=ValidateType.DISPLAY_PROFILE_IMAGE;
    private final Double FIlE_SIZE_TO_MB_CONVERTER_FACTOR=0.00000095367432;

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

    private Customer processCustomerInformation(Customer customer) {
        String methodName = "processCustomerInformation(Customer) in AccountsServiceIMpl";
        //set customer age from dob
        LocalDate dob = customer.getDateOfBirth();
        int age = Period.between(dob, LocalDate.now()).getYears();
        customer.setAge(age);
        return customer;
    }

    private OutputDto createAccount(PostInputRequestDto postInputRequestDto) throws AccountsException {
        Accounts account = inputToAccounts(postInputRequestDto);
        Customer customer = inputToCustomer(postInputRequestDto);
        account.setCustomer(customer);

        updateValidator(account, mapToAccountsDto(account), null, CREATE_ACCOUNT);

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

        return MapperHelper.mapToOutPutDto(mapToCustomerDto(savedCustomer),
                mapToAccountsDto(savedCustomer.getAccounts().get(0))
                , String.format("Account with id %s is created for customer %s",
                        accountNumber, savedCustomer.getCustomerId()));
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
        return mapToOutPutDto(mapToCustomerDto(customer.get()),
                mapToAccountsDto(savedAccount), String.format("New account with id %s is created " +
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
        return mapToOutPutDto(mapToCustomerDto(foundCustomer),
                mapToAccountsDto(foundAccount), String.format("Retrieved info about account with id: %s",
                        foundAccount.getAccountNumber()));
    }

    private List<AccountsDto> getAllActiveAccountsByCustomerId(Long customerId) throws AccountsException {
        String methodName = "getAllAccountsByCustomerId(Long) in AccountsServiceImpl";
        Optional<List<Accounts>> allAccounts = accountsRepository.findAllByCustomer_CustomerId(customerId);
        if (allAccounts.isEmpty())
            throw new AccountsException(AccountsException.class, String.format("No such accounts present with this customer %s", customerId), methodName);
        return allAccounts.get().stream().filter(accounts -> !STATUS_BLOCKED.equals(accounts.getAccountStatus())
                        && !STATUS_CLOSED.equals(accounts.getAccountStatus())).
                map(MapperHelper::mapToAccountsDto).collect(Collectors.toList());
    }

    private Boolean updateValidator(Accounts accounts, AccountsDto accountsDto, CustomerDto customerDto, ValidateType request) throws AccountsException {
        String methodName = "updateValidator(Accounts,ValidateType) in AccountsServiceImpl";
        String location = "";
        switch (request) {
            case CREATE_ACC -> {
                location = "Inside CREATE_ACC";
                //check whether such account owner is already present
                Optional<List<Accounts>> accountsList = Optional.of(accountsRepository.findAll());
                //if no accounts by far then certainly we can add
                if (accountsList.isEmpty()) return true;

                String adharNumber = accounts.getCustomer().getAdharNumber();
                boolean isNotPossible = accountsList.get().stream().anyMatch(acc -> adharNumber.equalsIgnoreCase(acc.getCustomer().getAdharNumber()));
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
                if (null == customerDto.getCustomerImage()) throw new BadRequestException(BadRequestException.class,
                        "Please provide image", String.format("%s of %s", methodName, location));
                if (customerDto.getCustomerImage().getSize()*FIlE_SIZE_TO_MB_CONVERTER_FACTOR <= 0.0 || customerDto.getCustomerImage().getSize()*FIlE_SIZE_TO_MB_CONVERTER_FACTOR > 100.0)
                    throw new BadRequestException(BadRequestException.class,
                            "Your file is either corrupted or you are exceeding the max size of 100mb",
                            String.format("%s of %s", methodName, location));
            }
            case DISPLAY_PROFILE_IMAGE -> {
                location="Inside Display Profile Image";
                if(null==customerDto.getImageName()) throw new BadRequestException(BadRequestException.class,
                        "No profile pics available for this customer",String.format("Inside %s of %s"));

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
        //but authorty reserves right to scrutiny any ongoing loan emis

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
        Optional<List<Accounts>> foundCustomer = accountsRepository.
                findAllByCustomer_CustomerId(customerId);
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

        if (Objects.nonNull(newName) && !oldName.equalsIgnoreCase(newName))
            newCustomerRecord.setCustomerName(newName);
        if (Objects.nonNull(newDateOfBirth) && !oldDateOfBirth.equals(newDateOfBirth)) {
            newCustomerRecord.setDateOfBirth(newDateOfBirth);
            newCustomerRecord.setAge(newAge);
        }
        if (Objects.nonNull(newEmail) && !oldEmail.equalsIgnoreCase(newEmail))
            newCustomerRecord.setEmail(newEmail);
        if (Objects.nonNull(newPhoneNumber) && !oldPhoneNumber.equalsIgnoreCase(newPhoneNumber))
            newCustomerRecord.setCustomerName(newPhoneNumber);
        if (Objects.nonNull(newAdharNumber) && !oldAdharNumber.equalsIgnoreCase(newAdharNumber))
            newCustomerRecord.setCustomerName(newAdharNumber);
        if (Objects.nonNull(newPassportNumber) && !oldPassportNumber.equalsIgnoreCase(newPassportNumber))
            newCustomerRecord.setCustomerName(newPassportNumber);
        if (Objects.nonNull(newPanNumber) && !oldPanNumber.equalsIgnoreCase(newPanNumber))
            newCustomerRecord.setCustomerName(newPanNumber);
        if (Objects.nonNull(newVoterId) && !voterId.equalsIgnoreCase(newVoterId))
            newCustomerRecord.setCustomerName(newVoterId);
        if (Objects.nonNull(newDrivingLicense) && !oldDrivingLicense.equalsIgnoreCase(newDrivingLicense))
            newCustomerRecord.setDrivingLicense(newDrivingLicense);

        Customer updatedCustomer = mapToCustomer(newCustomerRecord);

        //aduiting doesnot work during update so manually set it
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


    private void uploadProfileImage(CustomerDto customerDto) throws IOException {
        updateValidator(null, null, customerDto, UPLOAD_PROFILE_IMAGE);
        String imageName = fIleService.uploadFile(customerDto.getCustomerImage(), IMAGE_PATH);
        Customer customer = fetchCustomerByCustomerNumber(customerDto.getCustomerId());
        customer.setImageName(imageName);
        customerRepository.save(customer);
    }

    private void loadProfileImage(Customer foundCustomer, HttpServletResponse response) throws IOException {
        updateValidator(null,null,mapToCustomerDto(foundCustomer),DISPLAY_PROFILE_IMAGE);
        InputStream resource=fIleService.getResource(IMAGE_PATH,foundCustomer.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

    @Override
    public OutputDto postRequestExecutor(PostInputRequestDto postInputRequestDto) throws AccountsException, CustomerException, IOException {
        String methodName = "postRequestExecutor(InputDto) in AccountsServiceImpl";
        //map
        AccountsDto accountsDto = inputToAccountsDto(postInputRequestDto);
        CustomerDto customerDto = inputToCustomerDto(postInputRequestDto);

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
            case CREATE_ACC -> {
                return createAccount(postInputRequestDto);
            }
            case LEND_LOAN -> {
                //to be done...
                return new OutputDto("Baad main karenge");
            }

            default -> throw new AccountsException(AccountsException.class,
                    String.format("Invalid request type %s for POST requests", request),
                    methodName);
        }
    }

    @Override
    public OutputDto getRequestExecutor(GetInputRequestDto getInputRequestDto) throws AccountsException, CustomerException, IOException {
        String methodName = "getRequestExecutor(InputDto) in AccountsServiceImpl";
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
                return new OutputDto("Baad main karnge");
            }
            case GET_ACC_INFO -> {
                return getAccountInfo(accountNumber);
            }
            case GET_ALL_ACC -> {
                String locality = String.format("Inside switch ,for GET_ALL_ACC case under method %s", methodName);
                if (null==foundCustomer) throw new CustomerException(CustomerException.class, locality, methodName);

                List<AccountsDto> listOfAccounts = getAllActiveAccountsByCustomerId(customerId);
                if (listOfAccounts.size() == 0)
                    return new OutputDto(String.format("Customer with id %s have no accounts present", customerId));
                return new OutputDto(mapToCustomerOutputDto(mapToCustomerDto(foundCustomer)), listOfAccounts,
                        String.format("Fetched all accounts for customer id:%s", customerId));
            }
            case GET_CUSTOMER_IMAGE -> {
                loadProfileImage(foundCustomer,getInputRequestDto.getResponse());
                return new OutputDto(String.format("Serving you the image of customer:%s",customerId));
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
                return MapperHelper.mapToOutPutDto(mapToCustomerDto(updatedAccount.getCustomer()), mapToAccountsDto(updatedAccount),
                        String.format("Home branch for is changed from %s to %s for customer with id %s",
                                foundAccount.getHomeBranch(), accountsDto.getHomeBranch(),
                                foundAccount.getCustomer().getCustomerId()));
            }
            case UPDATE_CREDIT_SCORE -> {
                //updateCreditScore(accountsDto);
                return new OutputDto("Baad main karenge");
            }
            case UPLOAD_CUSTOMER_IMAGE -> {
                uploadProfileImage(customerDto);
                return new OutputDto(String.format("Profile Image for customer with id:%s has been uploaded successfully", customerDto.getCustomerId()));
            }
            case INC_TRANSFER_LIMIT -> {
                Accounts accountWithUpdatedLimit = increaseTransferLimit(accountsDto, foundAccount);
                return mapToOutPutDto(customerDto, mapToAccountsDto(accountWithUpdatedLimit),
                        String.format("Transfer Limit has been increased from %s to %s", foundAccount.getTransferLimitPerDay(), accountWithUpdatedLimit.getTransferLimitPerDay()));
            }
            case CLOSE_ACC -> {
                closeAccount(foundAccount);
                return new OutputDto(String.format("Account with id %s is successfully closed", accountsDto.getAccountNumber()));
            }
            case RE_OPEN_ACC -> {
                unCloseAccount(foundAccount);
                return new OutputDto(String.format("Account with id %s has been reOpened", accountNumber));
            }
            case BLOCK_ACC -> {
                //Note: acount once blocked , no operations can be performed on it not even get
                //only authority reserves the right to unblock it
                blockAccount(foundAccount);
                return new OutputDto(String.format("Account with id %s is successfully blocked", accountNumber));
            }
            case INC_APPROVED_LOAN_LIMIT -> {
                //to be done.....

                return new OutputDto("Baad main karenge");
            }
            case UPDATE_CUSTOMER_DETAILS -> {
                String location = String.format("Inside UPDATE_CUSTOMER_DETAILS in %s", methodName);
                if (Objects.isNull(foundCustomer)) throw new CustomerException(CustomerException.class,
                        "Please specify a customer id to update details", location);

                CustomerDto updatedCustomerDto = mapToCustomerDto(updateCustomerDetails(foundCustomer, customerDto));
                List<AccountsDto> listOfAccounts = getAllActiveAccountsByCustomerId(customerId);
                return new OutputDto(mapToCustomerOutputDto(updatedCustomerDto), listOfAccounts,
                        String.format("Customer With id %s has been updated", customerId));
            }
            default ->
                    throw new AccountsException(AccountsException.class, String.format("Invalid request type %s for PUT request", request), methodName);
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
                return new OutputDto(String.format("Account with id %s is successfully deleted", accountNumber));
            }
            case DELETE_ALL_ACC -> {
                deleteAllAccountsByCustomer(customerDto.getCustomerId());
                return new OutputDto(String.format("All accounts that belonged to customer with id %s has " +
                        "been deleted", customerDto.getCustomerId()));
            }
            default -> throw new AccountsException(AccountsException.class,
                    String.format("Invalid request type %s for DELETE request", request), methodName);
        }
    }
}
