package com.example.accountsservices.service.impl;


import com.example.accountsservices.dto.*;
import com.example.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.helpers.BankCodeRetrieverHelper;
import com.example.accountsservices.helpers.Mapper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.BeneficiaryRepository;
import com.example.accountsservices.repository.CustomerRepository;
import com.example.accountsservices.AbstractAccountsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.accountsservices.helpers.Mapper.*;
import static com.example.accountsservices.helpers.RegexMatchersHelper.*;

@Service
public class BeneficiaryServiceImpl extends AbstractAccountsService {
    private final BeneficiaryRepository beneficiaryRepository;
    private final AccountsRepository accountsRepository;
    public enum validateBenType {
        ADD_BEN, UPDATE_BEN, DELETE_BEN
    }
    private final validateBenType ADD_BEN = validateBenType.ADD_BEN;
    private final validateBenType UPDATE_BEN = validateBenType.UPDATE_BEN;
    private final validateBenType DELETE_BEN = validateBenType.DELETE_BEN;
    private final Beneficiary.RELATION FATHER = Beneficiary.RELATION.FATHER;
    private final Beneficiary.RELATION MOTHER = Beneficiary.RELATION.MOTHER;
    private final Beneficiary.RELATION SPOUSE = Beneficiary.RELATION.SPOUSE;

    BeneficiaryServiceImpl(AccountsRepository accountsRepository,
                           CustomerRepository customerRepository,
                           BeneficiaryRepository beneficiaryRepository) {
        super(accountsRepository, customerRepository);
        this.accountsRepository = accountsRepository;
        this.beneficiaryRepository = beneficiaryRepository;
    }

    private Beneficiary setBeneficiaryAgeFromDOB(Beneficiary beneficiary) {
        //initialize the age of beneficiaries
        int dobYear = beneficiary.getBenDate_Of_Birth().getYear();
        int now = LocalDate.now().getYear();
        beneficiary.setBenAge(now - dobYear);
        return beneficiary;
    }

    //For validating Unhappy Paths
    private void validate(Accounts accounts, BeneficiaryDto beneficiaryDto, validateBenType type) throws BeneficiaryException {
        String methodName = "validate(Accounts,validateBenType) in BeneficiaryServiceImpl";
        String location="";
        switch (type) {
            case ADD_BEN -> {
                location="Inside ADD_BEN";
                boolean notPossible = false;
                List<Beneficiary> listOfBeneficiaries = accounts.getListOfBeneficiary();
                if (listOfBeneficiaries.size() >= 5) throw new BeneficiaryException(BeneficiaryException.class,
                        "You can't add more than 5 beneficiaries", String.format("%s of %s",location,methodName));

                //mandatory fields
                String email=accounts.getCustomer().getEmail();
                String adharNumber=accounts.getCustomer().getAdharNumber();
                String panNumber=accounts.getCustomer().getPanNumber();
                String phoneNumber=accounts.getCustomer().getPhoneNumber();

                //not  mandatory  fields
                String voterId=accounts.getCustomer().getVoterId();
                String drivingLicense=accounts.getCustomer().getDrivingLicense();
                String passport=accounts.getCustomer().getPassportNumber();


                //new incoming fields
                String newEmail=beneficiaryDto.getBeneficiaryEmail();
                String newAdharNumber = beneficiaryDto.getBenAdharNumber();
                String newPanNumber=beneficiaryDto.getBenPanNumber();
                String newPhoneNumber=beneficiaryDto.getBenPhoneNumber();

                String newVoterId=beneficiaryDto.getBenVoterId();
                String newDrivingLicense=beneficiaryDto.getBenDrivingLicense();
                String newPassportNumber=beneficiaryDto.getBenPassportNumber();

                //you can't add yourself as your beneficiary  ,
                // check by mandatory as well as optional fields
                // if any matches then either yr info is incorrect or
                //you already have this as beneficiary
                if (adharNumber.equalsIgnoreCase(newAdharNumber) ||
                        email.equalsIgnoreCase(newEmail) ||
                        panNumber.equalsIgnoreCase(newPanNumber) ||
                        phoneNumber.equalsIgnoreCase(newPhoneNumber) ||
                 (null!=voterId && voterId.equalsIgnoreCase(newVoterId))
                        || (null!=drivingLicense && drivingLicense.equalsIgnoreCase(newDrivingLicense))
                        || (null!=passport && passport.equalsIgnoreCase(newPassportNumber))
                )
                    throw new BeneficiaryException(BeneficiaryException.class, "You can't add yourself as beneficiary",
                            String.format("%s of %s",location,methodName));


                //check if the same person already exist as a beneficiary
                notPossible = listOfBeneficiaries.stream().anyMatch(ben ->
                                ben.getBenAdharNumber().equalsIgnoreCase(newAdharNumber)
                        || ben.getBeneficiaryEmail().equalsIgnoreCase(newEmail)
                                        || ben.getBenPanNumber().equalsIgnoreCase(newPanNumber)
                                || ben.getBenPhoneNumber().equalsIgnoreCase(newPhoneNumber)
                                        || (null!=newVoterId && ben.getBenVoterId().equalsIgnoreCase(newVoterId))
                                        || (null!=newDrivingLicense && ben.getBenDrivingLicense().equalsIgnoreCase(newDrivingLicense))
                                        || (null!=newPassportNumber && ben.getBenPassportNumber().equalsIgnoreCase(newPassportNumber)));
                if (notPossible) throw new BeneficiaryException(BeneficiaryException.class,
                        "This person is already added as a beneficiary",String.format("%s of %s",location,methodName));

                switch (beneficiaryDto.getRelation()) {
                    case FATHER -> {
                        notPossible = listOfBeneficiaries.stream().anyMatch(ben -> ben.getRelation().equals(FATHER));
                        if (notPossible) throw new BeneficiaryException(BeneficiaryException.class,
                                "You already have added one person as a father", String.format("%s of %s",location,methodName));
                    }
                    case MOTHER -> {
                        notPossible = listOfBeneficiaries.stream().anyMatch(ben -> ben.getRelation().equals(MOTHER));
                        if (notPossible) throw new BeneficiaryException(BeneficiaryException.class,
                                "You already have added one person as a mother", String.format("%s of %s",location,methodName));
                    }
                    case SPOUSE -> {
                        notPossible = listOfBeneficiaries.stream().anyMatch(ben -> ben.getRelation().equals(SPOUSE));
                        if (notPossible) throw new BeneficiaryException(BeneficiaryException.class,
                                "You already have added one person as a spouse", String.format("%s of %s",location,methodName));
                    }
                }
            }
            case UPDATE_BEN -> {
                location="Inside UPDATE_BEN";
                boolean isTrue = true;
                if (null != beneficiaryDto.getBenDate_Of_Birth()) {
                    isTrue = Pattern.matches(PATTERN_FOR_DOB, beneficiaryDto.getBenDate_Of_Birth().toString());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give DOB in YYYY-mm-dd format",
                                String.format("%s of %s",location,methodName));
                }

                if (null != beneficiaryDto.getBeneficiaryEmail()) {
                    isTrue = Pattern.matches(PATTERN_FOR_EMAIL, beneficiaryDto.getBeneficiaryEmail());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give email in valid format",
                                String.format("%s of %s",location,methodName));
                }

                if (null != beneficiaryDto.getBenPhoneNumber()) {
                    isTrue = Pattern.matches(PATTERN_FOR_PHONE_NUMBER, beneficiaryDto.getBenPhoneNumber());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give phone Number in valid format e.g +xx-xxxxxxxxxx",
                                String.format("%s of %s",location,methodName));
                }
                if (null != beneficiaryDto.getBenAdharNumber()) {
                    isTrue = Pattern.matches(PATTERN_FOR_ADHAR, beneficiaryDto.getBenAdharNumber());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give adhar number in valid xxxx-xxxx-xxxx format",
                                String.format("%s of %s",location,methodName));
                }
                if (null != beneficiaryDto.getBenPanNumber()) {
                    isTrue = Pattern.matches(PATTERN_FOR_PAN_NUMBER, beneficiaryDto.getBenPanNumber());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give pan number in valid format",
                                String.format("%s of %s",location,methodName));
                }
                if (null != beneficiaryDto.getBenPassportNumber()) {
                    isTrue = Pattern.matches(PATTERN_FOR_PASSPORT, beneficiaryDto.getBenPassportNumber());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give passport number in valid format",
                                String.format("%s of %s",location,methodName));
                }
                if (null != beneficiaryDto.getBenVoterId()) {
                    isTrue = Pattern.matches(PATTERN_FOR_VOTER, beneficiaryDto.getBenVoterId());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give voter in valid format",
                                String.format("%s of %s",location,methodName));
                }
                if (null != beneficiaryDto.getBenDrivingLicense()) {
                    isTrue = Pattern.matches(PATTERN_FOR_DRIVING_LICENSE, beneficiaryDto.getBenDrivingLicense());
                    if (!isTrue)
                        throw new BeneficiaryException(BeneficiaryException.class, "Please give driving license in valid format",
                                String.format("%s of %s",location,methodName));
                }
            }
            case DELETE_BEN -> {
            }
            default -> throw new BeneficiaryException(BeneficiaryException.class,
                    "Invalid type of request", methodName);
        }
    }
    private Beneficiary addBeneficiary(Accounts fetchedAccount, BeneficiaryDto beneficiaryDto) throws AccountsException, BeneficiaryException {
        String methodName="addBeneficiary(Accounts,BeneficiaryDto) in BeneficiaryServiceImpl";

        //validate
        validate(fetchedAccount, beneficiaryDto, ADD_BEN);
        //Updating the beneficiary info & saving it
        Beneficiary beneficiaryAccount = mapToBeneficiary(beneficiaryDto);
        beneficiaryAccount.setBankCode(BankCodeRetrieverHelper.getBankCode(beneficiaryAccount.getBenBank()));
        Beneficiary processedBeneficiaryAccount = setBeneficiaryAgeFromDOB(beneficiaryAccount);
        //establishing the beneficiary as child of Account and vice versa
        List<Beneficiary> beneficiaryList = new ArrayList<>();
        beneficiaryList.add(processedBeneficiaryAccount);
        fetchedAccount.setListOfBeneficiary(beneficiaryList);
        processedBeneficiaryAccount.setAccounts(fetchedAccount);
        //sav & return
        Accounts savedAccounts=accountsRepository.save(fetchedAccount);
        Optional<Beneficiary> createdBeneficiary=savedAccounts.getListOfBeneficiary().stream().
                filter(ben->ben.getBeneficiaryEmail().
                        equalsIgnoreCase(processedBeneficiaryAccount.getBeneficiaryEmail()))
                        .findFirst();
        if(createdBeneficiary.isEmpty()) throw new BeneficiaryException(BeneficiaryException.class,"Faced problem while saving your beneficiary",methodName);
        return createdBeneficiary.get();
    }

    private Optional<Beneficiary> getBeneficiaryById(Accounts fetchedAccount, Long benId) throws BeneficiaryException {
        String methodName = "getBeneficiaryById(Accounts,Long";
        if (fetchedAccount.getListOfBeneficiary().size() == 0)
            throw new BeneficiaryException(BeneficiaryException.class,
                    "No beneficiaries found for this account", methodName);
        return fetchedAccount.getListOfBeneficiary().stream().
                filter(ben -> ben.getBeneficiaryId().equals(benId)).findFirst();
    }

    private List<Beneficiary> getAllBeneficiariesOfAnAccountByAccountNumber(Accounts fetchedAccount) throws AccountsException {
        //get all beneficiaries
        return fetchedAccount.getListOfBeneficiary();
    }

    private Beneficiary processedBeneficiaryAccount(Beneficiary oldBeneficiaryData, Beneficiary newBeneficiaryData) throws AccountsException {
        String newBeneficiaryName = newBeneficiaryData.getBeneficiaryName();
        Long newBeneficiaryNumber = newBeneficiaryData.getBeneficiaryAccountNumber();
        LocalDate newBeneficiaryDOB = newBeneficiaryData.getBenDate_Of_Birth();
        String newBeneficiaryAdharNumber = newBeneficiaryData.getBenAdharNumber();
        Beneficiary.RELATION newBeneficaryRelation = newBeneficiaryData.getRelation();
        String newBeneficiaryPanNumber = newBeneficiaryData.getBenPanNumber();
        String newBeneficiaryPassport = newBeneficiaryData.getBenPassportNumber();
        String newBeneficiaryVoterId = newBeneficiaryData.getBenVoterId();
        Beneficiary.BanksSupported newBenBank=newBeneficiaryData.getBenBank();
        String newBeneficiaryPhoneNumber=newBeneficiaryData.getBenPhoneNumber();
        String newBeneficiaryEmail=newBeneficiaryData.getBeneficiaryEmail();

        String oldBeneficiaryName = oldBeneficiaryData.getBeneficiaryName();
        Long oldBeneficiaryNumber = oldBeneficiaryData.getBeneficiaryAccountNumber();
        LocalDate oldBeneficiaryDOB = oldBeneficiaryData.getBenDate_Of_Birth();
        String oldBeneficiaryAdharNumber = oldBeneficiaryData.getBenAdharNumber();
        Beneficiary.RELATION oldBeneficiaryRelation = oldBeneficiaryData.getRelation();
        String oldBeneficiaryPanNumber = oldBeneficiaryData.getBenPanNumber();
        String oldBeneficiaryPassport = oldBeneficiaryData.getBenPassportNumber();
        String oldBeneficiaryVoterId = oldBeneficiaryData.getBenVoterId();
        Beneficiary.BanksSupported oldBenBank=oldBeneficiaryData.getBenBank();
        String oldBeneficiaryPhoneNumber=oldBeneficiaryData.getBenPhoneNumber();
        String oldBeneficiaryEmail=oldBeneficiaryData.getBeneficiaryEmail();

        if (null != newBeneficiaryName && !newBeneficiaryName.equalsIgnoreCase(oldBeneficiaryName))
            oldBeneficiaryData.setBeneficiaryName(newBeneficiaryName);

        if (null != newBeneficiaryNumber && !newBeneficiaryNumber.equals(oldBeneficiaryNumber))
            oldBeneficiaryData.setBeneficiaryAccountNumber(newBeneficiaryNumber);

        if (null != newBeneficiaryDOB && !newBeneficiaryDOB.equals(oldBeneficiaryDOB)) {
            oldBeneficiaryData.setBenDate_Of_Birth(newBeneficiaryDOB);

            //calculating & setting the new age
            LocalDate now=LocalDate.now();
            int age= Period.between(newBeneficiaryDOB,now).getYears();
            oldBeneficiaryData.setBenAge(age);
        }

        if (null != newBeneficiaryAdharNumber && !newBeneficiaryAdharNumber.equalsIgnoreCase(oldBeneficiaryAdharNumber))
            oldBeneficiaryData.setBenAdharNumber(newBeneficiaryAdharNumber);

        if (null != newBeneficaryRelation && !newBeneficaryRelation.equals(oldBeneficiaryRelation))
            oldBeneficiaryData.setRelation(newBeneficaryRelation);

        if (null != newBeneficiaryPanNumber && !newBeneficiaryPanNumber.equalsIgnoreCase(oldBeneficiaryPanNumber))
            oldBeneficiaryData.setBenPanNumber(newBeneficiaryPanNumber);

        if (null != newBeneficiaryPassport && !newBeneficiaryPassport.equalsIgnoreCase(oldBeneficiaryPassport))
            oldBeneficiaryData.setBenPassportNumber(newBeneficiaryPassport);

        if (null != newBeneficiaryVoterId && !newBeneficiaryVoterId.equalsIgnoreCase(oldBeneficiaryVoterId))
            oldBeneficiaryData.setBenVoterId(newBeneficiaryVoterId);

        if(null!=newBeneficiaryPhoneNumber && !newBeneficiaryPhoneNumber.equalsIgnoreCase(oldBeneficiaryPhoneNumber))
            oldBeneficiaryData.setBenPhoneNumber(newBeneficiaryPhoneNumber);

        if(null!=newBeneficiaryEmail && !newBeneficiaryEmail.equalsIgnoreCase(oldBeneficiaryEmail))
            oldBeneficiaryData.setBeneficiaryEmail(newBeneficiaryEmail);

        if(null!=newBenBank && !newBenBank.equals(oldBenBank))
        {
            oldBeneficiaryData.setBenBank(newBenBank);
            oldBeneficiaryData.setBankCode(BankCodeRetrieverHelper.getBankCode(newBenBank));
        }

        return oldBeneficiaryData;
    }

    /**
     * @param fetchedAccounts
     * @param beneficiaryDto
     * @return
     */
    private Beneficiary updateBeneficiaryDetailsOfAnAccount(Accounts fetchedAccounts, BeneficiaryDto beneficiaryDto) throws AccountsException, BeneficiaryException {
        String methodName = "updateBeneficiaryDetailsOfAnAccount(Long , BeneficiaryDto ) in BeneficiaryServiceImpl";

        //validate
        validate(fetchedAccounts, beneficiaryDto, UPDATE_BEN);

        //fetch the beneficiary from beneficiaryList
        Long BENEFICIARY_ID = beneficiaryDto.getBeneficiaryId();
        if (null == BENEFICIARY_ID) throw new BeneficiaryException(BeneficiaryException.class,
                "Please enter a valid beneficiary id", methodName);
        Optional<Beneficiary> beneficiaryAccount = fetchedAccounts.getListOfBeneficiary().stream().
                filter(beneficiary -> BENEFICIARY_ID.equals(beneficiary.getBeneficiaryId())).
                findFirst();
        if (beneficiaryAccount.isEmpty())
            throw new BeneficiaryException(BeneficiaryException.class,
                    String.format("No such beneficiary accounts exist with beneficiary id %s",
                            BENEFICIARY_ID), methodName);

        //update
        Beneficiary newBeneficiaryData = mapToBeneficiary(beneficiaryDto);
        Beneficiary processedAccount = processedBeneficiaryAccount(beneficiaryAccount.get(), newBeneficiaryData);
        return beneficiaryRepository.save(processedAccount);
    }

    private void deleteBeneficiariesForAnAccount(Accounts fetchedAccounts, Long beneficiaryId) throws AccountsException, BeneficiaryException {
        String methodName = " deleteBeneficiariesForAnAccount(Long , Long )  in BeneficiaryServiceImpl";
        if (null == beneficiaryId)
            throw new BeneficiaryException(BeneficiaryException.class, "Please provide a valid beneficiary id", methodName);

        //filter out the beneficiary list of that account
        List<Beneficiary> filteredListOfBeneficiaries = fetchedAccounts.getListOfBeneficiary().
                stream().
                filter(beneficiary -> !beneficiaryId.equals(beneficiary.getBeneficiaryId())).
                toList();
        //set the final filtered resultant list to that account
        fetchedAccounts.setListOfBeneficiary(filteredListOfBeneficiaries);
        //delete that beneficiary
        beneficiaryRepository.deleteByBeneficiaryId(beneficiaryId);
    }


    private void deleteAllBeneficiaries(Accounts fetchedAccounts) throws AccountsException {
        //delete everyone
        fetchedAccounts.getListOfBeneficiary().forEach(beneficiary ->
                beneficiaryRepository.deleteByBeneficiaryId(beneficiary.getBeneficiaryId()));
        //set an empty list
        fetchedAccounts.setListOfBeneficiary(new ArrayList<>());
    }

    @Override
    public OutputDto postRequestBenExecutor(PostInputRequestDto postInputDto) throws BeneficiaryException, AccountsException {
        String methodName = "postRequestBenExecutor(InputDto) in BeneficiaryServiceImpl";
        BeneficiaryDto beneficiaryDto = mapInputDtoToBenDto(postInputDto);

        //get the account
        Long accountNUmber = postInputDto.getAccountNumber();
        Accounts fetchedAccount = fetchAccountByAccountNumber(accountNUmber);
        AccountsDto accountsDto = mapToAccountsDto(fetchedAccount);

        //get the customer
        Customer customer = fetchedAccount.getCustomer();
        CustomerDto customerDto = mapToCustomerDto(customer);

        BeneficiaryDto.BenUpdateRequest requestType = postInputDto.getBenRequest();
        if (null == requestType) throw new BeneficiaryException(BeneficiaryException.class,
                "Please provide a non null request-type", methodName);
        switch (requestType) {
            case ADD_BEN -> {
                Beneficiary beneficiary = addBeneficiary(fetchedAccount, beneficiaryDto);
                return new OutputDto(mapToCustomerOutputDto(customerDto),
                        mapToAccountsOutputDto(accountsDto),
                        mapToBeneficiaryDto(beneficiary), String.format("Beneficiary with id:%s has been added for account with id:%s",
                        beneficiary.getBeneficiaryId(), fetchedAccount.getAccountNumber()));
            }
            default -> throw new BeneficiaryException(BeneficiaryException.class, "Wrong request type", methodName);
        }
    }

    @Override
    public OutputDto putRequestBenExecutor(PutInputRequestDto putInputRequestDto) throws BeneficiaryException, AccountsException {
        String methodName = "putRequestBenExecutor(InputDto) in BeneficiaryServiceImpl";
        BeneficiaryDto beneficiaryDto = mapPutInputRequestDtoToBenDto(putInputRequestDto);

        //get the account
        Long accountNumber = putInputRequestDto.getAccountNumber();
        Accounts fetchedAccount = fetchAccountByAccountNumber(accountNumber);

        //get customer
        Customer loadCustomer = fetchedAccount.getCustomer();

        BeneficiaryDto.BenUpdateRequest requestType = putInputRequestDto.getBenRequest();
        if (null == requestType) throw new BeneficiaryException(BeneficiaryException.class,
                "Please provide a non null request-type", methodName);
        switch (requestType) {
            case UPDATE_BEN -> {
                Beneficiary loadedBeneficiary = updateBeneficiaryDetailsOfAnAccount(fetchedAccount, beneficiaryDto);

                return new OutputDto(mapToCustomerOutputDto(mapToCustomerDto(loadCustomer)),
                        mapToAccountsOutputDto(mapToAccountsDto(fetchedAccount)),
                        mapToBeneficiaryDto(loadedBeneficiary),
                        String.format("beneficiaryId:%s beneficiary account has been updated", loadedBeneficiary.getBeneficiaryId()));
            }
            default -> throw new BeneficiaryException(BeneficiaryException.class,
                    "Wrong request type", methodName);
        }
    }

    @Override
    public OutputDto getRequestBenExecutor(GetInputRequestDto getInputRequestDto) throws AccountsException, BeneficiaryException {
        String methodName = "getRequestBenExecutor(InputDto) in BeneficiaryServiceImpl";
        BeneficiaryDto beneficiaryDto = mapGetRequestInputDtoToBenDto(getInputRequestDto);

        //get the account
        Long accountNUmber = getInputRequestDto.getAccountNumber();
        Accounts fetchedAccount = fetchAccountByAccountNumber(accountNUmber);

        BeneficiaryDto.BenUpdateRequest requestType = getInputRequestDto.getBenRequest();
        if (null == requestType) throw new BeneficiaryException(BeneficiaryException.class,
                "Please provide a non null request-type", methodName);

        String location = "";
        switch (requestType) {
            case GET_BEN -> {
                location = "Inside GET_BEN";
                Optional<Beneficiary> beneficiary = getBeneficiaryById(fetchedAccount, beneficiaryDto.getBeneficiaryId());
                if (beneficiary.isEmpty())
                    throw new BeneficiaryException(BeneficiaryException.class, String.format("No such beneficiaries present with id:%s",
                            beneficiaryDto.getBeneficiaryId()), String.format("%s of %s", location, methodName));

                return new OutputDto(mapToCustomerOutputDto(mapToCustomerDto(fetchedAccount.getCustomer())),
                        mapToAccountsOutputDto(mapToAccountsDto(fetchedAccount)), mapToBeneficiaryDto(beneficiary.get()),
                        String.format("Fetched beneficiary with id:%s", beneficiaryDto.getBeneficiaryId()));
            }
            case GET_ALL_BEN -> {
                location = "Inside GET_ALL_BEN";
                List<BeneficiaryDto> beneficiaryList = getAllBeneficiariesOfAnAccountByAccountNumber(fetchedAccount)
                        .stream().map(Mapper::mapToBeneficiaryDto).collect(Collectors.toList());

                return new OutputDto(mapToCustomerOutputDto(mapToCustomerDto(fetchedAccount.getCustomer())),
                        mapToAccountsOutputDto(mapToAccountsDto(fetchedAccount)), beneficiaryList,
                        String.format("Fetched all beneficiaries of account:%s", fetchedAccount.getAccountNumber()));
            }
            default -> throw new BeneficiaryException(BeneficiaryException.class,
                    "Wrong request type", methodName);
        }
    }

    @Override
    public OutputDto deleteRequestBenExecutor(DeleteInputRequestDto deleteInputRequestDto) throws BeneficiaryException, AccountsException {
        String methodName = "deleteRequestBenExecutor(InputDto) in BeneficiaryServiceImpl";
        BeneficiaryDto beneficiaryDto = mapDeleteInputRequestDtoToBenDto(deleteInputRequestDto);

        //get the account
        Long accountNUmber = deleteInputRequestDto.getAccountNumber();
        Accounts fetchedAccount = fetchAccountByAccountNumber(accountNUmber);

        BeneficiaryDto.BenUpdateRequest requestType = deleteInputRequestDto.getBenRequest();
        if (null == requestType) throw new BeneficiaryException(BeneficiaryException.class,
                "Please provide a non null request-type", methodName);
        switch (requestType) {
            case DELETE_BEN -> {
                deleteBeneficiariesForAnAccount(fetchedAccount, beneficiaryDto.getBeneficiaryId());
                return new OutputDto(String.format("Beneficiary with id:%s has been deleted",beneficiaryDto.getBeneficiaryId()));
            }
            case DELETE_ALL_BEN -> {
                deleteAllBeneficiaries(fetchedAccount);
                return new OutputDto(String.format("All beneficiaries with id:%s have been deleted",
                        fetchedAccount.getAccountNumber()));
            }
            default -> throw new BeneficiaryException(BeneficiaryException.class, "Wrong request type", methodName);
        }
    }
}
