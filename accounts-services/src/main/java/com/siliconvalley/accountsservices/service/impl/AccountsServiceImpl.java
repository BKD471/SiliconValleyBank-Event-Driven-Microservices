package com.siliconvalley.accountsservices.service.impl;

import com.siliconvalley.accountsservices.dto.baseDtos.AccountsDto;
import com.siliconvalley.accountsservices.dto.baseDtos.CustomerDto;
import com.siliconvalley.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.responseDtos.PageableResponseDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.BadApiRequestException;
import com.siliconvalley.accountsservices.exception.CustomerException;
import com.siliconvalley.accountsservices.exception.RolesException;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.Customer;
import com.siliconvalley.accountsservices.model.Role;
import com.siliconvalley.accountsservices.repository.IAccountsRepository;
import com.siliconvalley.accountsservices.repository.ICustomerRepository;
import com.siliconvalley.accountsservices.repository.IRoleRepository;
import com.siliconvalley.accountsservices.service.AbstractService;
import com.siliconvalley.accountsservices.service.IAccountsService;
import com.siliconvalley.accountsservices.service.IImageService;
import com.siliconvalley.accountsservices.service.IValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.*;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.AccountsValidateType.GET_ALL_ACC;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.AccountsValidateType.UPDATE_CUSTOMER_DETAILS;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.DIRECTION.asc;
import static com.siliconvalley.accountsservices.helpers.CodeRetrieverHelper.getBranchCode;
import static com.siliconvalley.accountsservices.helpers.MapperHelper.*;
import static com.siliconvalley.accountsservices.helpers.PagingHelper.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.CollectionUtils.isEmpty;


/**
 * @parent AccountsService
 * @class AccountsServiceImpl
 * @fields accountsRepository
 * @fieldTypes AccountsRepository
 * @overridenMethods createAccounts, getAccountByCustomerId,
 * updateAccountByCustomerIdAndAccountNumber,updateBeneficiaryDetails
 * @specializedMethods None
 */
@Slf4j
@Service("accountsServicePrimary")
public class AccountsServiceImpl extends AbstractService implements IAccountsService {
    private final IAccountsRepository accountsRepository;
    private final IRoleRepository roleRepository;
    private final ICustomerRepository customerRepository;
    private final IImageService fIleService;
    private final IValidationService validationService;
    private final String NORMAL_ROLE_ID;
    private final PasswordEncoder passwordEncoder;
    private final String IMAGE_PATH;
    private final String UPDATE = "UPDATE";

    /**
     * @paramType AccountsRepository
     * @returnType NA
     */
    public AccountsServiceImpl(final IAccountsRepository accountsRepository,
                               final ICustomerRepository customerRepository,
                               final IRoleRepository roleRepository,
                               final IValidationService validationService,
                               final PasswordEncoder passwordEncoder,
                               @Qualifier("fileServicePrimary") final IImageService fIleService,
                               @Value("${customer.profile.images.path}") final String IMAGE_PATH,
                               @Value("${normal.role.id}") final String NORMAL_ROLE_ID) {
        super(accountsRepository, customerRepository);
        this.accountsRepository = accountsRepository;
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.fIleService = fIleService;
        this.validationService = validationService;
        this.passwordEncoder = passwordEncoder;
        this.IMAGE_PATH = IMAGE_PATH;
        this.NORMAL_ROLE_ID = NORMAL_ROLE_ID;
    }


    private Accounts processAccountInit(final Accounts accounts, final String req) throws AccountsException {
        log.debug("<-------processAccountInit(Accounts accounts, String req) AccountsServiceImpl started------------------------------------------------------" +
                "--------------------------------------------------------------------------------------------------------------------------" +
                "------------>");
        final String methodName = "processAccountInit(Accounts,String) in AccountsServiceImpl";
        //If request is adding another accounts for a customer already have an account
        //there should not be two accounts with  same accountType in same homeBranch
        if (UPDATE.equalsIgnoreCase(req))
            IValidationService.checkConflictingAccountUpdateConditionForBranch(accounts, methodName);

        final String accountNumber = UUID.randomUUID().toString();
        accounts.setAccountNumber(accountNumber);

        accounts.setBalance(BigDecimal.valueOf(0L));
        accounts.setBranchCode(getBranchCode(accounts.getHomeBranch()));
        accounts.setAccountStatus(STATUS_OPENED);
        accounts.setTransferLimitPerDay(BigDecimal.valueOf(100000L));
        accounts.setRateOfInterest(5.5);

        accounts.setTotLoanIssuedSoFar(BigDecimal.valueOf(0L));
        accounts.setTotalOutStandingAmountPayableToBank(BigDecimal.valueOf(0L));
        accounts.setAnyActiveLoans(false);
        accounts.setApprovedLoanLimitBasedOnCreditScore(BigDecimal.valueOf(0L));
        log.debug("<------processAccountInit(Accounts, String) AccountsServiceImpl ended -------------------------------------------------------------------" +
                "----------------------------------------------------------------------------------------------------------------->");
        return accounts;
    }

    private Customer processCustomerInformation(final Customer customer) {
        log.debug("<-------processCustomerInformation(Customer) AccountsServiceImpl started--------------------------------------------------------------------" +
                "------------------------------------------------------------------------------------------------------------------------->");
        final String methodName = "processCustomerInformation(Customer)";
        final LocalDate dob = customer.getDateOfBirth();
        final int age = Period.between(dob, LocalDate.now()).getYears();
        customer.setAge(age);

        final String customerId = UUID.randomUUID().toString();
        customer.setCustomerId(customerId);

        //set role
        final Optional<Role> roles = roleRepository.findById(NORMAL_ROLE_ID);
        if (roles.isEmpty()) throw new RolesException(RolesException.class, "No roles found", methodName);
        customer.setRoles(new HashSet<>(Collections.singletonList(roles.get())));

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        log.debug("<----------------processCustomerInformation(Customer) AccountsServiceImpl ended ------------------------------------------------------------" +
                "------------------------------------------------------------------------------------------------------------------------->");
        return customer;
    }

    private OutputDto createAccount(final PostInputRequestDto postInputRequestDto) throws AccountsException {
        log.debug("<---------createAccount(PostInputRequestDto)started AccountsServiceImpl --------------------------------------------------------------------------" +
                "----------------------------------------------------------------------------------------------------------------------->");
        final Accounts account = inputToAccounts(postInputRequestDto);
        final Customer customer = inputToCustomer(postInputRequestDto);
        account.setCustomer(customer);
        validationService.accountsUpdateValidator(account, null, CREATE_ACCOUNT);

        final Accounts processedAccount = processAccountInit(account, "INIT");
        final Customer processedCustomer = processCustomerInformation(customer);

        final Set<Accounts> listOfAccounts = new LinkedHashSet<>();
        listOfAccounts.add(processedAccount);
        processedCustomer.setAccounts(listOfAccounts);
        processedAccount.setCustomer(processedCustomer);

        final Customer savedCustomer = customerRepository.save(processedCustomer);
        final String accountNumber = savedCustomer.getAccounts().stream().toList().get(0).getAccountNumber();
        log.debug("<------------createAccount(PostInputRequestDto) AccountsServiceImpl ended --------------------------------------------------------------" +
                "-------------------------------------------------------------------------------------------------------------------->");
        return OutputDto.builder()
                .customer(mapToCustomerOutputDto(mapToCustomerDto(savedCustomer)))
                .accounts(mapToAccountsOutputDto(mapToAccountsDto(savedCustomer.getAccounts().stream().toList().get(0))))
                .defaultMessage(String.format("Account with id %s is created for customer %s", accountNumber, savedCustomer.getCustomerId()))
                .build();
    }


    private OutputDto createAccountForAlreadyCreatedUser(final String customerId, final Accounts loadAccount, final AccountsDto accountsDto) throws AccountsException {
        log.debug("<-------------- createAccountForAlreadyCreatedUser(long,Accounts,AccountsDto) AccountsServiceImpl started -----------------------------" +
                "------------------------------------------------------------------------------------------------------>--->");
        final String methodName = "createAccountForAlreadyCreatedUser(long,InoutDto) in AccountsServiceImpl";
        final Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) throw new AccountsException(AccountsException.class,
                String.format("No such customers with id %s found", customerId),
                methodName);

        loadAccount.setCustomer(customer.get());
        validationService.accountsUpdateValidator(loadAccount, null, ADD_ACCOUNT);
        final Accounts accounts = mapToAccounts(accountsDto);
        accounts.setCustomer(customer.get());
        Accounts processedAccount = processAccountInit(accounts, UPDATE);
        final Accounts savedAccount = accountsRepository.save(processedAccount);
        log.debug("<-------createAccountForAlreadyCreatedUser(long,Accounts,AccountsDto) AccountsServiceImpl ended -----------------------------------" +
                "--------------------------------------------------------------------------------------------------------------------->");

        return OutputDto.builder()
                .customer(mapToCustomerOutputDto(mapToCustomerDto(customer.get())))
                .accounts(mapToAccountsOutputDto(mapToAccountsDto(savedAccount)))
                .defaultMessage(String.format("New account with id %s is created for customer with id:%s", savedAccount.getAccountNumber(), customerId))
                .build();
    }

    /**
     * @param accountNumber accountNumber
     * @paramType long
     * @returnType AccountsDto
     */
    private OutputDto getAccountInfo(final String accountNumber) throws AccountsException {
        log.debug("<-----------getAccountInfo(long) AccountsServiceImpl started --------------------------------------------------------------------------" +
                "--------------------------------------------------------------------------------------------------------------------->");
        final Accounts foundAccount = fetchAccountByAccountNumber(accountNumber);
        final Customer foundCustomer = foundAccount.getCustomer();

        log.debug("<-----------getAccountInfo(long) AccountsServiceImpl ended ---------------------------------------------------------------------------" +
                "----------------------------------------------------------------------------------------------------------------->");
        return OutputDto.builder()
                .customer(mapToCustomerOutputDto(mapToCustomerDto(foundCustomer)))
                .accounts(mapToAccountsOutputDto(mapToAccountsDto(foundAccount)))
                .defaultMessage(String.format("Retrieved info about account with id: %s", foundAccount.getAccountNumber()))
                .build();
    }

    private PageableResponseDto<AccountsDto> getAllActiveAccountsByCustomerId(final String customerId, final Pageable pageable) throws AccountsException {
        log.debug("<-----------------getAllActiveAccountsByCustomerId(long,Pageable) AccountsServiceImpl started -----------------------------------" +
                "----------------------------------------------------------------------------------------------------------------->");
        final String methodName = "getAllAccountsByCustomerId(long) in AccountsServiceImpl";
        final Optional<Page<Accounts>> allPagedAccounts = accountsRepository.findAllByCustomer_CustomerId(customerId, pageable);
        if (allPagedAccounts.isEmpty())
            throw new AccountsException(AccountsException.class,
                    String.format("No such accounts present with this customer %s", customerId), methodName);
        log.debug("<------------------getAllActiveAccountsByCustomerId(long,Pageable) AccountsServiceImpl ended ----------------------------------------------------" +
                "------------------------------------------------------------------------------------------------------------------>");
        return getPageableResponse(allPagedAccounts.get(), AccountsDto.class);
    }


    private Accounts updateHomeBranch(final AccountsDto accountsDto, final Accounts accounts) throws AccountsException {
        log.debug("<-------------updateHomeBranch(AccountsDto,Accounts) AccountsServiceImpl started --------------------------------------------------------" +
                "-------------------------------------------------------------------------------------------------------------------->");
        final AllConstantHelpers.Branch oldHomeBranch = accounts.getHomeBranch();
        final AllConstantHelpers.Branch newHomeBranch = accountsDto.getHomeBranch();
        Accounts savedUpdatedAccount = accounts;

        validationService.accountsUpdateValidator(accounts, null, UPDATE_HOME_BRANCH);
        if (nonNull(newHomeBranch) && !newHomeBranch.equals(oldHomeBranch)) {
            accounts.setHomeBranch(newHomeBranch);
            accounts.setBranchCode(getBranchCode(newHomeBranch));
            savedUpdatedAccount = accountsRepository.save(accounts);
        }
        log.debug("<-----------------updateHomeBranch(AccountsDto accountsDto, Accounts accounts) AccountsServiceImpl ended --------------------------------" +
                "----------------------------------------------------------------------------------------------------------------------->");
        return savedUpdatedAccount;
    }

    private Accounts increaseTransferLimit(final AccountsDto accountsDto, final Accounts accounts) throws AccountsException {
        log.debug("<------------increaseTransferLimit(AccountsDto,Accounts) AccountsServiceImpl started --------------------------------------------------" +
                "------------------------------------------------------------------------------------------------------------------->");
        final String methodName = "increaseTransferLimit(AccountsDto,Accounts) in AccountsServiceImpl";
        final BigDecimal oldCashLimit = accounts.getTransferLimitPerDay();
        final BigDecimal newCashLimit = accountsDto.getTransferLimitPerDay();

        Accounts savedAccount = accounts;
        BiPredicate<BigDecimal, BigDecimal> checkForCashLimitRevision = (newLimit, oldLimit) -> newLimit.compareTo(BigDecimal.ZERO) == 0
                && newLimit.compareTo(oldLimit) != 0;

        validationService.accountsUpdateValidator(accounts, null, UPDATE_CASH_LIMIT);
        if (checkForCashLimitRevision.test(newCashLimit, oldCashLimit)) {
            accounts.setTransferLimitPerDay(newCashLimit);
            savedAccount = accountsRepository.save(accounts);
        }
        log.debug("<----------increaseTransferLimit(AccountsDto,Accounts) AccountsServiceImpl ended ------------------------------------------------------" +
                "--------------------------------------------------------------------------------------------------------------------->");
        return savedAccount;
    }

    private void blockAccount(final Accounts foundAccount) throws AccountsException {
        //Note: block is very urgent so no prior validation is required  for ongoing loan
        //but authority reserves right to scrutiny any ongoing loan Emi
        log.debug("<----------blockAccount(Accounts) AccountsServiceImpl started --------------------------------------------------------------------------" +
                "------------------------------------------------------------------------------------------------------------------->");
        validationService.accountsUpdateValidator(foundAccount, null, BLOCK_ACCOUNT);
        //Block it
        foundAccount.setAccountStatus(STATUS_BLOCKED);
        //save it
        accountsRepository.save(foundAccount);
        log.debug("<-----------blockAccount(Accounts) AccountsServiceImpl ended ----------------------------------------------------------------------------" +
                "--------------------------------------------------------------------------------------------------------------------->");
    }

    private void closeAccount(final Accounts foundAccount) throws AccountsException {
        log.debug("<--------closeAccount(Accounts) AccountsServiceImpl started ----------------------------------------------------------------------------" +
                "----------------------------------------------------------------------------------------------------------------------->");
        validationService.accountsUpdateValidator(foundAccount,null, CLOSE_ACCOUNT);
        foundAccount.setAccountStatus(STATUS_CLOSED);
        accountsRepository.save(foundAccount);
        log.debug("<----------closeAccount(Accounts) AccountsServiceImpl ended ----------------------------------------------------------------------------" +
                "---------------------------------------------------------------------------------------------------------------------->");
    }

    private void unCloseAccount(final Accounts account) throws AccountsException {
        log.debug("<-----------------unCloseAccount(Accounts) AccountsServiceImpl started ----------------------------------------------------------------------" +
                "-------------------------------------------------------------------------------------------------------->");
        validationService.accountsUpdateValidator(account, null, RE_OPEN_ACCOUNT);
        account.setAccountStatus(STATUS_OPENED);
        accountsRepository.save(account);
        log.debug("<------------unCloseAccount(Accounts) AccountsServiceImpl ended -----------------------------------------------------------------------" +
                "--------------------------------------------------------------------------------------------------------------------->");
    }

    private void deleteAccount(final String accountNumber) throws AccountsException {
        log.debug("<-----------deleteAccount(long) AccountsServiceImpl started -------------------------------------------------------------------------" +
                "------------------------------------------------------------------------------------------------------------------>");
        //checking whether account exist or not
        fetchAccountByAccountNumber(accountNumber);
        accountsRepository.deleteByAccountNumber(accountNumber);
        log.debug("<-------------deleteAccount(long) AccountsServiceImpl ended -------------------------------------------------------------------------" +
                "------------------------------------------------------------------------------------------------------------------->");
    }

    private void deleteAllAccountsByCustomer(final String customerId) throws AccountsException {
        log.debug("<-----------deleteAllAccountsByCustomer(long) AccountsServiceImpl started-----------------------------------------------------------------------" +
                "---------------------------------------------------------------------------------------------------------------->");
        final String methodName = "deleteAllAccountsByCustomer(long ) in AccountsServiceImpl";
        //checking whether customer exist
        final Optional<Customer> foundCustomer = customerRepository.findById(customerId);

        if (foundCustomer.isEmpty())
            throw new AccountsException(AccountsException.class, String.format("No such customer exists with id %s", customerId), methodName);
        accountsRepository.deleteAllByCustomer_CustomerId(customerId);
        log.debug("<-----------deleteAllAccountsByCustomer(long) AccountsServiceImpl ended ----------------------------------------------------------------" +
                "------------------------------------------------------------------------------------------------------------------->");
    }

    private Customer updateCustomerDetails(final Customer oldCustomerRecord, final CustomerDto newCustomerRecord) {
        log.debug("<-----------updateCustomerDetails(Customer,CustomerDto ) AccountsServiceImpl started-----------------------" +
                "--------------------------------------------------------------------------------------------------------------------->");
        final String oldName = oldCustomerRecord.getName();
        final String newName = newCustomerRecord.getCustomerName();
        final LocalDate oldDateOfBirth = oldCustomerRecord.getDateOfBirth();
        final LocalDate newDateOfBirth = newCustomerRecord.getDateOfBirth();
        final int newAge = Period.between(newDateOfBirth, LocalDate.now()).getYears();
        final String oldEmail = oldCustomerRecord.getEmail();
        final String newEmail = newCustomerRecord.getEmail();
        final String oldPhoneNumber = oldCustomerRecord.getPhoneNumber();
        final String newPhoneNumber = newCustomerRecord.getPhoneNumber();
        final String oldAdharNumber = oldCustomerRecord.getAdharNumber();
        final String newAdharNumber = newCustomerRecord.getAdharNumber();
        final String oldPanNumber = oldCustomerRecord.getPanNumber();
        final String newPanNumber = newCustomerRecord.getPanNumber();
        final String oldVoterId = oldCustomerRecord.getVoterId();
        final String newVoterId = newCustomerRecord.getVoterId();
        final String oldDrivingLicense = oldCustomerRecord.getDrivingLicense();
        final String newDrivingLicense = newCustomerRecord.getDrivingLicense();
        final String oldPassportNumber = oldCustomerRecord.getPassportNumber();
        final String newPassportNumber = newCustomerRecord.getPassportNumber();

        BiPredicate<String, String> isAllowedToUpdate = (newRecord, oldRecord) -> isNotBlank(newRecord) && !oldRecord.equalsIgnoreCase(newRecord);
        BiPredicate<LocalDate, LocalDate> isAllowedToUpdateForObjects = (newObj, oldObj) -> nonNull(newObj) && !oldObj.equals(newObj);

        if (isAllowedToUpdate.test(newName, oldName))
            newCustomerRecord.setCustomerName(newName);
        if (isAllowedToUpdateForObjects.test(newDateOfBirth, oldDateOfBirth)) {
            newCustomerRecord.setDateOfBirth(newDateOfBirth);
            newCustomerRecord.setAge(newAge);
        }
        if (isAllowedToUpdate.test(newEmail, oldEmail))
            newCustomerRecord.setEmail(newEmail);
        if (isAllowedToUpdate.test(newPhoneNumber, oldPhoneNumber))
            newCustomerRecord.setCustomerName(newPhoneNumber);
        if (isAllowedToUpdate.test(newAdharNumber, oldAdharNumber))
            newCustomerRecord.setCustomerName(newAdharNumber);
        if (isAllowedToUpdate.test(newPassportNumber, oldPassportNumber))
            newCustomerRecord.setCustomerName(newPassportNumber);
        if (isAllowedToUpdate.test(newPanNumber, oldPanNumber))
            newCustomerRecord.setCustomerName(newPanNumber);
        if (isAllowedToUpdate.test(newVoterId, oldVoterId))
            newCustomerRecord.setCustomerName(newVoterId);
        if (isAllowedToUpdate.test(newName, oldDrivingLicense))
            newCustomerRecord.setDrivingLicense(newDrivingLicense);

        final Customer updatedCustomer = mapToCustomer(newCustomerRecord);

        //auditing does not work during update so manually set it
        updatedCustomer.setCreatedBy("Admin");
        log.debug("<------------updateCustomerDetails(Customer oldCustomerRecord, CustomerDto newCustomerRecord) AccountsServiceImpl ended -----------------" +
                "------------------------------------------------------------------------------------------------------------------------>");
        return customerRepository.save(updatedCustomer);
    }

    private int getCreditScore(final String accountNumber) {
        log.debug("<----------getCreditScore(Long ) AccountsServiceImpl started -----------------------------------" +
                "------------------------------------------------------------------------------>");
        ///to be done

        log.debug("<-----------getCreditScore(Long ) AccountsServiceImpl ended -----------------------------------------" +
                "------------------------------------------------------------------------------------->");
        return 0;
    }

    private int updateCreditScore(final AccountsDto accountsDto) {
        log.debug("<------------updateCreditScore(AccountsDto) AccountsServiceImpl started -------------------" +
                "----------------------------------------------------------------------------------->");
        //to be done
        log.debug("<-------------updateCreditScore(AccountsDto) AccountsServiceImpl ended ----------------------" +
                "------------------------------------------------------------------------------->");
        return 0;
    }

    private PageableResponseDto<AccountsDto> accountsPagination(final DIRECTION sortDir, final String sortBy, final int pageNumber, final int pageSize, final String customerId) {
        log.debug("<---------accountsPagination(DIRECTION,String ,int,int long) AccountsServiceImpl started ------------------------------------------------" +
                "-------------------------------------------------------------------------------------------------------------------->");
        final String methodName = "accountsPagination(DIRECTION,String,int,int,long) in AccountsServiceImpl";
        final Sort sort = sortDir.equals(PAGE_SORT_DIRECTION_ASCENDING) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        final PageableResponseDto<AccountsDto> pageableResponseDto = getAllActiveAccountsByCustomerId(customerId, pageable);
        if (isEmpty(pageableResponseDto.getContent()))
            throw new BadApiRequestException(BadApiRequestException.class,
                    String.format("Customer with id %s have no accounts present", customerId),
                    methodName);

        Predicate<AccountsDto> checkForOnlyActiveAccounts=acc -> !STATUS_BLOCKED.equals(acc.getAccountStatus())
                && !STATUS_CLOSED.equals(acc.getAccountStatus());

        final List<AccountsDto> onlyActiveAccounts = pageableResponseDto.getContent()
                .stream().filter(checkForOnlyActiveAccounts).toList();

        pageableResponseDto.setContent(onlyActiveAccounts);
        log.debug("<-----------------accountsPagination(DIRECTION,String,int,int,long) AccountsServiceImpl ended -------------------------------------------------------------------------------------------------------------->");
        return pageableResponseDto;
    }

    private void uploadProfileImage(final CustomerDto customerDto) throws IOException {
        log.debug("<-------------uploadProfileImage(CustomerDto) AccountsServiceImpl started --------------------------------------------------------------" +
                "---------------------------------------------------------------------------------------------------------------------->");
        validationService.accountsUpdateValidator(null,  customerDto, UPLOAD_PROFILE_IMAGE);
        final String imageName = fIleService.uploadFile(customerDto.getCustomerImage(), IMAGE_PATH);
        final Customer customer = fetchCustomerByCustomerNumber(customerDto.getCustomerId());
        customer.setImageName(imageName);
        customerRepository.save(customer);
        log.debug("<--------------uploadProfileImage(CustomerDto) AccountsServiceImpl ended ----------------------------------------------------------------" +
                "--------------------------------------------------------------------------------------------------------------------->");
    }


    /**
     * @param postInputRequestDto
     * @return
     */
    @Override
    public OutputDto accountSetUp(final PostInputRequestDto postInputRequestDto) {
        return createAccount(postInputRequestDto);
    }

    @Override
    public OutputDto postRequestExecutor(final PostInputRequestDto postInputRequestDto) throws AccountsException, CustomerException {
        final String methodName = "postRequestExecutor(InputDto) in AccountsServiceImpl";
        //map
        final AccountsDto accountsDto = inputToAccountsDto(postInputRequestDto);
        final CustomerDto customerDto = inputToCustomerDto(postInputRequestDto);

        //Get the accountNumber & account & customer
        final String accountNumber = accountsDto.getAccountNumber();
        Accounts foundAccount;
        if (isNotBlank(accountNumber)) foundAccount = fetchAccountByAccountNumber(accountNumber);

        final String customerId = customerDto.getCustomerId();
        Customer foundCustomer;
        if (isNotBlank(customerId)) foundCustomer = fetchCustomerByCustomerNumber(customerId);
        //check the request type
        if (isNull(accountsDto.getUpdateRequest()))
            throw new AccountsException(AccountsException.class, "update request field must not be blank", methodName);
        final AllConstantHelpers.UpdateRequest request = accountsDto.getUpdateRequest();
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
    public OutputDto getRequestExecutor(final GetInputRequestDto getInputRequestDto) throws AccountsException, CustomerException {
        final String methodName = "getRequestExecutor(InputDto) in AccountsServiceImpl";

        final int pageNumber = getInputRequestDto.getPageNumber();
        if (pageNumber < 0) throw new BadApiRequestException(BadApiRequestException.class,
                "pageNumber cant be in negative", methodName);

        if (getInputRequestDto.getPageSize() < 0)
            throw new BadApiRequestException(BadApiRequestException.class, "Page Size can't be in negative", methodName);
        final int pageSize = (getInputRequestDto.getPageSize() == 0) ? DEFAULT_PAGE_SIZE : getInputRequestDto.getPageSize();
        final String sortBy = (isBlank(getInputRequestDto.getSortBy())) ? "balance" : getInputRequestDto.getSortBy();
        final AllConstantHelpers.DIRECTION sortDir = (Objects.isNull(getInputRequestDto.getSortDir())) ? asc : getInputRequestDto.getSortDir();

        final AccountsDto accountsDto = getInputToAccountsDto(getInputRequestDto);
        final CustomerDto customerDto = getInputToCustomerDto(getInputRequestDto);
        //load accounts & customer
        final String accountNumber = accountsDto.getAccountNumber();
        Accounts foundAccount = null;
        if (isNotBlank(accountNumber)) foundAccount = fetchAccountByAccountNumber(accountNumber);

        final String customerId = customerDto.getCustomerId();
        Customer foundCustomer = null;
        if (isNotBlank(customerId)) foundCustomer = fetchCustomerByCustomerNumber(customerId);

        //check the request type
        if (Objects.isNull(accountsDto.getUpdateRequest()))
            throw new AccountsException(AccountsException.class, "update request field must not be blank", methodName);
        final AllConstantHelpers.UpdateRequest request = accountsDto.getUpdateRequest();
        final StringBuffer location = new StringBuffer(500);
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
                location.append("Inside GET_ALL_ACC");
                location.trimToSize();
                validationService.accountsUpdateValidator(foundAccount,mapToCustomerDto(foundCustomer),GET_ALL_ACC);

                //validate the genuineness of sorting fields
                final Set<String> allPageableFieldsOfAccounts = getAllPageableFieldsOfAccounts();
                if (!allPageableFieldsOfAccounts.contains(sortBy))
                    throw new BadApiRequestException(BadApiRequestException.class,
                            String.format("%s is not a valid field of account", sortBy), String.format("Inside %s of %s", location, methodName));
                final PageableResponseDto<AccountsDto> pageableResponseDto = accountsPagination(sortDir, sortBy, pageNumber, pageSize, customerId);

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
    public OutputDto putRequestExecutor(final PutInputRequestDto putInputRequestDto) throws AccountsException, CustomerException, IOException {
        final String methodName = "putRequestExecutor(InputDto) in AccountsServiceImpl";
        final AccountsDto accountsDto = putInputRequestToAccountsDto(putInputRequestDto);
        final CustomerDto customerDto = putInputRequestToCustomerDto(putInputRequestDto);

        final int pageNumber = putInputRequestDto.getPageNumber();
        if (pageNumber < 0) throw new BadApiRequestException(BadApiRequestException.class,
                "pageNumber cant be in negative", methodName);

        if (putInputRequestDto.getPageSize() < 0)
            throw new BadApiRequestException(BadApiRequestException.class, "Page Size can't be in negative", methodName);
        final int pageSize = (putInputRequestDto.getPageSize() == 0) ? DEFAULT_PAGE_SIZE : putInputRequestDto.getPageSize();

        final String sortBy = (isBlank(putInputRequestDto.getSortBy())) ? "balance" : putInputRequestDto.getSortBy();
        final DIRECTION sortDir = (Objects.isNull(putInputRequestDto.getSortDir())) ? DIRECTION.asc : putInputRequestDto.getSortDir();

        //Get the accountNumber & account & customer
        final String accountNumber = accountsDto.getAccountNumber();
        Accounts foundAccount = null;
        if (isNotBlank(accountNumber)) foundAccount = fetchAccountByAccountNumber(accountNumber);

        final String customerId = customerDto.getCustomerId();
        Customer foundCustomer = null;
        if (isNotBlank(customerId)) foundCustomer = fetchCustomerByCustomerNumber(customerId);

        //check the request type
        if (isNull(accountsDto.getUpdateRequest()))
            throw new AccountsException(AccountsException.class, "update request field must not be blank", methodName);
        final AllConstantHelpers.UpdateRequest request = accountsDto.getUpdateRequest();

        final StringBuffer location = new StringBuffer(500);
        switch (request) {
            case ADD_ACCOUNT -> {
                return createAccountForAlreadyCreatedUser(customerDto.getCustomerId(), mapToAccounts(accountsDto), accountsDto);
            }
            case UPDATE_HOME_BRANCH -> {
                final Accounts updatedAccount = updateHomeBranch(accountsDto, foundAccount);

                return OutputDto.builder()
                        .customer(mapToCustomerOutputDto(mapToCustomerDto(updatedAccount.getCustomer())))
                        .accounts(mapToAccountsOutputDto(mapToAccountsDto(updatedAccount)))
                        .defaultMessage(String.format("Home branch is changed from %s to %s for customer with id %s",
                                foundAccount.getHomeBranch(), accountsDto.getHomeBranch(), foundAccount.getCustomer().getCustomerId()))
                        .build();
            }
            case UPDATE_CREDIT_SCORE -> {
                updateCreditScore(accountsDto);
                return OutputDto.builder().defaultMessage("Baad main karenge").build();
            }
            case UPLOAD_CUSTOMER_IMAGE -> {
                uploadProfileImage(customerDto);
                return OutputDto.builder().customer(mapToCustomerOutputDto(mapToCustomerDto(foundCustomer)))
                        .defaultMessage(String.format("Profile Image for customer with id:%s has been updated successfully", customerDto.getCustomerId()))
                        .build();
            }
            case INC_TRANSFER_LIMIT -> {
                final Accounts accountWithUpdatedLimit = increaseTransferLimit(accountsDto, foundAccount);
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
                return OutputDto.builder().defaultMessage("BAAD MAIN KARNGE").build();
            }
            case UPDATE_CUSTOMER_DETAILS -> {
                location.append("Inside UPDATE_CUSTOMER_DETAILS");
                location.trimToSize();
                validationService.accountsUpdateValidator(foundAccount,mapToCustomerDto(foundCustomer),UPDATE_CUSTOMER_DETAILS);
                final CustomerDto updatedCustomerDto = mapToCustomerDto(updateCustomerDetails(foundCustomer, customerDto));
                final PageableResponseDto<AccountsDto> pageableResponseDto = accountsPagination(sortDir, sortBy, pageNumber, pageSize, customerId);

                return OutputDto.builder()
                        .customer(mapToCustomerOutputDto(updatedCustomerDto))
                        .accountsListPages(pageableResponseDto)
                        .defaultMessage(String.format("Customer with id %s has been updated", customerId)).build();
            }
            default -> throw new AccountsException(AccountsException.class,
                    String.format("Invalid request type %s for PUT request", request), methodName);
        }
    }

    @Override
    public OutputDto deleteRequestExecutor(final DeleteInputRequestDto deleteInputRequestDto) throws AccountsException {
        final String methodName = "requestExecutor(InputDto) in AccountsServiceImpl";
        //map
        final AccountsDto accountsDto = deleteRequestInputToAccountsDto(deleteInputRequestDto);
        final CustomerDto customerDto = deleteInputRequestToCustomerDto(deleteInputRequestDto);
        //check the request type
        if (isNull(accountsDto.getUpdateRequest()))
            throw new AccountsException(AccountsException.class, "update request field must not be blank", methodName);
        final AllConstantHelpers.UpdateRequest request = accountsDto.getUpdateRequest();
        switch (request) {
            case DELETE_ACC -> {
                final String accountNumber = accountsDto.getAccountNumber();
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

    /**
     * @param deleteInputRequestDto
     * @return
     */
    @Override
    public OutputDto deleteCustomer(final DeleteInputRequestDto deleteInputRequestDto) {
        final String methodName = "deleteCustomer(DeleteInputRequestDto) in AccountsServiceImpl";

        final String customerId = deleteInputRequestDto.getCustomerId();
        final AllConstantHelpers.DeleteRequest deleteRequest = deleteInputRequestDto.getDeleteRequest();
        if (isNull(deleteRequest) || isBlank(customerId))
            throw new BadApiRequestException(BadApiRequestException.class, "Pls specify delete request type or customer id", methodName);

        final Customer foundCustomer = fetchCustomerByCustomerNumber(customerId);
        customerRepository.delete(foundCustomer);
        return OutputDto.builder()
                .defaultMessage(String.format("Customer with id %s is deleted", customerId))
                .build();
    }
}