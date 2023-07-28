package com.siliconvalley.accountsservices.service.impl;

import com.siliconvalley.accountsservices.dto.baseDtos.AccountsDto;
import com.siliconvalley.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.siliconvalley.accountsservices.dto.baseDtos.CustomerDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.BadApiRequestException;
import com.siliconvalley.accountsservices.exception.BeneficiaryException;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.helpers.MapperHelper;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.Beneficiary;
import com.siliconvalley.accountsservices.model.Customer;
import com.siliconvalley.accountsservices.repository.IAccountsRepository;
import com.siliconvalley.accountsservices.service.IValidationService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.*;
import static com.siliconvalley.accountsservices.helpers.MapperHelper.mapToBeneficiaryDto;
import static com.siliconvalley.accountsservices.helpers.RegexMatchersHelper.*;
import static java.util.Objects.isNull;
import  org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import scala.tools.nsc.doc.html.HtmlTags;

@Service
@Primary
@Slf4j
public final class ValidationServiceImpl implements IValidationService {
    private final IAccountsRepository accountsRepository;
    ValidationServiceImpl(final IAccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    @Override
    public Boolean accountsUpdateValidator(final Accounts accounts, final AccountsDto accountsDto, final CustomerDto customerDto, final AccountsValidateType request) throws AccountsException, BadApiRequestException {
        log.debug("<---------------updateValidator(Accounts,AccountsDto,CustomerDto,ValidateType) AccountsServiceImpl started -----------------------------------" +
                "------------------------------------------------------------------------------------------------------------------------>");
        final String methodName = "updateValidator(Accounts,ValidateType) in AccountsServiceImpl";
        final StringBuffer location=new StringBuffer(500) ;
        switch (request) {
            case CREATE_ACC -> {
                location.append("Inside CREATE_ACC");
                location.trimToSize();
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
                location.append("Inside ADD_ACC");
                location.trimToSize();
                final int MAX_PERMISSIBLE_ACCOUNT = 5;
                final Customer customer = accounts.getCustomer();

                Predicate<Customer> checkUnhappyPathConditionForOpeningNewAccount=customers-> customers.getAccounts().size()>=MAX_PERMISSIBLE_ACCOUNT;
                if (checkUnhappyPathConditionForOpeningNewAccount.test(customer))
                    throw new AccountsException(AccountsException.class,
                            "You can;t have more than 10 accounts",
                            String.format("%s of %s", location, methodName));
            }
            case UPDATE_CASH_LIMIT -> {
                return Period.between(accounts.getCreatedDate(), LocalDate.now()).getMonths() >= 6;
            }
            case UPLOAD_PROFILE_IMAGE -> {
                location.append("Inside UPLOAD_PROFILE_IMAGE");
                location.trimToSize();
                if (isNull(customerDto.getCustomerImage()))
                    throw new BadApiRequestException(BadApiRequestException.class,
                            "Please provide image", String.format("%s of %s", methodName, location));
                final double FIlE_SIZE_TO_MB_CONVERTER_FACTOR = 0.00000095367432;

                Predicate<CustomerDto> checkUnhappyPathConditionForUploadingProfileImage=customer->customer.getCustomerImage().getSize() * FIlE_SIZE_TO_MB_CONVERTER_FACTOR <= 0.0 || customer.getCustomerImage().getSize() * FIlE_SIZE_TO_MB_CONVERTER_FACTOR > 100.0;
                if (checkUnhappyPathConditionForUploadingProfileImage.test(customerDto))
                    throw new BadApiRequestException(BadApiRequestException.class,
                            "Your file is either corrupted or you are exceeding the max size of 100mb",
                            String.format("%s of %s", methodName, location));
            }
            case UPDATE_HOME_BRANCH -> {
                location.append("Inside UPDATE_HOME_BRANCH");
                location.trimToSize();
                return IValidationService.checkConflictingAccountUpdateConditionForBranch(accounts,
                        accountsDto, String.format("%s of %s", location, methodName));
            }
            case CLOSE_ACCOUNT -> {
                location.append("Inside CLOSE_ACCOUNT");
                location.trimToSize();
                final AllConstantHelpers.AccountStatus status = accounts.getAccountStatus();
                switch (status) {
                    case CLOSED ->
                            throw new AccountsException(AccountsException.class, String.format("Account: %s is already closed", accounts.getAccountNumber()),String.format("%s of %s", location, methodName));
                    case BLOCKED ->
                            throw new AccountsException(AccountsException.class, String.format("Cant perform anything on Blocked account:%s", accounts.getAccountNumber()),String.format("%s of %s", location, methodName));
                    case OPEN -> {
                        return accounts.getAnyActiveLoans();
                    }
                }
            }
            case RE_OPEN_ACCOUNT -> {
                location.append("Inside RE_OPEN_ACCOUNT");
                location.trimToSize();
                final AllConstantHelpers.AccountStatus status = accounts.getAccountStatus();
                switch (status) {
                    case CLOSED -> {return true;}
                    case BLOCKED -> throw new AccountsException(AccountsException.class, String.format("Cant perform anything on Blocked account:%s", accounts.getAccountNumber()), String.format("%s of %s", location, methodName));
                    case OPEN -> throw new AccountsException(AccountsException.class, String.format("Status of Account: %s is already Open", accounts.getAccountNumber()), String.format("%s of %s", location, methodName));
                }
            }
            case BLOCK_ACCOUNT -> {
                location.append("Inside BLOCK_ACCOUNT");
                location.trimToSize();
                if (accounts.getAccountStatus().equals(STATUS_BLOCKED))
                    throw new AccountsException(AccountsException.class,
                            String.format("Status of Account: %s is already Blocked",
                                    accounts.getAccountStatus()),String.format("%s of %s", location, methodName));
                return true;
            }
        }

        log.debug("<-----------------updateValidator(Accounts,AccountsDto,CustomerDto,ValidateType) AccountsServiceImpl ended -----------------------" +
                "----------" +
                "-------------------------------------------------------------------------------------------------------------------------->");
        return false;
    }

    @Override
    public void beneficiaryUpdateValidator(final Accounts accounts, final BeneficiaryDto beneficiaryDto, final validateBenType type) throws BeneficiaryException {
        log.debug("<----validate(Accounts,BeneficiaryDto, validateBenType) BeneficiaryServiceImpl started -----------------------------------" +
                "------------------------------------------------------------------------------------------------------>");
        final String methodName = "validate(Accounts,validateBenType) in BeneficiaryServiceImpl";
        final StringBuffer location=new StringBuffer(500);
        switch (type) {
            case ADD_BEN -> {
                location.append("Inside ADD_BEN");
                location.trimToSize();
                boolean notPossible;
                final List<Beneficiary> listOfBeneficiaries = accounts.getListOfBeneficiary();
                if (listOfBeneficiaries.size() >= 5) throw new BeneficiaryException(BeneficiaryException.class,
                        "You can't add more than 5 beneficiaries", String.format("%s of %s", location, methodName));

                BiPredicate<Accounts,BeneficiaryDto> checkUnhappyConditionForAddingBeneficiaryCompulsoryFields=(acc,ben)-> ben.getBenAdharNumber().equalsIgnoreCase(acc.getCustomer().getAdharNumber()) ||
                        ben.getBeneficiaryEmail().equalsIgnoreCase(acc.getCustomer().getEmail()) ||
                        ben.getBenPanNumber().equalsIgnoreCase(acc.getCustomer().getPanNumber()) ||
                        ben.getBenPhoneNumber().equalsIgnoreCase(acc.getCustomer().getPhoneNumber());

                BiPredicate<Accounts,BeneficiaryDto> checkUnhappyConditionForAddingBeneficiaryOptionalFields=(acc,ben)->{
                    final String voterId = acc.getCustomer().getVoterId();
                    final String drivingLicense = acc.getCustomer().getDrivingLicense();
                    final String passport = acc.getCustomer().getPassportNumber();

                    final String newVoterId = ben.getBenVoterId();
                    final String newDrivingLicense = ben.getBenDrivingLicense();
                    final String newPassportNumber = ben.getBenPassportNumber();

                    return (StringUtils.isNotBlank(voterId) && voterId.equalsIgnoreCase(newVoterId))
                            || (StringUtils.isNotBlank(drivingLicense) && drivingLicense.equalsIgnoreCase(newDrivingLicense))
                            || (StringUtils.isNotBlank(passport) && passport.equalsIgnoreCase(newPassportNumber));
                };
                //you can't add yourself as your beneficiary  ,
                // check by mandatory as well as optional fields
                // if any matches then either yr info is incorrect or
                //you already have this as beneficiary
                if (checkUnhappyConditionForAddingBeneficiaryCompulsoryFields.and(checkUnhappyConditionForAddingBeneficiaryOptionalFields).test(accounts,beneficiaryDto))
                    throw new BeneficiaryException(BeneficiaryException.class, "You can't add yourself as beneficiary",
                            String.format("%s of %s", location, methodName));


                //check if the same person already exist as a beneficiary
                notPossible = listOfBeneficiaries.stream().anyMatch(ben ->checkUnhappyConditionForAddingBeneficiaryCompulsoryFields.and(checkUnhappyConditionForAddingBeneficiaryOptionalFields).test(accounts, mapToBeneficiaryDto(ben)));
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
                location.append("Inside UPDATE_BEN");
                location.trimToSize();
                boolean isTrue;

                Predicate<String> guardClauseForEmptyStringCheck= StringUtils::isNotBlank;
                Predicate<Object> guardClauseForEmptyObjectCheck= Objects::nonNull;

                if (guardClauseForEmptyObjectCheck.test(beneficiaryDto.getBenDate_Of_Birth())) {
                    isTrue = Pattern.matches(PATTERN_FOR_DOB, beneficiaryDto.getBenDate_Of_Birth().toString());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give DOB in YYYY-mm-dd format",
                                String.format("%s of %s", location, methodName));
                }

                if (guardClauseForEmptyStringCheck.test(beneficiaryDto.getBeneficiaryEmail())) {
                    isTrue = Pattern.matches(PATTERN_FOR_EMAIL, beneficiaryDto.getBeneficiaryEmail());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give email in valid format",
                                String.format("%s of %s", location, methodName));
                }

                if (guardClauseForEmptyStringCheck.test(beneficiaryDto.getBenPhoneNumber())) {
                    isTrue = Pattern.matches(PATTERN_FOR_PHONE_NUMBER, beneficiaryDto.getBenPhoneNumber());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give phone Number in valid format e.g +xx-xxxxxxxxxx",
                                String.format("%s of %s", location, methodName));
                }
                if (guardClauseForEmptyStringCheck.test(beneficiaryDto.getBenAdharNumber())) {
                    isTrue = Pattern.matches(PATTERN_FOR_ADHAR, beneficiaryDto.getBenAdharNumber());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give adhar number in valid xxxx-xxxx-xxxx format",
                                String.format("%s of %s", location, methodName));
                }
                if (guardClauseForEmptyStringCheck.test(beneficiaryDto.getBenPanNumber())) {
                    isTrue = Pattern.matches(PATTERN_FOR_PAN_NUMBER, beneficiaryDto.getBenPanNumber());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give pan number in valid format",
                                String.format("%s of %s", location, methodName));
                }
                if (guardClauseForEmptyStringCheck.test(beneficiaryDto.getBenPassportNumber())) {
                    isTrue = Pattern.matches(PATTERN_FOR_PASSPORT, beneficiaryDto.getBenPassportNumber());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give passport number in valid format",
                                String.format("%s of %s", location, methodName));
                }
                if (guardClauseForEmptyStringCheck.test(beneficiaryDto.getBenVoterId())) {
                    isTrue = Pattern.matches(PATTERN_FOR_VOTER, beneficiaryDto.getBenVoterId());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give voter in valid format",
                                String.format("%s of %s", location, methodName));
                }
                if (guardClauseForEmptyStringCheck.test(beneficiaryDto.getBenDrivingLicense())) {
                    isTrue = Pattern.matches(PATTERN_FOR_DRIVING_LICENSE, beneficiaryDto.getBenDrivingLicense());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give driving license in valid format",
                                String.format("%s of %s", location, methodName));
                }
            }
            case DELETE_BEN -> {
                location.append("Inside Delete Ben");
                location.trimToSize();
                String beneficiaryId= beneficiaryDto.getBeneficiaryId();
                if(CollectionUtils.isEmpty(accounts.getListOfBeneficiary())) throw new BeneficiaryException(BeneficiaryException.class,
                        "Account has no beneficiaries to delete",String.format("%s of %s",location,methodName));

                //filter out the beneficiary to be deleted for that account
                final Optional<Beneficiary> filteredBeneficiary= accounts.getListOfBeneficiary().
                        stream().filter(beneficiary -> !beneficiaryId.equalsIgnoreCase(beneficiary.getBeneficiaryId())).
                        findFirst();
                if(filteredBeneficiary.isEmpty()) throw new BeneficiaryException(BeneficiaryException.class,
                        String.format("No such beneficiaries with id %s exist for this account",beneficiaryId)
                        ,String.format("%s of %s",location,methodName));

                //your account must need to have at least one  beneficiary
                if(accounts.getListOfBeneficiary().size()==1) throw new BeneficiaryException(BeneficiaryException.class,
                        "Your account must have at least one beneficiary",String.format("%s of %s",location,methodName));
            }
            default -> throw new BeneficiaryException(BeneficiaryException.class,
                    "Invalid type of Validation request", methodName);
        }
        log.debug("<-------------------validate(Accounts, BeneficiaryDto, validateBenType) BeneficiaryServiceImpl ended ---------------------" +
                "--------------------------------------------------------------------------------------------------->");
    }
}
