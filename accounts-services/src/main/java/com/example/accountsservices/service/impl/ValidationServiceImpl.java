package com.example.accountsservices.service.impl;

import com.example.accountsservices.dto.baseDtos.AccountsDto;
import com.example.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.example.accountsservices.dto.baseDtos.CustomerDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BadApiRequestException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.helpers.AllConstantHelpers;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.repository.IAccountsRepository;
import com.example.accountsservices.service.IValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

import static com.example.accountsservices.helpers.AllConstantHelpers.*;
import static com.example.accountsservices.helpers.RegexMatchersHelper.*;

@Service
@Primary
@Slf4j
public class ValidationServiceImpl implements IValidationService {
    private final IAccountsRepository accountsRepository;
    ValidationServiceImpl(IAccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    @Override
    public Boolean accountsUpdateValidator(final Accounts accounts,final AccountsDto accountsDto,final CustomerDto customerDto,final AccountsValidateType request) throws AccountsException, BadApiRequestException {
        log.debug("<---------------updateValidator(Accounts,AccountsDto,CustomerDto,ValidateType) AccountsServiceImpl started -----------------------------------" +
                "------------------------------------------------------------------------------------------------------------------------>");
        final String methodName = "updateValidator(Accounts,ValidateType) in AccountsServiceImpl";
        StringBuilder location=new StringBuilder();
        switch (request) {
            case CREATE_ACC -> {
                location =new StringBuilder("Inside CREATE_ACC");
                //check whether such account owner is already present
                final List<Accounts> accountsList = accountsRepository.findAll();
                //if no accounts by far then certainly we can add
                if (accountsList.isEmpty()) return true;

                final String adharNumber = accounts.getCustomer().getAdharNumber();
                final boolean isNotPossible = accountsList.stream().anyMatch(acc -> adharNumber.equalsIgnoreCase(acc.getCustomer().getAdharNumber()));
                if (isNotPossible)
                    throw new AccountsException(AccountsException.class, String.format("You already have an account with adhar:%s", adharNumber),
                            String.format("%s of %s", location, methodName));
            }
            case ADD_ACC -> {
                location=new StringBuilder("Inside ADD_ACC");
                final int MAX_PERMISSIBLE_ACCOUNT = 5;
                //prevent a customer to create more than 10 accounts
                final Customer customer = accounts.getCustomer();
                if (customer.getAccounts().size() >= MAX_PERMISSIBLE_ACCOUNT)
                    throw new AccountsException(AccountsException.class,
                            "You can;t have more than 10 accounts",
                            String.format("%s of %s", location, methodName));
            }
            case UPDATE_CASH_LIMIT -> {
                return Period.between(accounts.getCreatedDate(), LocalDate.now()).getMonths() >= 6;
            }
            case UPLOAD_PROFILE_IMAGE -> {
                location =new StringBuilder("Inside UPLOAD_PROFILE_IMAGE");
                if (null == customerDto.getCustomerImage())
                    throw new BadApiRequestException(BadApiRequestException.class,
                            "Please provide image", String.format("%s of %s", methodName, location));
                final double FIlE_SIZE_TO_MB_CONVERTER_FACTOR = 0.00000095367432;
                if (customerDto.getCustomerImage().getSize() * FIlE_SIZE_TO_MB_CONVERTER_FACTOR <= 0.0 || customerDto.getCustomerImage().getSize() * FIlE_SIZE_TO_MB_CONVERTER_FACTOR > 100.0)
                    throw new BadApiRequestException(BadApiRequestException.class,
                            "Your file is either corrupted or you are exceeding the max size of 100mb",
                            String.format("%s of %s", methodName, location));
            }
            case UPDATE_HOME_BRANCH -> {
                location=new StringBuilder("Inside UPDATE_HOME_BRANCH");
                return checkConflictingAccountUpdateConditionForBranch(accounts,
                        accountsDto, String.format("%s of %s", location, methodName));
            }
            case CLOSE_ACCOUNT -> {
                final AllConstantHelpers.AccountStatus status = accounts.getAccountStatus();
                switch (status) {
                    case CLOSED ->
                            throw new AccountsException(AccountsException.class, String.format("Account: %s is already closed", accounts.getAccountNumber()), location.toString());
                    case BLOCKED ->
                            throw new AccountsException(AccountsException.class, String.format("Cant perform anything on Blocked account:%s", accounts.getAccountNumber()), location.toString());
                    case OPEN -> {
                        return accounts.getAnyActiveLoans();
                    }
                }
            }
            case RE_OPEN_ACCOUNT -> {
                final AllConstantHelpers.AccountStatus status = accounts.getAccountStatus();
                switch (status) {
                    case CLOSED -> {
                        return true;
                    }
                    case BLOCKED ->
                            throw new AccountsException(AccountsException.class, String.format("Cant perform anything on Blocked account:%s", accounts.getAccountNumber()), location.toString());
                    case OPEN ->
                            throw new AccountsException(AccountsException.class, String.format("Status of Account: %s is already Open", accounts.getAccountNumber()), location.toString());
                }
            }
            case BLOCK_ACCOUNT -> {
                if (accounts.getAccountStatus().equals(STATUS_BLOCKED))
                    throw new AccountsException(AccountsException.class,
                            String.format("Status of Account: %s is already Blocked",
                                    accounts.getAccountStatus()), location.toString());
                return true;
            }
        }

        log.debug("<-----------------updateValidator(Accounts,AccountsDto,CustomerDto,ValidateType) AccountsServiceImpl ended -----------------------" +
                "----------" +
                "-------------------------------------------------------------------------------------------------------------------------->");
        return false;
    }

    @Override
    public void beneficiaryUpdateValidator(final Accounts accounts,final BeneficiaryDto beneficiaryDto,final validateBenType type) throws BeneficiaryException {
        log.debug("<----validate(Accounts,BeneficiaryDto, validateBenType) BeneficiaryServiceImpl started -----------------------------------" +
                "------------------------------------------------------------------------------------------------------>");
        final String methodName = "validate(Accounts,validateBenType) in BeneficiaryServiceImpl";
        StringBuilder location;
        switch (type) {
            case ADD_BEN -> {
                location = new StringBuilder("Inside ADD_BEN");
                boolean notPossible;
                final List<Beneficiary> listOfBeneficiaries = accounts.getListOfBeneficiary();
                if (listOfBeneficiaries.size() >= 5) throw new BeneficiaryException(BeneficiaryException.class,
                        "You can't add more than 5 beneficiaries", String.format("%s of %s", location, methodName));

                //mandatory fields
                final String email = accounts.getCustomer().getEmail();
                final String adharNumber = accounts.getCustomer().getAdharNumber();
                final String panNumber = accounts.getCustomer().getPanNumber();
                final String phoneNumber = accounts.getCustomer().getPhoneNumber();

                //not  mandatory  fields
                final String voterId = accounts.getCustomer().getVoterId();
                final String drivingLicense = accounts.getCustomer().getDrivingLicense();
                final String passport = accounts.getCustomer().getPassportNumber();


                //new incoming fields
                final String newEmail = beneficiaryDto.getBeneficiaryEmail();
                final String newAdharNumber = beneficiaryDto.getBenAdharNumber();
                final String newPanNumber = beneficiaryDto.getBenPanNumber();
                final String newPhoneNumber = beneficiaryDto.getBenPhoneNumber();

                final String newVoterId = beneficiaryDto.getBenVoterId();
                final String newDrivingLicense = beneficiaryDto.getBenDrivingLicense();
                final String newPassportNumber = beneficiaryDto.getBenPassportNumber();

                //you can't add yourself as your beneficiary  ,
                // check by mandatory as well as optional fields
                // if any matches then either yr info is incorrect or
                //you already have this as beneficiary
                if (adharNumber.equalsIgnoreCase(newAdharNumber) ||
                        email.equalsIgnoreCase(newEmail) ||
                        panNumber.equalsIgnoreCase(newPanNumber) ||
                        phoneNumber.equalsIgnoreCase(newPhoneNumber) ||
                        (null != voterId && voterId.equalsIgnoreCase(newVoterId))
                        || (null != drivingLicense && drivingLicense.equalsIgnoreCase(newDrivingLicense))
                        || (null != passport && passport.equalsIgnoreCase(newPassportNumber))
                )
                    throw new BeneficiaryException(BeneficiaryException.class, "You can't add yourself as beneficiary",
                            String.format("%s of %s", location, methodName));


                //check if the same person already exist as a beneficiary
                notPossible = listOfBeneficiaries.stream().anyMatch(ben ->
                        ben.getBenAdharNumber().equalsIgnoreCase(newAdharNumber)
                                || ben.getBeneficiaryEmail().equalsIgnoreCase(newEmail)
                                || ben.getBenPanNumber().equalsIgnoreCase(newPanNumber)
                                || ben.getBenPhoneNumber().equalsIgnoreCase(newPhoneNumber)
                                || (null != newVoterId && ben.getBenVoterId().equalsIgnoreCase(newVoterId))
                                || (null != newDrivingLicense && ben.getBenDrivingLicense().equalsIgnoreCase(newDrivingLicense))
                                || (null != newPassportNumber && ben.getBenPassportNumber().equalsIgnoreCase(newPassportNumber)));
                if (notPossible) throw new BeneficiaryException(BeneficiaryException.class,
                        "This person is already added as a beneficiary", String.format("%s of %s", location, methodName));

                switch (beneficiaryDto.getRelation()) {
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
                location = new StringBuilder("Inside UPDATE_BEN");
                boolean isTrue;
                if (null != beneficiaryDto.getBenDate_Of_Birth()) {
                    isTrue = Pattern.matches(PATTERN_FOR_DOB, beneficiaryDto.getBenDate_Of_Birth().toString());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give DOB in YYYY-mm-dd format",
                                String.format("%s of %s", location, methodName));
                }

                if (null != beneficiaryDto.getBeneficiaryEmail()) {
                    isTrue = Pattern.matches(PATTERN_FOR_EMAIL, beneficiaryDto.getBeneficiaryEmail());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give email in valid format",
                                String.format("%s of %s", location, methodName));
                }

                if (null != beneficiaryDto.getBenPhoneNumber()) {
                    isTrue = Pattern.matches(PATTERN_FOR_PHONE_NUMBER, beneficiaryDto.getBenPhoneNumber());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give phone Number in valid format e.g +xx-xxxxxxxxxx",
                                String.format("%s of %s", location, methodName));
                }
                if (null != beneficiaryDto.getBenAdharNumber()) {
                    isTrue = Pattern.matches(PATTERN_FOR_ADHAR, beneficiaryDto.getBenAdharNumber());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give adhar number in valid xxxx-xxxx-xxxx format",
                                String.format("%s of %s", location, methodName));
                }
                if (null != beneficiaryDto.getBenPanNumber()) {
                    isTrue = Pattern.matches(PATTERN_FOR_PAN_NUMBER, beneficiaryDto.getBenPanNumber());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give pan number in valid format",
                                String.format("%s of %s", location, methodName));
                }
                if (null != beneficiaryDto.getBenPassportNumber()) {
                    isTrue = Pattern.matches(PATTERN_FOR_PASSPORT, beneficiaryDto.getBenPassportNumber());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give passport number in valid format",
                                String.format("%s of %s", location, methodName));
                }
                if (null != beneficiaryDto.getBenVoterId()) {
                    isTrue = Pattern.matches(PATTERN_FOR_VOTER, beneficiaryDto.getBenVoterId());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give voter in valid format",
                                String.format("%s of %s", location, methodName));
                }
                if (null != beneficiaryDto.getBenDrivingLicense()) {
                    isTrue = Pattern.matches(PATTERN_FOR_DRIVING_LICENSE, beneficiaryDto.getBenDrivingLicense());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give driving license in valid format",
                                String.format("%s of %s", location, methodName));
                }
            }
            case DELETE_BEN -> {
            }
            default -> throw new BeneficiaryException(BeneficiaryException.class,
                    "Invalid type of request", methodName);
        }
        log.debug("<-------------------validate(Accounts, BeneficiaryDto, validateBenType) BeneficiaryServiceImpl ended ---------------------" +
                "--------------------------------------------------------------------------------------------------->");
    }
}
