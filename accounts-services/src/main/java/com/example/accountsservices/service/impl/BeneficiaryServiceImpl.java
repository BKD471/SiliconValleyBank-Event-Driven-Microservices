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
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.BeneficiaryRepository;
import com.example.accountsservices.repository.CustomerRepository;
import com.example.accountsservices.service.AbstractAccountsService;
import com.example.accountsservices.service.IBeneficiaryService;
import com.example.accountsservices.service.IValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import static com.example.accountsservices.helpers.CodeRetrieverHelper.getBankCode;
import static com.example.accountsservices.helpers.MapperHelper.*;
import static com.example.accountsservices.helpers.PagingHelper.*;
import static com.example.accountsservices.helpers.RegexMatchersHelper.*;

@Slf4j
@Service("beneficiaryServicePrimary")
public class BeneficiaryServiceImpl extends AbstractAccountsService implements IBeneficiaryService {
    private final BeneficiaryRepository beneficiaryRepository;
    private final AccountsRepository accountsRepository;

    private final IValidationService validationService;
    public enum validateBenType {
        ADD_BEN, UPDATE_BEN, DELETE_BEN
    }
    private final validateBenType ADD_BEN = validateBenType.ADD_BEN;
    private final validateBenType UPDATE_BEN = validateBenType.UPDATE_BEN;


    BeneficiaryServiceImpl(AccountsRepository accountsRepository,
                           CustomerRepository customerRepository,
                           BeneficiaryRepository beneficiaryRepository,
                           IValidationService validationService) {
        super(accountsRepository, customerRepository);
        this.accountsRepository = accountsRepository;
        this.beneficiaryRepository = beneficiaryRepository;
        this.validationService=validationService;
    }

    private Beneficiary setBeneficiaryAgeFromDOB(Beneficiary beneficiary) {
        log.debug("<-------------------setBeneficiaryAgeFromDOB(Beneficiary) BeneficiaryServiceImpl started ---------------------------------" +
                "------------------------------------------------------------------------------------------------------>");
        //initialize the age of beneficiaries
        LocalDate dob = beneficiary.getBenDate_Of_Birth();
        LocalDate now=LocalDate.now();
        int age=Period.between(dob,now).getYears();
        beneficiary.setBenAge(age);
        log.debug("<---------------setBeneficiaryAgeFromDOB(Beneficiary) BeneficiaryServiceImpl ended ----------------------------------------" +
                "------------------------------------------------------------------------------------------------------->");
        return beneficiary;
    }

    //For validating Unhappy Paths

    private Beneficiary addBeneficiary(Accounts fetchedAccount, BeneficiaryDto beneficiaryDto) throws AccountsException, BeneficiaryException {
        log.debug("<-----------------addBeneficiary(Accounts,BeneficiaryDto) BeneficiaryServiceImpl started ---------------------------------" +
                "---------------------------------------------------------------------------------------------->");
        String methodName="addBeneficiary(Accounts,BeneficiaryDto) in BeneficiaryServiceImpl";

        //validate
        validationService.beneficiaryUpdateValidator(fetchedAccount, beneficiaryDto, ADD_BEN);
        //Updating the beneficiary info & saving it
        Beneficiary beneficiaryAccount = mapToBeneficiary(beneficiaryDto);
        beneficiaryAccount.setBankCode(getBankCode(beneficiaryAccount.getBenBank()));
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
        log.debug("<-------------addBeneficiary(Accounts,BeneficiaryDto) BeneficiaryServiceImpl ended ----------------------------------------" +
                "------------------------------------------------------------------------------------------------->");
        return createdBeneficiary.get();
    }

    private Optional<Beneficiary> getBeneficiaryById(Accounts fetchedAccount, Long benId) throws BeneficiaryException {
        log.debug("<-----------------getBeneficiaryById(Accounts, Long ) BeneficiaryServiceImpl started ----------------------------------" +
                "---------------------------------------------------------------------------------------------->");
        String methodName = "getBeneficiaryById(Accounts,Long";
        if (fetchedAccount.getListOfBeneficiary().size() == 0)
            throw new BeneficiaryException(BeneficiaryException.class,
                    "No beneficiaries found for this account", methodName);
        log.debug("<-------------------------getBeneficiaryById(Accounts, Long) BeneficiaryServiceImpl ended --------------------------------" +
                "--------------------------------------------------------------------------------------------->");
        return fetchedAccount.getListOfBeneficiary().stream().
                filter(ben -> ben.getBeneficiaryId().equals(benId)).findFirst();
    }

    private PageableResponseDto<BeneficiaryDto> getAllBeneficiariesOfAnAccountByAccountNumber(Accounts fetchedAccount,Pageable pageable) throws AccountsException, BeneficiaryException {
        log.debug("<------------- getAllBeneficiariesOfAnAccountByAccountNumber(Accounts,Pageable) BeneficiaryServiceImpl started -------------" +
                "--------------------------------------------------------------------------------------------------->");
        String methodName = "getAllAccountsByCustomerId(Account,Pageable) in BeneficiaryServiceImpl";
        Optional<Page<Beneficiary>> allPagedBeneficiary = beneficiaryRepository.findAllByAccounts_AccountNumber(fetchedAccount.getAccountNumber(), pageable);
        if (allPagedBeneficiary.isEmpty())
            throw new BeneficiaryException(BeneficiaryException.class,
                    String.format("No such beneficiary present for this account ben id: %s", fetchedAccount.getAccountNumber()), methodName);
        log.debug("<---------getAllBeneficiariesOfAnAccountByAccountNumber(Accounts fetchedAccount,Pageable pageable) BeneficiaryServiceImpl ended --------------------------------------" +
                "----------------------------------------------------------------------------------------------->");
        return getPageableResponse(allPagedBeneficiary.get(), BeneficiaryDto.class);
    }

    private Beneficiary processedBeneficiaryAccount(Beneficiary oldBeneficiaryData, Beneficiary newBeneficiaryData) throws AccountsException {
        log.debug("<--------------processedBeneficiaryAccount(Beneficiary, Beneficiary) BeneficiaryServiceImpl started ----------------------------" +
                "-------------------------------------------------------------------------------------------------------->");
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
        String newBenDrivingLicense=newBeneficiaryData.getBenDrivingLicense();

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
        String oldDrivingLicense=oldBeneficiaryData.getBenDrivingLicense();

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
            oldBeneficiaryData.setBankCode(getBankCode(newBenBank));
        }

        if(null!=newBenDrivingLicense && !newBenDrivingLicense.equalsIgnoreCase(oldDrivingLicense)){
            oldBeneficiaryData.setBenDrivingLicense(newBenDrivingLicense);
        }

        log.debug("<----------processedBeneficiaryAccount(Beneficiary, Beneficiary) BeneficiaryServiceImpl ended -----------------------------" +
                "---------------------------------------------------------------------------------------------------->");
        return oldBeneficiaryData;
    }

    /**
     * @param fetchedAccounts
     * @param beneficiaryDto
     * @return
     */
    private Beneficiary updateBeneficiaryDetailsOfAnAccount(Accounts fetchedAccounts, BeneficiaryDto beneficiaryDto) throws AccountsException, BeneficiaryException {
        log.debug("<---------------updateBeneficiaryDetailsOfAnAccount(Accounts, BeneficiaryDto) BeneficiaryServiceImpl started -----------------" +
                "--------------------------------------------------------------------------------------------------------->");
        String methodName = "updateBeneficiaryDetailsOfAnAccount(Long , BeneficiaryDto ) in BeneficiaryServiceImpl";

        //validate
        validationService.beneficiaryUpdateValidator(fetchedAccounts, beneficiaryDto, UPDATE_BEN);

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
        log.debug("<---------------updateBeneficiaryDetailsOfAnAccount(Accounts, BeneficiaryDto) BeneficiaryServiceImpl ended ----------------" +
                "------------------------------------------------------------------------------------------------------>");
        return beneficiaryRepository.save(processedAccount);
    }

    private void deleteBeneficiariesForAnAccount(Accounts fetchedAccounts, Long beneficiaryId) throws AccountsException, BeneficiaryException {
        log.debug("<---------------deleteBeneficiariesForAnAccount(Accounts, Long) BeneficiaryServiceImpl started ----------------------" +
                "------------------------------------------------------------------------------------------------>");
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
        log.debug("<-------------deleteBeneficiariesForAnAccount(Accounts, Long ) BeneficiaryServiceImpl ended ------------------------" +
                "------------------------------------------------------------------------------------------------>");
    }


    private void deleteAllBeneficiaries(Accounts fetchedAccounts) throws AccountsException {
        log.debug("<------------deleteAllBeneficiaries(Accounts) BeneficiaryServiceImpl started ----------------------------------------" +
                "----------------------------------------------------------------------------------------------->");
        //delete everyone
        beneficiaryRepository.deleteAllByAccounts_AccountNumber(fetchedAccounts.getAccountNumber());
        log.debug("<------------deleteAllBeneficiaries(Accounts ) BeneficiaryServiceImpl ended ------------------------------------------" +
                "------------------------------------------------------------------------------------------------>");
    }

    private PageableResponseDto<BeneficiaryDto> beneficiaryPagination(DIRECTION sortDir,String sortBy,int pageNumber,int pageSize,Accounts fetchedAccount) throws BadApiRequestException, BeneficiaryException, AccountsException {
        log.debug("<------------beneficiaryPagination(DIRECTION,String,int ,int ,Accounts ) BeneficiaryServiceImpl started -----------------------------------" +
                "----------------------------------------------------------------------------------------------------------->");
        String methodName="beneficiaryPagination(DIRECTION,String,int,int,Accounts) in AccountsServiceImpl";
        Sort sort = sortDir.equals(PAGE_SORT_DIRECTION_ASCENDING) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        PageableResponseDto<BeneficiaryDto> pageableResponseDto = getAllBeneficiariesOfAnAccountByAccountNumber(fetchedAccount,pageable);

        if (pageableResponseDto.getContent().size() == 0)
            throw new BadApiRequestException(BadApiRequestException.class,
                    String.format("Account with id %s have no beneficiary present", fetchedAccount.getAccountNumber()),
                    methodName);

        log.debug("<-------------beneficiaryPagination(DIRECTION,String,int,int ,Accounts ) BeneficiaryServiceImpl ended --------------------------------------" +
                "----------------------------------------------------------------------------------------------------------->");
        return  pageableResponseDto;
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


                return OutputDto.builder()
                        .customer(mapToCustomerOutputDto(customerDto))
                        .accounts(mapToAccountsOutputDto(accountsDto))
                        .beneficiary(mapToBeneficiaryDto(beneficiary))
                        .defaultMessage(String.format("Beneficiary with id:%s has been added for account with id:%s",
                                beneficiary.getBeneficiaryId(),fetchedAccount.getAccountNumber())).build();
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

                return OutputDto.builder()
                        .customer(mapToCustomerOutputDto(mapToCustomerDto(loadCustomer)))
                        .accounts(mapToAccountsOutputDto(mapToAccountsDto(fetchedAccount)))
                        .beneficiary(mapToBeneficiaryDto(loadedBeneficiary))
                        .defaultMessage(String.format("beneficiaryId:%s beneficiary account has been updated",loadedBeneficiary.getBeneficiaryId()))
                        .build();
            }
            default -> throw new BeneficiaryException(BeneficiaryException.class,
                    "Wrong request type", methodName);
        }
    }

    @Override
    public OutputDto getRequestBenExecutor(GetInputRequestDto getInputRequestDto) throws AccountsException, BeneficiaryException, BadApiRequestException {
        String methodName = "getRequestBenExecutor(InputDto) in BeneficiaryServiceImpl";
        BeneficiaryDto beneficiaryDto = mapGetRequestInputDtoToBenDto(getInputRequestDto);

        //get paging details
        int pageNumber = getInputRequestDto.getPageNumber();
        if (pageNumber < 0) throw new BadApiRequestException(BadApiRequestException.class,
                "pageNumber cant be in negative", methodName);

        int pageSize = getInputRequestDto.getPageSize();
        if (pageSize < 0)
            throw new BadApiRequestException(BadApiRequestException.class, "Page Size can't be in negative", methodName);
        pageSize = (getInputRequestDto.getPageSize() == 0) ? DEFAULT_PAGE_SIZE : getInputRequestDto.getPageSize();

        String sortBy = (null == getInputRequestDto.getSortBy()) ? "beneficiaryName" : getInputRequestDto.getSortBy();
        DIRECTION sortDir = (null == getInputRequestDto.getSortDir()) ? DIRECTION.asc : getInputRequestDto.getSortDir();


        //get the account
        Long accountNumber = getInputRequestDto.getAccountNumber();
        Accounts fetchedAccount = fetchAccountByAccountNumber(accountNumber);

        BeneficiaryDto.BenUpdateRequest requestType = getInputRequestDto.getBenRequest();
        if (null == requestType) throw new BeneficiaryException(BeneficiaryException.class,
                "Please provide a non null request-type", methodName);

        String location;
        switch (requestType) {
            case GET_BEN -> {
                location = "Inside GET_BEN";
                Optional<Beneficiary> beneficiary = getBeneficiaryById(fetchedAccount, beneficiaryDto.getBeneficiaryId());
                if (beneficiary.isEmpty())
                    throw new BeneficiaryException(BeneficiaryException.class, String.format("No such beneficiaries present with id:%s",
                            beneficiaryDto.getBeneficiaryId()), String.format("%s of %s", location, methodName));

                return OutputDto.builder()
                        .customer(mapToCustomerOutputDto(mapToCustomerDto(fetchedAccount.getCustomer())))
                        .accounts(mapToAccountsOutputDto(mapToAccountsDto(fetchedAccount)))
                        .beneficiary(mapToBeneficiaryDto(beneficiary.get()))
                        .defaultMessage(String.format("Fetched beneficiary with id:%s",beneficiaryDto.getBeneficiaryId()))
                        .build();
            }
            case GET_ALL_BEN -> {
                location = "Inside GET_ALL_BEN";
                //validate the genuineness of sorting fields
                Set<String> allPageableFieldsOfAccounts = getAllPageableFieldsOfBeneficiary();
                if (!allPageableFieldsOfAccounts.contains(sortBy))
                    throw new BadApiRequestException(BadApiRequestException.class,
                            String.format("%s is not a valid field of account", sortBy), String.format("Inside %s of %s", location, methodName));
                //paging & sorting
                PageableResponseDto<BeneficiaryDto> pageableResponseDto=beneficiaryPagination(sortDir,sortBy,pageNumber,pageSize,fetchedAccount);


                return OutputDto.builder()
                        .customer(mapToCustomerOutputDto(mapToCustomerDto(fetchedAccount.getCustomer())))
                        .accounts(mapToAccountsOutputDto(mapToAccountsDto(fetchedAccount)))
                        .beneficiaryListPages(pageableResponseDto)
                        .defaultMessage(String.format("Fetched all beneficiaries of account:%s",fetchedAccount.getAccountNumber()))
                        .build();
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
                return OutputDto.builder()
                        .defaultMessage(String.format("Beneficiary with id:%s has been deleted",beneficiaryDto.getBeneficiaryId()))
                        .build();
            }
            case DELETE_ALL_BEN -> {
                deleteAllBeneficiaries(fetchedAccount);
                return OutputDto.builder()
                        .defaultMessage(String.format("All beneficiaries with id:%s have been deleted",fetchedAccount.getAccountNumber()))
                        .build();
            }
            default -> throw new BeneficiaryException(BeneficiaryException.class, "Wrong request type", methodName);
        }
    }
}
