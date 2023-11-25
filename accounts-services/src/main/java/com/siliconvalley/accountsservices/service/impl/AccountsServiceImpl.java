package com.siliconvalley.accountsservices.service.impl;

import com.siliconvalley.accountsservices.dto.baseDtos.AccountsDto;
import com.siliconvalley.accountsservices.dto.baseDtos.CustomerDto;
import com.siliconvalley.accountsservices.dto.inputDtos.*;
import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.responseDtos.PageableResponseDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.BadApiRequestException;
import com.siliconvalley.accountsservices.exception.CustomerException;
import com.siliconvalley.accountsservices.exception.RolesException;
import com.siliconvalley.accountsservices.exception.exceptionbuilders.ExceptionBuilder;
import com.siliconvalley.accountsservices.externalservice.service.ILoansService;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.helpers.MapperHelper;
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
import com.siliconvalley.loansservices.dto.LoansDto;
import com.siliconvalley.loansservices.dto.OutPutDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.*;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.AccountsValidateType.*;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.BLOCK_ACCOUNT;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.CLOSE_ACCOUNT;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.DIRECTION.asc;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.ExceptionCodes.*;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.RE_OPEN_ACCOUNT;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.UPDATE_CASH_LIMIT;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.UPDATE_HOME_BRANCH;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.UPLOAD_PROFILE_IMAGE;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.ValidateField.*;
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
    private final ILoansService loansService;
    private final PasswordEncoder passwordEncoder;
    private final String UPDATE = "UPDATE";
    private final Properties properties;
    /**
     * @paramType AccountsRepository
     * @returnType NA
     */
    public AccountsServiceImpl(final IAccountsRepository accountsRepository,
                               final ICustomerRepository customerRepository,
                               final IRoleRepository roleRepository,
                               final IValidationService validationService,
                               final ILoansService loansService,
                               final PasswordEncoder passwordEncoder,
                               @Qualifier("fileServicePrimary") final IImageService fIleService,
                               @Value("${path.service.accounts}") String path_to_accounts_service_properties) {
        super(accountsRepository, customerRepository);
        properties = new Properties();
        try {

            properties.load(new FileInputStream(path_to_accounts_service_properties));
        }catch (IOException e){
            log.error("Error while reading {}'s properties file {}",this.getClass().getSimpleName(),e.getMessage());
        }
        this.accountsRepository = accountsRepository;
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.fIleService = fIleService;
        this.validationService = validationService;
        this.loansService=loansService;
        this.passwordEncoder = passwordEncoder;
    }


    private Accounts processAccountInit(final Accounts accounts, final String req) throws AccountsException {
        final BigDecimal initialTransferLimitPerDay=BigDecimal.valueOf(Long.parseLong(properties.getProperty("initialTransferLimitPerDay")));
        final Double initialRateOfInterest= Double.valueOf(properties.getProperty("initialRateOfInterest"));
        final BigDecimal initialBalance=BigDecimal.valueOf(Long.parseLong(properties.getProperty("initialBalance")));
        final BigDecimal initialLoanLimitBasedOnCreditScore=BigDecimal.valueOf(Long.parseLong(properties.getProperty("initialLoanLimitBasedOnCreditScore")));

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

        accounts.setBalance(initialBalance);
        accounts.setBranchCode(getBranchCode(accounts.getHomeBranch()));
        accounts.setAccountStatus(STATUS_OPENED);
        accounts.setTransferLimitPerDay(initialTransferLimitPerDay);
        accounts.setRateOfInterest(initialRateOfInterest);

        accounts.setTotLoanIssuedSoFar(BigDecimal.valueOf(0L));
        accounts.setTotalOutStandingAmountPayableToBank(BigDecimal.valueOf(0L));
        accounts.setAnyActiveLoans(false);
        accounts.setApprovedLoanLimitBasedOnCreditScore(initialLoanLimitBasedOnCreditScore);
        log.debug("<------processAccountInit(Accounts, String) AccountsServiceImpl ended -------------------------------------------------------------------" +
                "----------------------------------------------------------------------------------------------------------------->");
        return accounts;
    }

    private Customer processCustomerInformation(final Customer customer) throws RolesException {
        log.debug("<-------processCustomerInformation(Customer) AccountsServiceImpl started--------------------------------------------------------------------" +
                "------------------------------------------------------------------------------------------------------------------------->");
        final String methodName = "processCustomerInformation(Customer)";
        final LocalDate dob = customer.getDateOfBirth();
        final int age = Period.between(dob, LocalDate.now()).getYears();
        customer.setAge(age);

        final String customerId = UUID.randomUUID().toString();
        customer.setCustomerId(customerId);

        //set role
        final String NORMAL_ROLE_ID=properties.getProperty("normal.role.id");

        final Role roles = roleRepository.findById(NORMAL_ROLE_ID)
                .orElseThrow(()->(RolesException)ExceptionBuilder.builder()
                        .className(RolesException.class)
                        .reason("No roles found")
                        .methodName(methodName)
                        .build(ROLE_EXC));
        customer.setRoles(new HashSet<>(Collections.singletonList(roles)));

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
        return new OutputDto.Builder()
                .customer(mapToCustomerOutputDto(mapToCustomerDto(savedCustomer)))
                .accounts(mapToAccountsOutputDto(mapToAccountsDto(savedCustomer.getAccounts().stream().toList().get(0))))
                .defaultMessage(String.format("Account with id %s is created for customer %s", accountNumber, savedCustomer.getCustomerId()))
                .build();
    }


    private OutputDto createAccountForAlreadyCreatedUser(final String customerId, final Accounts loadAccount, final AccountsDto accountsDto) throws AccountsException {
        log.debug("<-------------- createAccountForAlreadyCreatedUser(long,Accounts,AccountsDto) AccountsServiceImpl started -----------------------------" +
                "------------------------------------------------------------------------------------------------------>--->");
        final String methodName = "createAccountForAlreadyCreatedUser(long,InoutDto) in AccountsServiceImpl";


        final Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()->  (AccountsException)ExceptionBuilder.builder()
                        .className(AccountsException.class)
                        .reason(String.format("No such customers with id %s found", customerId))
                        .methodName(methodName)
                        .build(ACC_EXC));

        loadAccount.setCustomer(customer);
        validationService.accountsUpdateValidator(loadAccount, null, ADD_ACCOUNT);
        final Accounts accounts = mapToAccounts(accountsDto);
        accounts.setCustomer(customer);
        Accounts processedAccount = processAccountInit(accounts, UPDATE);
        final Accounts savedAccount = accountsRepository.save(processedAccount);
        log.debug("<-------createAccountForAlreadyCreatedUser(long,Accounts,AccountsDto) AccountsServiceImpl ended -----------------------------------" +
                "--------------------------------------------------------------------------------------------------------------------->");

        return new OutputDto.Builder()
                .customer(mapToCustomerOutputDto(mapToCustomerDto(customer)))
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
        return new OutputDto.Builder()
                .customer(mapToCustomerOutputDto(mapToCustomerDto(foundCustomer)))
                .accounts(mapToAccountsOutputDto(mapToAccountsDto(foundAccount)))
                .defaultMessage(String.format("Retrieved info about account with id: %s", foundAccount.getAccountNumber()))
                .build();
    }

    private PageableResponseDto<AccountsDto> getAllActiveAccountsByCustomerId(final String customerId, final Pageable pageable) throws AccountsException {
        log.debug("<-----------------getAllActiveAccountsByCustomerId(long,Pageable) AccountsServiceImpl started -----------------------------------" +
                "----------------------------------------------------------------------------------------------------------------->");
        final String methodName = "getAllAccountsByCustomerId(long) in AccountsServiceImpl";
        final Page<Accounts> allPagedAccounts = accountsRepository.findAllByCustomer_CustomerId(customerId, pageable)
                .orElseThrow(()-> new AccountsException(AccountsException.class,
                String.format("No such accounts present with this customer %s", customerId), methodName));

        log.debug("<------------------getAllActiveAccountsByCustomerId(long,Pageable) AccountsServiceImpl ended ----------------------------------------------------" +
                "------------------------------------------------------------------------------------------------------------------>");
        return getPageableResponse(allPagedAccounts, DestinationDtoType.AccountsDto);
    }

    private PageableResponseDto<CustomerDto> getAllCustomers( final Pageable pageable) throws AccountsException {
        log.debug("<-----------------getAllActiveCustomer(long,Pageable) AccountsServiceImpl started -----------------------------------" +
                "----------------------------------------------------------------------------------------------------------------->");
        final String methodName = "getAllActiveCustomer(long) in AccountsServiceImpl";


        final Page<Customer> allPagedAccounts = customerRepository.findAll(pageable)
                .orElseThrow(()-> (CustomerException)ExceptionBuilder.builder()
                        .className(CustomerException.class)
                        .reason("No such customers")
                        .methodName(methodName)
                        .build(CUST_EXC));

        log.debug("<------------------getAllActiveAccountsByCustomerId(long,Pageable) AccountsServiceImpl ended ----------------------------------------------------" +
                "------------------------------------------------------------------------------------------------------------------>");
        return getPageableResponse(allPagedAccounts, DestinationDtoType.CustomerDto);
    }


    private Accounts updateHomeBranch(final AccountsDto accountsDto, final Accounts accounts) throws AccountsException {
        log.debug("<-------------updateHomeBranch(AccountsDto,Accounts) AccountsServiceImpl started --------------------------------------------------------" +
                "-------------------------------------------------------------------------------------------------------------------->");
        final AllConstantHelpers.Branch oldHomeBranch = accounts.getHomeBranch();
        final AllConstantHelpers.Branch newHomeBranch = accountsDto.homeBranch();
        Accounts savedUpdatedAccount = accounts;

        validationService.accountsUpdateValidator(mapToAccounts(accountsDto),
                mapToCustomerDto(accounts.getCustomer()), UPDATE_HOME_BRANCH);
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
        final BigDecimal newCashLimit = accountsDto.transferLimitPerDay();

        Accounts savedAccount = accounts;
        BiPredicate<BigDecimal, BigDecimal> checkForCashLimitRevision = (newLimit, oldLimit) ->
                newLimit.compareTo(BigDecimal.ZERO) == 0
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
        final Customer foundCustomer = customerRepository.findById(customerId)
                .orElseThrow(()->(AccountsException)ExceptionBuilder.builder()
                                .className(AccountsException.class)
                                .reason(String.format("No such customer exists with id %s", customerId))
                                .methodName(methodName)
                                .build(ACC_EXC));

        accountsRepository.deleteAllByCustomer_CustomerId(customerId);
        log.debug("<-----------deleteAllAccountsByCustomer(long) AccountsServiceImpl ended ----------------------------------------------------------------" +
                "------------------------------------------------------------------------------------------------------------------->");
    }

    private CustomerDto mapEveryRecordsField(CustomerDto record){
        return new CustomerDto.Builder()
                .customerId(record.customerId())
                .customerName(record.customerName())
                .DateOfBirth(record.DateOfBirth())
                .age(record.age())
                .email(record.email())
                .phoneNumber(record.phoneNumber())
                .adharNumber(record.adharNumber())
                .imageName(record.imageName())
                .panNumber(record.panNumber())
                .voterId(record.voterId())
                .passportNumber(record.passportNumber())
                .drivingLicense(record.drivingLicense())
                .password(record.password())
                .address(record.address())
                .build();
    }

    private Customer updateCustomerDetails(final Customer oldCustomerRecord, final CustomerDto newCustomerRecord) {
        log.debug("<-----------updateCustomerDetails(Customer,CustomerDto ) AccountsServiceImpl started-----------------------" +
                "--------------------------------------------------------------------------------------------------------------------->");
        final String oldName = oldCustomerRecord.getName();
        final String newName = newCustomerRecord.customerName();
        final LocalDate oldDateOfBirth = oldCustomerRecord.getDateOfBirth();
        final LocalDate newDateOfBirth = newCustomerRecord.DateOfBirth();

        final String oldEmail = oldCustomerRecord.getEmail();
        final String newEmail = newCustomerRecord.email();
        final String oldPhoneNumber = oldCustomerRecord.getPhoneNumber();
        final String newPhoneNumber = newCustomerRecord.phoneNumber();
        final String oldAdharNumber = oldCustomerRecord.getAdharNumber();
        final String newAdharNumber = newCustomerRecord.adharNumber();
        final String oldPanNumber = oldCustomerRecord.getPanNumber();
        final String newPanNumber = newCustomerRecord.panNumber();
        final String oldVoterId = oldCustomerRecord.getVoterId();
        final String newVoterId = newCustomerRecord.voterId();
        final String oldDrivingLicense = oldCustomerRecord.getDrivingLicense();
        final String newDrivingLicense = newCustomerRecord.drivingLicense();
        final String oldPassportNumber = oldCustomerRecord.getPassportNumber();
        final String newPassportNumber = newCustomerRecord.passportNumber();

        BiPredicate<String, String> isAllowedToUpdate = (newRecord, oldRecord) -> isNotBlank(newRecord) && !oldRecord.equalsIgnoreCase(newRecord);
        BiPredicate<LocalDate, LocalDate> isAllowedToUpdateForObjects = (newObj, oldObj) -> nonNull(newObj) && !oldObj.equals(newObj);


        CustomerDto newUpdatedRecord = new CustomerDto.Builder()
                .customerId(oldCustomerRecord.getCustomerId())
                .customerName(oldCustomerRecord.getName())
                .DateOfBirth(oldCustomerRecord.getDateOfBirth())
                .age(oldCustomerRecord.getAge())
                .email(oldCustomerRecord.getEmail())
                .phoneNumber(oldCustomerRecord.getPhoneNumber())
                .adharNumber(oldCustomerRecord.getAdharNumber())
                .imageName(oldCustomerRecord.getImageName())
                .panNumber(oldCustomerRecord.getPanNumber())
                .voterId(oldCustomerRecord.getVoterId())
                .passportNumber(oldCustomerRecord.getPassportNumber())
                .drivingLicense(oldCustomerRecord.getDrivingLicense())
                .password(oldCustomerRecord.getPassword())
                .address(oldCustomerRecord.getAddress())
                .password(oldCustomerRecord.getPassword())
                .build();

        if (isAllowedToUpdate.test(newName, oldName)) {
            newUpdatedRecord=newUpdatedRecord.withCustomerName(newName);
            newUpdatedRecord = mapEveryRecordsField(newUpdatedRecord);
        }
        if (isAllowedToUpdateForObjects.test(newDateOfBirth, oldDateOfBirth)) {
            validationService.fieldValidator(oldCustomerRecord.getCustomerId(), newDateOfBirth.toString(),DOB);
            newUpdatedRecord=newUpdatedRecord.withDateOfBirth(newDateOfBirth);
            final int newAge = Period.between(newDateOfBirth, LocalDate.now()).getYears();
            newUpdatedRecord=newUpdatedRecord.withAge(newAge);
            newUpdatedRecord = mapEveryRecordsField(newUpdatedRecord);
        }
        if (isAllowedToUpdate.test(newEmail, oldEmail)) {
            validationService.fieldValidator(oldCustomerRecord.getCustomerId(),newEmail,EMAIL);
            newUpdatedRecord=newUpdatedRecord.withEmail(newEmail);
            newUpdatedRecord = mapEveryRecordsField(newUpdatedRecord);
        }
        if (isAllowedToUpdate.test(newPhoneNumber, oldPhoneNumber)) {
            validationService.fieldValidator(oldCustomerRecord.getCustomerId(),newPhoneNumber,PHONE);
            newUpdatedRecord=newUpdatedRecord.withPhoneNumber(newPhoneNumber);
            newUpdatedRecord = mapEveryRecordsField(newUpdatedRecord);
        }
        if (isAllowedToUpdate.test(newAdharNumber, oldAdharNumber)) {
            validationService.fieldValidator(oldCustomerRecord.getCustomerId(),newAdharNumber,ADHAR);
            newUpdatedRecord=newUpdatedRecord.withAdharNumber(newAdharNumber);
            newUpdatedRecord = mapEveryRecordsField(newUpdatedRecord);
        }
        if (isAllowedToUpdate.test(newPassportNumber, oldPassportNumber)){
            validationService.fieldValidator(oldCustomerRecord.getCustomerId(),newPassportNumber,PASSPORT);
            newUpdatedRecord=newUpdatedRecord.withPassportNumber(newPassportNumber);
            newUpdatedRecord = mapEveryRecordsField(newUpdatedRecord);
        }
        if (isAllowedToUpdate.test(newPanNumber, oldPanNumber)) {
            validationService.fieldValidator(oldCustomerRecord.getCustomerId(),newPanNumber,PAN);
            newUpdatedRecord=newUpdatedRecord.withPanNumber(newPanNumber);
            newUpdatedRecord=mapEveryRecordsField(newUpdatedRecord);
        }
        if (isAllowedToUpdate.test(newVoterId, oldVoterId)) {
            validationService.fieldValidator(oldCustomerRecord.getCustomerId(),newVoterId,VOTER);
            newUpdatedRecord=newUpdatedRecord.withVoterId(newVoterId);
            newUpdatedRecord=mapEveryRecordsField(newUpdatedRecord);
        }
        if (isAllowedToUpdate.test(newDrivingLicense, oldDrivingLicense)){
            validationService.fieldValidator(oldCustomerRecord.getCustomerId(),newDrivingLicense,DRIVING_LICENSE);
            newUpdatedRecord=newUpdatedRecord.withDrivingLicense(newDrivingLicense);
            newUpdatedRecord=mapEveryRecordsField(newUpdatedRecord);
        }

        final Customer updatedCustomer = mapToCustomer(newUpdatedRecord);
        updatedCustomer.setImageName(oldCustomerRecord.getImageName());
        updatedCustomer.setAccounts(oldCustomerRecord.getAccounts());
        updatedCustomer.setRoles(oldCustomerRecord.getRoles());
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

    private PageableResponseDto<AccountsDto> accountsPagination(final DIRECTION sortDir, final String sortBy, final int pageNumber, final int pageSize, final String customerId) throws BadApiRequestException{
        log.debug("<---------accountsPagination(DIRECTION,String ,int,int long) AccountsServiceImpl started ------------------------------------------------" +
                "-------------------------------------------------------------------------------------------------------------------->");
        final String methodName = "accountsPagination(DIRECTION,String,int,int,long) in AccountsServiceImpl";
        final Sort sort = sortDir.equals(PAGE_SORT_DIRECTION_ASCENDING) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        final PageableResponseDto<AccountsDto> pageableResponseDto = getAllActiveAccountsByCustomerId(customerId, pageable);



        if (isEmpty(pageableResponseDto.content()))
            throw (BadApiRequestException)ExceptionBuilder.builder()
                    .className(BadApiRequestException.class)
                    .reason(String.format("Customer with id %s have no accounts present", customerId))
                    .methodName(methodName)
                    .build(BAD_API_EXC);

        Predicate<AccountsDto> checkForOnlyActiveAccounts=acc -> !STATUS_BLOCKED.equals(acc.accountStatus())
                && !STATUS_CLOSED.equals(acc.accountStatus());

        final List<AccountsDto> onlyActiveAccounts = pageableResponseDto.content()
                .stream().filter(checkForOnlyActiveAccounts).toList();

        pageableResponseDto.withContent(onlyActiveAccounts);
        log.debug("<-----------------accountsPagination(DIRECTION,String,int,int,long) AccountsServiceImpl ended -------------------------------------------------------------------------------------------------------------->");
        return pageableResponseDto;
    }

    private PageableResponseDto<CustomerDto> customerPagination(final DIRECTION sortDir, final String sortBy, final int pageNumber, final int pageSize) throws BadApiRequestException{
        log.debug("<---------customerPagination(DIRECTION,String ,int,int long) AccountsServiceImpl started ------------------------------------------------" +
                "-------------------------------------------------------------------------------------------------------------------->");
        final String methodName = "customerPagination(DIRECTION,String,int,int,long) in AccountsServiceImpl";
        final Sort sort = sortDir.equals(PAGE_SORT_DIRECTION_ASCENDING) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        final PageableResponseDto<CustomerDto> pageableResponseDto = getAllCustomers(pageable);

        if (isEmpty(pageableResponseDto.content()))
            throw (BadApiRequestException)ExceptionBuilder.builder()
                    .className(BadApiRequestException.class)
                    .reason("No customers found")
                    .methodName(methodName)
                    .build(BAD_API_EXC);

        final List<CustomerDto> allCustomers = pageableResponseDto.content()
                .stream().toList();
        List<Customer> mutableListOfCustomers= new ArrayList<>(allCustomers.stream()
                .map(MapperHelper::mapToCustomer).toList());
        Collections.sort(mutableListOfCustomers);
        pageableResponseDto.withContent(mutableListOfCustomers.stream().map(MapperHelper::mapToCustomerDto).toList());
        log.debug("<-----------------accountsPagination(DIRECTION,String,int,int,long) AccountsServiceImpl ended -------------------------------------------------------------------------------------------------------------->");
        return pageableResponseDto;
    }

    private void uploadProfileImage(final CustomerDto customerDto) throws IOException {
        log.debug("<-------------uploadProfileImage(CustomerDto) AccountsServiceImpl started --------------------------------------------------------------" +
                "---------------------------------------------------------------------------------------------------------------------->");
        validationService.accountsUpdateValidator(null,  customerDto, UPLOAD_PROFILE_IMAGE);
        final String IMAGE_PATH=properties.getProperty("customer.profile.images.path");
        final String imageName = fIleService.uploadFile(customerDto.customerImage(), IMAGE_PATH);
        final Customer customer = fetchCustomerByCustomerNumber(customerDto.customerId());
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
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public OutputDto postRequestExecutor(final PostInputRequestDto postInputRequestDto) throws AccountsException, CustomerException {
        final String methodName = "postRequestExecutor(InputDto) in AccountsServiceImpl";
        //map
        final AccountsDto accountsDto = inputToAccountsDto(postInputRequestDto);
        final CustomerDto customerDto = inputToCustomerDto(postInputRequestDto);

        //Get the accountNumber & account & customer
        final String accountNumber = accountsDto.accountNumber();
        Accounts foundAccount;
        if (isNotBlank(accountNumber)) foundAccount = fetchAccountByAccountNumber(accountNumber);

        final String customerId = customerDto.customerId();
        Customer foundCustomer;
        if (isNotBlank(customerId)) foundCustomer = fetchCustomerByCustomerNumber(customerId);
        //check the request type


        if (isNull(accountsDto.updateRequest()))
            throw (AccountsException) ExceptionBuilder.builder()
                    .className(AccountsException.class)
                    .reason("update request field must not be blank")
                    .methodName(methodName).build(ACC_EXC);
        final AllConstantHelpers.UpdateRequest request = accountsDto.updateRequest();
        switch (request) {
            case LEND_LOAN -> {
                //to be done...
//                LoansDto loansDto= new LoansDto.Builder()
//                        .customerId(customerId)
//                        .totalLoan(postInputRequestDto.totalLoan())
//                        .loanType(postInputRequestDto.loanType())
//                        .requestType(postInputRequestDto.requestType())
//                        .loanTenureInYears(postInputRequestDto.loanTenureInYears())
//                        .build();
//
//                ResponseEntity<OutPutDto> processedLoan=loansService.borrowLoan(loansDto);
                return new OutputDto.Builder().defaultMessage("Baad main karenge").build();
            }
            default -> throw (AccountsException) ExceptionBuilder.builder()
                    .className(AccountsException.class)
                    .reason(String.format("Invalid request type %s for POST requests", request))
                    .methodName(methodName).build(ACC_EXC);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public OutputDto externalServiceRequestExecutor(final ExternalServiceRequestDto externalServiceRequestDto) throws AccountsException, CustomerException {
        final String methodName = "externalServiceRequestExecutor(ExternalServiceRequestDto) in AccountsServiceImpl";

        //Get the accountNumber & account & customer
        final String accountNumber = externalServiceRequestDto.accountNumber();
        Accounts foundAccount;
        if (isNotBlank(accountNumber)) foundAccount = fetchAccountByAccountNumber(accountNumber);

        final String customerId = externalServiceRequestDto.customerId();
        Customer foundCustomer;
        if (isNotBlank(customerId)) foundCustomer = fetchCustomerByCustomerNumber(customerId);


        //check the request type
        if (isNull(externalServiceRequestDto.updateRequest()))
            throw (AccountsException) ExceptionBuilder.builder()
                .className(AccountsException.class)
                .reason("update request field must not be blank")
                .methodName(methodName).build(ACC_EXC);

        final AllConstantHelpers.UpdateRequest request = externalServiceRequestDto.updateRequest();
        switch (request) {
            case LEND_LOAN -> {
                //to be done...
                LoansDto loansDto= new LoansDto.Builder()
                        .customerId(customerId)
                        .totalLoan(externalServiceRequestDto.totalLoan())
                        .loanType(externalServiceRequestDto.loanType())
                        .requestType(externalServiceRequestDto.requestType())
                        .loanTenureInYears(externalServiceRequestDto.loanTenureInYears())
                        .build();

                ResponseEntity<OutPutDto> processedLoan=loansService.borrowLoan(loansDto);
                return new OutputDto.Builder()
                        .loansOutputDto(processedLoan.getBody())
                        .defaultMessage("Baad main karenge").build();
            }

            default -> throw (AccountsException) ExceptionBuilder.builder().className(AccountsException.class)
                    .reason(String.format("Invalid request type %s for POST requests", request))
                    .methodName(methodName).build(ACC_EXC);
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public OutputDto getRequestExecutor(final GetInputRequestDto getInputRequestDto) throws BadApiRequestException,AccountsException, CustomerException {
        final String methodName = "getRequestExecutor(InputDto) in AccountsServiceImpl";

        final int pageNumber = getInputRequestDto.pageNumber();
        if (pageNumber < 0) throw new BadApiRequestException(BadApiRequestException.class,
                "pageNumber cant be in negative", methodName);


        if (getInputRequestDto.pageSize() < 0)
            throw (BadApiRequestException) ExceptionBuilder.builder().className(BadApiRequestException.class)
                    .reason("Page Size can't be in negative")
                    .methodName(methodName).build(BAD_API_EXC);
        final int pageSize = (getInputRequestDto.pageSize() == 0) ? DEFAULT_PAGE_SIZE : getInputRequestDto.pageSize();
        final String sortBy = (isBlank(getInputRequestDto.sortBy())) ? "balance" : getInputRequestDto.sortBy();
        final AllConstantHelpers.DIRECTION sortDir = (Objects.isNull(getInputRequestDto.sortDir())) ? asc : getInputRequestDto.sortDir();

        final AccountsDto accountsDto = getInputToAccountsDto(getInputRequestDto);
        final CustomerDto customerDto = getInputToCustomerDto(getInputRequestDto);
        //load accounts & customer
        final String accountNumber = accountsDto.accountNumber();
        Accounts foundAccount = null;
        if (isNotBlank(accountNumber)) foundAccount = fetchAccountByAccountNumber(accountNumber);

        final String customerId = customerDto.customerId();
        Customer foundCustomer = null;
        if (isNotBlank(customerId)) foundCustomer = fetchCustomerByCustomerNumber(customerId);


        //check the request type
        if (Objects.isNull(accountsDto.updateRequest()))
            throw (AccountsException)ExceptionBuilder.builder().className(AccountsException.class)
                    .reason("update request field must not be blank")
                    .methodName(methodName).build(ACC_EXC);
        final AllConstantHelpers.UpdateRequest request = accountsDto.updateRequest();
        final String location;
        switch (request) {
            case GET_CREDIT_SCORE -> {
                getCreditScore(accountNumber);
                //to be done after implementing credit card microservice
                return new OutputDto.Builder().defaultMessage("Baad main karenge").build();
            }
            case GET_ACC_INFO -> {
                return getAccountInfo(accountNumber);
            }
            case GET_ALL_CUSTOMER -> {
                location="Inside GET_ALL_CUST";
                validationService.accountsUpdateValidator(foundAccount,
                        mapToCustomerDto(foundCustomer),GET_ALL_CUSTOMER);

                final Set<String> allPageableFieldsOfCustomer = getAllPageableFieldsOfAcustomer();

                if (!allPageableFieldsOfCustomer.contains(sortBy))
                    throw (BadApiRequestException) ExceptionBuilder.builder().className(BadApiRequestException.class)
                            .reason(String.format("%s is not a valid field of account", sortBy))
                            .methodName(String.format("Inside %s of %s", location, methodName))
                            .build(BAD_API_EXC);
                final PageableResponseDto<CustomerDto> pageableResponseDto =
                        customerPagination(sortDir, sortBy, pageNumber, pageSize);

                return new OutputDto.Builder()
                        .customer(mapToCustomerOutputDto(mapToCustomerDto(foundCustomer)))
                        .customerListPages(pageableResponseDto)
                        .defaultMessage("Fetched all customers")
                        .build();

            }
            case GET_ALL_ACC -> {
                location="Inside GET_ALL_ACC";
                validationService.accountsUpdateValidator(foundAccount,mapToCustomerDto(foundCustomer),GET_ALL_ACC);

                //validate the genuineness of sorting fields
                final Set<String> allPageableFieldsOfAccounts = getAllPageableFieldsOfAccounts();

                if (!allPageableFieldsOfAccounts.contains(sortBy))
                    throw (BadApiRequestException) ExceptionBuilder.builder().className(BadApiRequestException.class)
                            .reason(String.format("%s is not a valid field of account", sortBy))
                            .methodName(String.format("Inside %s of %s", location, methodName))
                            .build(BAD_API_EXC);
                final PageableResponseDto<AccountsDto> pageableResponseDto = accountsPagination(sortDir, sortBy, pageNumber, pageSize, customerId);

                return new OutputDto.Builder()
                        .customer(mapToCustomerOutputDto(mapToCustomerDto(foundCustomer)))
                        .accountsListPages(pageableResponseDto)
                        .defaultMessage(String.format("Fetched all accounts for customer id:%s", customerId))
                        .build();
            }
            default ->
                    throw (AccountsException) ExceptionBuilder.builder().className(AccountsException.class)
                            .reason(String.format("Invalid request type %s for GET request", request))
                            .methodName( methodName)
                            .build(ACC_EXC);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public OutputDto putRequestExecutor(final PutInputRequestDto putInputRequestDto) throws BadApiRequestException,AccountsException,CustomerException, IOException {
        final String methodName = "putRequestExecutor(InputDto) in AccountsServiceImpl";
        final AccountsDto accountsDto = putInputRequestToAccountsDto(putInputRequestDto);
        final CustomerDto customerDto = putInputRequestToCustomerDto(putInputRequestDto);

        final int pageNumber = putInputRequestDto.pageNumber();

        if (pageNumber < 0) throw (BadApiRequestException) ExceptionBuilder.builder().className(BadApiRequestException.class)
                .reason("pageNumber cant be in negative")
                .methodName( methodName)
                .build(BAD_API_EXC);


        if (putInputRequestDto.pageSize() < 0)
            throw (BadApiRequestException) ExceptionBuilder.builder().className(BadApiRequestException.class)
                    .reason("Page Size can't be in negative")
                    .methodName( methodName)
                    .build(BAD_API_EXC);
        final int pageSize = (putInputRequestDto.pageSize() == 0) ? DEFAULT_PAGE_SIZE : putInputRequestDto.pageSize();

        final String sortBy = (isBlank(putInputRequestDto.sortBy())) ? "balance" : putInputRequestDto.sortBy();
        final DIRECTION sortDir = (Objects.isNull(putInputRequestDto.sortDir())) ? DIRECTION.asc : putInputRequestDto.sortDir();

        //Get the accountNumber & account & customer
        final String accountNumber = accountsDto.accountNumber();
        Accounts foundAccount = null;
        if (isNotBlank(accountNumber)) foundAccount = fetchAccountByAccountNumber(accountNumber);

        final String customerId = customerDto.customerId();
        Customer foundCustomer = null;
        if (isNotBlank(customerId)) foundCustomer = fetchCustomerByCustomerNumber(customerId);


        //check the request type
        if (isNull(accountsDto.updateRequest()))
            throw (AccountsException) ExceptionBuilder.builder().className(AccountsException.class)
                    .reason("update request field must not be blank")
                    .methodName( methodName)
                    .build(ACC_EXC);
        final AllConstantHelpers.UpdateRequest request = accountsDto.updateRequest();

        final String location;
        switch (request) {
            case ADD_ACCOUNT -> {
                return createAccountForAlreadyCreatedUser(customerDto.customerId(), mapToAccounts(accountsDto), accountsDto);
            }
            case UPDATE_HOME_BRANCH -> {
                final Branch oldBranch=foundAccount.getHomeBranch();
                final Accounts updatedAccount = updateHomeBranch(accountsDto, foundAccount);

                return new OutputDto.Builder()
                        .customer(mapToCustomerOutputDto(mapToCustomerDto(updatedAccount.getCustomer())))
                        .accounts(mapToAccountsOutputDto(mapToAccountsDto(updatedAccount)))
                        .defaultMessage(String.format("Home branch is changed from %s to %s for customer with id %s",
                                oldBranch, accountsDto.homeBranch(), foundAccount.getCustomer().getCustomerId()))
                        .build();
            }
            case UPDATE_CREDIT_SCORE -> {
                updateCreditScore(accountsDto);
                return new OutputDto.Builder().defaultMessage("Baad main karenge").build();
            }
            case UPLOAD_CUSTOMER_IMAGE -> {
                uploadProfileImage(customerDto);
                return new OutputDto.Builder().customer(mapToCustomerOutputDto(mapToCustomerDto(foundCustomer)))
                        .defaultMessage(String.format("Profile Image for customer with id:%s has been updated successfully", customerDto.customerId()))
                        .build();
            }
            case INC_TRANSFER_LIMIT -> {
                final Accounts accountWithUpdatedLimit = increaseTransferLimit(accountsDto, foundAccount);
                return new OutputDto.Builder()
                        .customer(mapToCustomerOutputDto(customerDto))
                        .accounts(mapToAccountsOutputDto(mapToAccountsDto(accountWithUpdatedLimit)))
                        .defaultMessage(String.format("Transfer Limit has been increased from %s to %s", foundAccount.getTransferLimitPerDay(), accountWithUpdatedLimit.getTransferLimitPerDay()))
                        .build();
            }
            case CLOSE_ACC -> {
                closeAccount(foundAccount);
                return new OutputDto.Builder()
                        .defaultMessage(String.format("Account with id %s is successfully closed", accountsDto.accountNumber()))
                        .build();
            }
            case RE_OPEN_ACC -> {
                unCloseAccount(foundAccount);
                return new OutputDto.Builder().defaultMessage(String.format("Account with id %s has been reopened ", accountNumber)).build();
            }
            case BLOCK_ACC -> {
                //Note: account once blocked , no operations can be performed on it not even get
                //only authority reserves the right to unblock it
                blockAccount(foundAccount);
                return new OutputDto.Builder().defaultMessage(String.format("Account with id %s has been blocked", accountNumber)).build();
            }
            case INC_APPROVED_LOAN_LIMIT -> {
                //to be done.....
                return new OutputDto.Builder().defaultMessage("BAAD MAIN KARNGE").build();
            }
            case UPDATE_CUSTOMER_DETAILS -> {
                location="Inside UPDATE_CUSTOMER_DETAILS";
                validationService.accountsUpdateValidator(foundAccount,mapToCustomerDto(foundCustomer),UPDATE_CUSTOMER_DETAILS);
                final CustomerDto updatedCustomerDto = mapToCustomerDto(updateCustomerDetails(foundCustomer, customerDto));
                final PageableResponseDto<AccountsDto> pageableResponseDto = accountsPagination(sortDir, sortBy, pageNumber, pageSize, customerId);

                return new OutputDto.Builder()
                        .customer(mapToCustomerOutputDto(updatedCustomerDto))
                        .accountsListPages(pageableResponseDto)
                        .defaultMessage(String.format("Customer with id %s has been updated", customerId)).build();
            }
            default ->
                    throw (AccountsException) ExceptionBuilder.builder().className(AccountsException.class)
                            .reason(String.format("Invalid request type %s for PUT request", request))
                            .methodName( methodName)
                            .build(ACC_EXC);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public OutputDto deleteRequestExecutor(final DeleteInputRequestDto deleteInputRequestDto) throws AccountsException {
        final String methodName = "requestExecutor(InputDto) in AccountsServiceImpl";
        //map
        final AccountsDto accountsDto = deleteRequestInputToAccountsDto(deleteInputRequestDto);
        final CustomerDto customerDto = deleteInputRequestToCustomerDto(deleteInputRequestDto);


        //check the request type
        if (isNull(accountsDto.updateRequest()))
            throw (AccountsException)ExceptionBuilder.builder().className(AccountsException.class)
                    .reason("update request field must not be blank").methodName(methodName).build(ACC_EXC);

        final AllConstantHelpers.UpdateRequest request = accountsDto.updateRequest();
        switch (request) {
            case DELETE_ACC -> {
                final String accountNumber = accountsDto.accountNumber();
                deleteAccount(accountNumber);
                return new OutputDto.Builder()
                        .defaultMessage(String.format("Account with id %s is successfully deleted", accountNumber))
                        .build();
            }
            case DELETE_ALL_ACC -> {
                deleteAllAccountsByCustomer(customerDto.customerId());
                return new OutputDto.Builder()
                        .defaultMessage(String.format("All accounts that belonged to customer with id %s has been deleted",
                                customerDto.customerId())).build();
            }
            default ->
                    throw (AccountsException)ExceptionBuilder.builder().className(AccountsException.class)
                            .reason(String.format("Invalid request type %s for DELETE request", request))
                            .methodName(methodName).build(ACC_EXC);
        }
    }

    /**
     * @param deleteInputRequestDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public OutputDto deleteCustomer(final DeleteInputRequestDto deleteInputRequestDto) throws BadApiRequestException,CustomerException{
        final String methodName = "deleteCustomer(DeleteInputRequestDto) in AccountsServiceImpl";

        final String customerId = deleteInputRequestDto.customerId();
        final AllConstantHelpers.DeleteRequest deleteRequest = deleteInputRequestDto.deleteRequest();
        if (isNull(deleteRequest) || isBlank(customerId))
            throw (BadApiRequestException)ExceptionBuilder.builder()
                    .className(BadApiRequestException.class)
                    .reason("Pls specify delete request type or customer id")
                    .methodName(methodName).build(BAD_API_EXC);

        final Customer foundCustomer = fetchCustomerByCustomerNumber(customerId);
        customerRepository.delete(foundCustomer);
        return new OutputDto.Builder()
                .defaultMessage(String.format("Customer with id %s is deleted", customerId))
                .build();
    }
}