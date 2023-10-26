package com.siliconvalley.accountsservices.service.impl;

import com.google.common.collect.Iterables;
import com.siliconvalley.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.siliconvalley.accountsservices.dto.baseDtos.CustomerDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.exception.*;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.helpers.MapperHelper;
import com.siliconvalley.accountsservices.model.*;
import com.siliconvalley.accountsservices.repository.IAccountsRepository;
import com.siliconvalley.accountsservices.repository.ICustomerRepository;
import com.siliconvalley.accountsservices.service.IValidationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.*;
import static com.siliconvalley.accountsservices.helpers.MapperHelper.mapToCustomer;
import static com.siliconvalley.accountsservices.helpers.RegexMatchersHelper.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


@Service
@Primary
@Slf4j
public final class ValidationServiceImpl implements IValidationService {
    private final IAccountsRepository accountsRepository;
    private final ICustomerRepository customerRepository;

    ValidationServiceImpl(final IAccountsRepository accountsRepository,
                          final ICustomerRepository customerRepository) {
        this.accountsRepository = accountsRepository;
        this.customerRepository=customerRepository;
    }

    @Override
    public void accountsUpdateValidator(final Accounts accounts, final CustomerDto customerDto, final AccountsValidateType request) throws AccountsException, BadApiRequestException {
        log.debug("<---------------accountsUpdateValidator(Accounts,CustomerDto,ValidateType) ValidationServiceImpl started -----------------------------------" +
                "------------------------------------------------------------------------------------------------------------------------>");
        final String methodName = "accountsUpdateValidator(Accounts,CustomerDto,ValidateType) in ValidationServiceImpl";
        final String location;
        switch (request) {
            case CREATE_ACC -> {
                location="Inside CREATE_ACC";
                //check whether such account owner is already present
                final List<Accounts> accountsList = accountsRepository.findAll();
                //if no accounts by far then certainly we can add
                if (accountsList.isEmpty()) return;

                final String adharNumber = accounts.getCustomer().getAdharNumber();
                final boolean isNotPossible = accountsList.stream().anyMatch(acc -> adharNumber.equalsIgnoreCase(acc.getCustomer().getAdharNumber()));
                if (isNotPossible)
                    throw new AccountsException(AccountsException.class, String.format("There is already an account with adhar:%s", adharNumber),
                            String.format("%s of %s", location, methodName));
            }
            case ADD_ACC -> {
                location="Inside ADD_ACC";
                final int MAX_PERMISSIBLE_ACCOUNT = 5;
                final Customer customer = accounts.getCustomer();

                Predicate<Customer> checkUnhappyPathConditionForOpeningNewAccount = customers -> customers.getAccounts().size() >= MAX_PERMISSIBLE_ACCOUNT;
                if (checkUnhappyPathConditionForOpeningNewAccount.test(customer))
                    throw new AccountsException(AccountsException.class,
                            "You can;t have more than 10 accounts",
                            String.format("%s of %s", location, methodName));
            }
            case UPDATE_CASH_LIMIT -> {
                location="Inside UPDATE_CASH_LIMIT";
                if (Period.between(accounts.getCreatedDate(), LocalDate.now()).getMonths() < 6)
                    throw new AccountsException(AccountsException.class, "Your account must be at least 6 months old to update cash Limit",
                            String.format("%s of %s", location, methodName));
            }
            case UPLOAD_PROFILE_IMAGE -> {
                location="Inside UPLOAD_PROFILE_IMAGE";
                if (Objects.isNull(customerDto.customerImage()))
                    throw new BadApiRequestException(BadApiRequestException.class,
                            "Please provide image", String.format("%s of %s", methodName, location));
                final double FIlE_SIZE_TO_MB_CONVERTER_FACTOR = 0.00000095367432;

                Predicate<CustomerDto> checkUnhappyPathConditionForUploadingProfileImage = customer -> customer.customerImage().getSize() * FIlE_SIZE_TO_MB_CONVERTER_FACTOR <= 0.0 || customer.customerImage().getSize() * FIlE_SIZE_TO_MB_CONVERTER_FACTOR > 100.0;
                if (checkUnhappyPathConditionForUploadingProfileImage.test(customerDto))
                    throw new BadApiRequestException(BadApiRequestException.class,
                            "Your file is either corrupted or you are exceeding the max size of 100mb",
                            String.format("%s of %s", methodName, location));
            }
            case UPDATE_HOME_BRANCH -> {
                location="Inside UPDATE_HOME_BRANCH";
                if(Objects.isNull(accounts.getHomeBranch()) ||
                        Objects.isNull(accounts.getAccountType())) throw new BadApiRequestException(BadApiRequestException.class,
                        "homebranch or accountype ccanot be null",
                        String.format("%s 0f %s",location,methodName));

                Customer customer= mapToCustomer(customerDto);
                accounts.setCustomer(customer);
                IValidationService.checkConflictingAccountUpdateConditionForBranch(accounts,
                        String.format("%s of %s", location, methodName));
            }
            case CLOSE_ACCOUNT -> {
                location="Inside CLOSE_ACCOUNT";
                final AllConstantHelpers.AccountStatus status = accounts.getAccountStatus();

                if (accounts.getAnyActiveLoans())
                    throw new AccountsException(AccountsException.class, String.format("This account with id %s still has " +
                            "running loan. Please consider paying it before closing", accounts.getAccountNumber()), String.format("%s of %s", location, methodName));

                switch (status) {
                    case CLOSED ->
                            throw new AccountsException(AccountsException.class, String.format("Account: %s is already closed", accounts.getAccountNumber()), String.format("%s of %s", location, methodName));
                    case BLOCKED ->
                            throw new AccountsException(AccountsException.class, String.format("Cant perform anything on Blocked account:%s", accounts.getAccountNumber()), String.format("%s of %s", location, methodName));
                    case OPEN -> {
                        return;
                    }
                }
            }
            case RE_OPEN_ACCOUNT -> {
                location="Inside RE_OPEN_ACCOUNT";
                final AllConstantHelpers.AccountStatus status = accounts.getAccountStatus();
                switch (status) {
                    case CLOSED -> {
                        return;
                    }
                    case BLOCKED ->
                            throw new AccountsException(AccountsException.class, String.format("Cant perform anything on Blocked account:%s, Please contact the admin department", accounts.getAccountNumber()), String.format("%s of %s", location, methodName));
                    case OPEN ->
                            throw new AccountsException(AccountsException.class, String.format("Status of Account: %s is already Open", accounts.getAccountNumber()), String.format("%s of %s", location, methodName));
                }
            }
            case BLOCK_ACCOUNT -> {
                location="Inside BLOCK_ACCOUNT";
                if (accounts.getAccountStatus().equals(STATUS_BLOCKED))
                    throw new AccountsException(AccountsException.class,
                            String.format("Status of Account: %s is already Blocked",
                                    accounts.getAccountStatus()), String.format("%s of %s", location, methodName));
            }
            case GET_ALL_ACC -> {
                location="GET_ALL_ACC";
                if (CollectionUtils.isEmpty(customerDto.accounts()))
                    throw new AccountsException(AccountsException.class,
                            "No accounts found", String.format("%s of %s", location, methodName));
            }
            case UPDATE_CUSTOMER_DETAILS -> {
                location="UPDATE_CUSTOMER_DETAILS";
                if (Objects.isNull(customerDto)) throw new CustomerException(CustomerException.class,
                        "Please specify a customer id to update details", String.format("%s of %s", location, methodName));
            }
            case GET_ALL_CUSTOMER -> {
                location="GET_ALL_CUSTOMER";
                if(Objects.isNull(customerDto)) throw  new CustomerException(CustomerException.class,
                        "No customer provided",String.format("%s of %s",location,methodName));
            }
        }

        log.debug("<-----------------accountsUpdateValidator(Accounts,CustomerDto,ValidateType) ValidationServiceImpl ended -----------------------" +
                "----------" +
                "-------------------------------------------------------------------------------------------------------------------------->");
    }

    @Override
    public void beneficiaryUpdateValidator(final Accounts accounts, final BeneficiaryDto beneficiaryDto, final validateBenType type) throws BeneficiaryException {
        log.debug("<----beneficiaryUpdateValidator(Accounts,BeneficiaryDto, validateBenType) ValidationServiceImpl started -----------------------------------" +
                "------------------------------------------------------------------------------------------------------>");
        final String methodName = "beneficiaryUpdateValidator(Accounts,BeneficiaryDto,validateBenType) in ValidationServiceImpl";
        final String location;
        switch (type) {
            case ADD_BEN -> {
                location = "Inside ADD_BEN";
                boolean notPossible;
                final Set<Beneficiary> listOfBeneficiaries = accounts.getListOfBeneficiary();
                if (listOfBeneficiaries.size() >= 5) throw new BeneficiaryException(BeneficiaryException.class,
                        "You can't add more than 5 beneficiaries", String.format("%s of %s", location, methodName));

                BiPredicate<Accounts, BeneficiaryDto> checkUnhappyConditionForAddingBeneficiaryCompulsoryFields = (acc, ben) ->
                        ben.benAdharNumber().equalsIgnoreCase(acc.getCustomer().getAdharNumber()) ||
                                ben.beneficiaryEmail().equalsIgnoreCase(acc.getCustomer().getEmail()) ||
                                ben.benPanNumber().equalsIgnoreCase(acc.getCustomer().getPanNumber()) ||
                                ben.benPhoneNumber().equalsIgnoreCase(acc.getCustomer().getPhoneNumber());

                BiPredicate<Accounts, BeneficiaryDto> checkUnhappyConditionForAddingBeneficiaryOptionalFields = (acc, ben) -> {
                    final String voterId = acc.getCustomer().getVoterId();
                    final String drivingLicense = acc.getCustomer().getDrivingLicense();
                    final String passport = acc.getCustomer().getPassportNumber();

                    final String newVoterId = ben.benVoterId();
                    final String newDrivingLicense = ben.benDrivingLicense();
                    final String newPassportNumber = ben.benPassportNumber();

                    return (isNotBlank(voterId) && voterId.equalsIgnoreCase(newVoterId))
                            || (isNotBlank(drivingLicense) && drivingLicense.equalsIgnoreCase(newDrivingLicense))
                            || (isNotBlank(passport) && passport.equalsIgnoreCase(newPassportNumber));
                };

                //you can't add yourself as your beneficiary  ,
                // check by mandatory as well as optional fields
                // if any matches then either yr info is incorrect or
                //you already have this as beneficiary
                if (checkUnhappyConditionForAddingBeneficiaryCompulsoryFields.or(checkUnhappyConditionForAddingBeneficiaryOptionalFields).test(accounts, beneficiaryDto))
                    throw new BeneficiaryException(BeneficiaryException.class, "You can't add yourself as beneficiary",
                            String.format("%s of %s", location, methodName));


                Predicate<Beneficiary> checkWheteherBeneficiaryAlreadyPresentCompulSoryFields = (ben) ->
                        ben.getBenAdharNumber().equalsIgnoreCase(beneficiaryDto.benAdharNumber()) ||
                                ben.getBeneficiaryEmail().equalsIgnoreCase(beneficiaryDto.beneficiaryEmail()) ||
                                ben.getBenPanNumber().equalsIgnoreCase(beneficiaryDto.benPanNumber()) ||
                                ben.getBenPhoneNumber().equalsIgnoreCase(beneficiaryDto.benPhoneNumber());

                Predicate<Beneficiary> checkWhetherBeneficiaryAlreadyPresentOptionalFields=(ben)->{
                    final String voterId=beneficiaryDto.benVoterId();
                    final String drivingLicense=beneficiaryDto.benDrivingLicense();
                    final String passPortNumber=beneficiaryDto.benPassportNumber();

                    return ( isNotBlank(voterId) && voterId.equalsIgnoreCase(ben.getBenVoterId())) ||
                            ( isNotBlank(drivingLicense) && drivingLicense.equalsIgnoreCase(ben.getBenDrivingLicense())) ||
                            (isNotBlank(passPortNumber) && passPortNumber.equalsIgnoreCase(ben.getBenDrivingLicense()));
                };

                //check if the same person already exist as a beneficiary
                notPossible = listOfBeneficiaries.stream().anyMatch(ben ->
                        checkWheteherBeneficiaryAlreadyPresentCompulSoryFields
                                .or(checkWhetherBeneficiaryAlreadyPresentOptionalFields)
                                .test(ben));
                if (notPossible) throw new BeneficiaryException(BeneficiaryException.class,
                        "This person is already added as a beneficiary", String.format("%s of %s", location, methodName));

                switch (beneficiaryDto.relation()) {
                    case FATHER -> {
                        notPossible = listOfBeneficiaries.stream().anyMatch(ben -> ben.getRelation().equals(FATHER));
                        if (notPossible) throw new BeneficiaryException(BeneficiaryException.class,
                                "You already have added one person as a father", String.format("%s of %s", location, methodName));
                    }
                    case MOTHER -> {
                        notPossible = listOfBeneficiaries.stream().anyMatch(ben -> ben.getRelation().equals(MOTHER));
                        if (notPossible) throw new BeneficiaryException(BeneficiaryException.class,
                                "You already have added one person as a mother", String.format("%s of %s", location, methodName));
                    }
                    case SPOUSE -> {
                        notPossible = listOfBeneficiaries.stream().anyMatch(ben -> ben.getRelation().equals(SPOUSE));
                        if (notPossible) throw new BeneficiaryException(BeneficiaryException.class,
                                "You already have added one person as a spouse", String.format("%s of %s", location, methodName));
                    }
                }

            }
            case UPDATE_BEN -> {
                location = "Inside UPDATE_BEN";
                boolean isTrue;

                Predicate<String> guardClauseForEmptyStringCheck = StringUtils::isNotBlank;
                Predicate<Object> guardClauseForEmptyObjectCheck = Objects::nonNull;

                if (guardClauseForEmptyObjectCheck.test(beneficiaryDto.BenDate_Of_Birth())) {
                    isTrue = Pattern.matches(PATTERN_FOR_DOB, beneficiaryDto.BenDate_Of_Birth().toString());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give DOB in YYYY-mm-dd format",
                                String.format("%s of %s", location, methodName));
                }

                if (guardClauseForEmptyStringCheck.test(beneficiaryDto.beneficiaryEmail())) {
                    isTrue = Pattern.matches(PATTERN_FOR_EMAIL, beneficiaryDto.beneficiaryEmail());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give email in valid format",
                                String.format("%s of %s", location, methodName));
                }

                if (guardClauseForEmptyStringCheck.test(beneficiaryDto.benPhoneNumber())) {
                    isTrue = Pattern.matches(PATTERN_FOR_PHONE_NUMBER, beneficiaryDto.benPhoneNumber());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give phone Number in valid format e.g +xx-xxxxxxxxxx",
                                String.format("%s of %s", location, methodName));
                }
                if (guardClauseForEmptyStringCheck.test(beneficiaryDto.benAdharNumber())) {
                    isTrue = Pattern.matches(PATTERN_FOR_ADHAR, beneficiaryDto.benAdharNumber());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give adhar number in valid xxxx-xxxx-xxxx format",
                                String.format("%s of %s", location, methodName));
                }
                if (guardClauseForEmptyStringCheck.test(beneficiaryDto.benPanNumber())) {
                    isTrue = Pattern.matches(PATTERN_FOR_PAN_NUMBER, beneficiaryDto.benPanNumber());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give pan number in valid format",
                                String.format("%s of %s", location, methodName));
                }
                if (guardClauseForEmptyStringCheck.test(beneficiaryDto.benPassportNumber())) {
                    isTrue = Pattern.matches(PATTERN_FOR_PASSPORT, beneficiaryDto.benPassportNumber());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give passport number in valid format",
                                String.format("%s of %s", location, methodName));
                }
                if (guardClauseForEmptyStringCheck.test(beneficiaryDto.benVoterId())) {
                    isTrue = Pattern.matches(PATTERN_FOR_VOTER, beneficiaryDto.benVoterId());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give voter in valid format",
                                String.format("%s of %s", location, methodName));
                }
                if (guardClauseForEmptyStringCheck.test(beneficiaryDto.benDrivingLicense())) {
                    isTrue = Pattern.matches(PATTERN_FOR_DRIVING_LICENSE, beneficiaryDto.benDrivingLicense());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give driving license in valid format",
                                String.format("%s of %s", location, methodName));
                }
            }
            case DELETE_BEN -> {
                location = "Inside Delete Ben";
                String beneficiaryId = beneficiaryDto.beneficiaryId();
                if (CollectionUtils.isEmpty(accounts.getListOfBeneficiary())) throw new BeneficiaryException(BeneficiaryException.class,
                        "Account has no beneficiaries to delete", String.format("%s of %s", location, methodName));

                //filter out the beneficiary to be deleted for that account
                final Beneficiary filteredBeneficiary = accounts.getListOfBeneficiary().
                        stream().filter(beneficiary -> beneficiaryId.equalsIgnoreCase(beneficiary.getBeneficiaryId())).
                        findFirst()
                        .orElseThrow(()-> new BeneficiaryException(BeneficiaryException.class,
                        String.format("No such beneficiaries with id %s exist for this account", beneficiaryId)
                        , String.format("%s of %s", location, methodName)));

                //your account must need to have at least one  beneficiary
                if (accounts.getListOfBeneficiary().size() == 1)
                    throw new BeneficiaryException(BeneficiaryException.class,
                            "Your account must have at least one beneficiary", String.format("%s of %s", location, methodName));
            }
            default -> throw new BeneficiaryException(BeneficiaryException.class,
                    "Invalid type of Validation request", methodName);
        }
        log.debug("<-------------------beneficiaryUpdateValidator(Accounts,BeneficiaryDto, validateBenType) ValidationServiceImpl ended ---------------------" +
                "--------------------------------------------------------------------------------------------------->");
    }

    @Override
    public void transactionsUpdateValidator(final Accounts accounts, final TransactionsDto transactionsDto,final BankStatement bankStatement, final AllConstantHelpers.ValidateTransactionType type){
        log.debug("<----transactionsUpdateValidator(Accounts,TransactionsDto, ValidateTransactionType) BeneficiaryServiceImpl started -----------------------------------" +
                "------------------------------------------------------------------------------------------------------>");
        final String methodName = "transactionsUpdateValidator(Accounts,validateBenType) in ValidationServiceImpl";
        final String location;
        switch (type){
            case GEN_BANK_STATEMENT -> {
                   location="GEN_BANK_STATEMENT";
                   final Set<Transactions> setOfTransactions=bankStatement.getListOfTransaction();
                   final LocalDate startDate=bankStatement.getStartDate();
                   final LocalDate endDate=bankStatement.getEndDate();
                   if(CollectionUtils.isEmpty(setOfTransactions)) throw new TransactionException(TransactionException.class,
                           String.format("You have no transactions between %s & %s",startDate,endDate),
                           String.format("Inside %s of %s",location,methodName));
            }
            case GET_PAST_SIX_MONTHS_TRANSACTIONS -> {
                location="GET_PAST_SIX_MONTHS_TRANSACTIONS";
                final Set<Transactions> transactionsSet=accounts.getListOfTransactions();
                if(CollectionUtils.isEmpty(transactionsSet)) throw new TransactionException(TransactionException.class,
                        String.format("No transactions available for account with id:%s",accounts.getAccountNumber())
                        ,String.format("Inside %s of %s",location,methodName));
            }
        }
        log.debug("<----transactionsUpdateValidator(Accounts,TransactionsDto, ValidateTransactionType) BeneficiaryServiceImpl ended -----------------------------------" +
                "------------------------------------------------------------------------------------------------------>");
    }

    @Override
    public void fieldValidator(String customerId,String field,ValidateField validateField){
        final String methodName="fieldValidator(String,ValidateField)";
        Iterable<Customer> listOfCustomers= customerRepository.findAll();
        BiPredicate<Customer,String> checkWhetherSameCustomer=(customer,id)-> id.equalsIgnoreCase(customer.getCustomerId());

        Pattern pattern=null;
        Matcher matcher=null;
        switch (validateField){
            case PAN -> {
                pattern= Pattern.compile(PATTERN_FOR_PAN_NUMBER);
                matcher=pattern.matcher(field);
                boolean isMatched=matcher.matches();
                if(!isMatched) throw new BadApiRequestException(BadApiRequestException.class,String.format("The pan number %s you " +
                        "entered is invalid",field),methodName);

                if(Iterables.isEmpty(listOfCustomers)) return;
                boolean isAnother=StreamSupport
                           .stream(listOfCustomers.spliterator(),false)
                           .anyMatch(customer-> field.equals(customer.getPanNumber())
                                   && !checkWhetherSameCustomer.test(customer,customerId));
                if(isAnother) throw new BadApiRequestException(BadApiRequestException.class,
                           String.format("There exists another customer with same pan %s",field),methodName);
            }
            case EMAIL -> {
                pattern= Pattern.compile(PATTERN_FOR_EMAIL);
                matcher=pattern.matcher(field);
                boolean isMatched=matcher.matches();
                if(!isMatched) throw new BadApiRequestException(BadApiRequestException.class,String.format("The email id %s you " +
                        "entered is invalid",field),methodName);

                if(Iterables.isEmpty(listOfCustomers)) return;
                boolean isAnother=StreamSupport
                        .stream(listOfCustomers.spliterator(),false)
                        .anyMatch(customer-> field.equals(customer.getEmail())
                                && !checkWhetherSameCustomer.test(customer,customerId));
                if(isAnother) throw new BadApiRequestException(BadApiRequestException.class,
                        String.format("There exists another customer with same email %s",field),methodName);
            }
            case PASSPORT -> {
                pattern= Pattern.compile(PATTERN_FOR_PASSPORT);
                matcher=pattern.matcher(field);
                boolean isMatched=matcher.matches();
                if(!isMatched) throw new BadApiRequestException(BadApiRequestException.class,String.format("The passport number %s you " +
                        "entered is invalid",field),methodName);

                if(Iterables.isEmpty(listOfCustomers)) return;
                boolean isAnother=StreamSupport
                        .stream(listOfCustomers.spliterator(),false)
                        .anyMatch(customer-> field.equals(customer.getPassportNumber()) &&
                                !checkWhetherSameCustomer.test(customer,customerId));
                if(isAnother) throw new BadApiRequestException(BadApiRequestException.class,
                        String.format("There exists another customer with same passport %s",field),methodName);
            }
            case VOTER -> {
                pattern= Pattern.compile(PATTERN_FOR_VOTER);
                matcher=pattern.matcher(field);
                boolean isMatched=matcher.matches();
                if(!isMatched) throw new BadApiRequestException(BadApiRequestException.class,String.format("The voter id %s you " +
                        "entered is invalid",field),methodName);

                if(Iterables.isEmpty(listOfCustomers)) return;
                boolean isAnother=StreamSupport
                        .stream(listOfCustomers.spliterator(),false)
                        .anyMatch(customer-> field.equals(customer.getVoterId()) &&
                                !checkWhetherSameCustomer.test(customer,customerId));

                if(isAnother) throw new BadApiRequestException(BadApiRequestException.class,
                        String.format("There exists another customer with same voter id %s",field),methodName);
            }
            case DRIVING_LICENSE -> {
                pattern=Pattern.compile(PATTERN_FOR_DRIVING_LICENSE);
                matcher=pattern.matcher(field);
                boolean isMatched=matcher.matches();
                if(!isMatched) throw new BadApiRequestException(BadApiRequestException.class,String.format("The Driving License  %s you " +
                        "entered is invalid",field),methodName);
                if(Iterables.isEmpty(listOfCustomers)) return;
                boolean isAnother=StreamSupport
                        .stream(listOfCustomers.spliterator(),false)
                        .anyMatch(customer-> field.equals(customer.getDrivingLicense())
                                && !checkWhetherSameCustomer.test(customer,customerId));

                if(isAnother) throw new BadApiRequestException(BadApiRequestException.class,
                        String.format("There exists another customer with same driving license %s",field),methodName);
            }
            case PHONE -> {
                pattern= Pattern.compile(PATTERN_FOR_PHONE_NUMBER);
                matcher=pattern.matcher(field);
                boolean isMatched=matcher.matches();
                if(!isMatched) throw new BadApiRequestException(BadApiRequestException.class,String.format("The Phone number  %s you " +
                        "entered is invalid",field),methodName);

                if(Iterables.isEmpty(listOfCustomers)) return;
                boolean isAnother=StreamSupport
                        .stream(listOfCustomers.spliterator(),false)
                        .anyMatch(customer-> field.equals(customer.getPhoneNumber())
                                && !checkWhetherSameCustomer.test(customer,customerId));
                if(isAnother) throw new BadApiRequestException(BadApiRequestException.class,
                        String.format("There exists another customer with same phone %s",field),methodName);
            }
            case ADHAR -> {
                pattern= Pattern.compile(PATTERN_FOR_ADHAR);
                matcher=pattern.matcher(field);
                boolean isMatched=matcher.matches();
                if(!isMatched) throw new BadApiRequestException(BadApiRequestException.class,String.format("The adhar number  %s you " +
                        "entered is invalid",field),methodName);

                if(Iterables.isEmpty(listOfCustomers)) return;
                boolean isAnother=StreamSupport
                        .stream(listOfCustomers.spliterator(),false)
                        .anyMatch(customer-> field.equals(customer.getAdharNumber())
                                && !checkWhetherSameCustomer.test(customer,customerId));
                if(isAnother) throw new BadApiRequestException(BadApiRequestException.class,
                        String.format("There exists another customer with same adhar %s",field),methodName);
            }
            case DOB->{
                pattern= Pattern.compile(PATTERN_FOR_DOB);
                matcher=pattern.matcher(field);
                boolean isMatched=matcher.matches();
                if(!isMatched) throw new BadApiRequestException(BadApiRequestException.class,String.format("The birth date  %s you " +
                        "entered is invalid",field),methodName);
            }
        }

    }
}
