package com.example.accountsservices.service.impl;


import com.example.accountsservices.dto.baseDtos.AccountsDto;
import com.example.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.example.accountsservices.dto.baseDtos.CustomerDto;
import com.example.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.example.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.dto.responseDtos.PageableResponseDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BadApiRequestException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.helpers.AllConstantHelpers;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.repository.IAccountsRepository;
import com.example.accountsservices.repository.IBeneficiaryRepository;
import com.example.accountsservices.repository.ICustomerRepository;
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
import java.util.*;

import static com.example.accountsservices.helpers.AllConstantHelpers.validateBenType.ADD_BEN;
import static com.example.accountsservices.helpers.AllConstantHelpers.validateBenType.UPDATE_BEN;
import static com.example.accountsservices.helpers.AllConstantHelpers.DIRECTION;
import static com.example.accountsservices.helpers.CodeRetrieverHelper.getBankCode;
import static com.example.accountsservices.helpers.MapperHelper.*;
import static com.example.accountsservices.helpers.PagingHelper.*;

@Slf4j
@Service("beneficiaryServicePrimary")
public class BeneficiaryServiceImpl extends AbstractAccountsService implements IBeneficiaryService {
    private final IBeneficiaryRepository beneficiaryRepository;
    private final IAccountsRepository accountsRepository;
    private final IValidationService validationService;




    BeneficiaryServiceImpl(IAccountsRepository accountsRepository,
                           ICustomerRepository customerRepository,
                           IBeneficiaryRepository beneficiaryRepository,
                           IValidationService validationService) {
        super(accountsRepository, customerRepository);
        this.accountsRepository = accountsRepository;
        this.beneficiaryRepository = beneficiaryRepository;
        this.validationService=validationService;
    }

    private Beneficiary setBeneficiaryAgeFromDOB(final Beneficiary beneficiary) {
        log.debug("<-------------------setBeneficiaryAgeFromDOB(Beneficiary) BeneficiaryServiceImpl started ---------------------------------" +
                "------------------------------------------------------------------------------------------------------>");
        //initialize the age of beneficiaries
        final LocalDate dob = beneficiary.getBenDate_Of_Birth();
        final LocalDate now=LocalDate.now();
        final int age=Period.between(dob,now).getYears();
        beneficiary.setBenAge(age);
        log.debug("<---------------setBeneficiaryAgeFromDOB(Beneficiary) BeneficiaryServiceImpl ended ----------------------------------------" +
                "------------------------------------------------------------------------------------------------------->");
        return beneficiary;
    }

    //For validating Unhappy Paths

    private Beneficiary addBeneficiary(final Accounts fetchedAccount,final BeneficiaryDto beneficiaryDto) throws AccountsException, BeneficiaryException {
        log.debug("<-----------------addBeneficiary(Accounts,BeneficiaryDto) BeneficiaryServiceImpl started ---------------------------------" +
                "---------------------------------------------------------------------------------------------->");
        final String methodName="addBeneficiary(Accounts,BeneficiaryDto) in BeneficiaryServiceImpl";

        //validate
        validationService.beneficiaryUpdateValidator(fetchedAccount, beneficiaryDto, ADD_BEN);
        //Updating the beneficiary info & saving it
        final Beneficiary beneficiaryAccount = mapToBeneficiary(beneficiaryDto);
        beneficiaryAccount.setBankCode(getBankCode(beneficiaryAccount.getBenBank()));
        final Beneficiary processedBeneficiaryAccount = setBeneficiaryAgeFromDOB(beneficiaryAccount);
        //establishing the beneficiary as child of Account and vice versa
        final List<Beneficiary> beneficiaryList = new ArrayList<>();
        beneficiaryList.add(processedBeneficiaryAccount);
        fetchedAccount.setListOfBeneficiary(beneficiaryList);
        processedBeneficiaryAccount.setAccounts(fetchedAccount);
        //set beneficiary id
        final String beneficiaryId= UUID.randomUUID().toString();
        processedBeneficiaryAccount.setBeneficiaryId(beneficiaryId);
        //sav & return
        final Accounts savedAccounts=accountsRepository.save(fetchedAccount);
        final Optional<Beneficiary> createdBeneficiary=savedAccounts.getListOfBeneficiary().stream().
                filter(ben->ben.getBeneficiaryEmail().
                        equalsIgnoreCase(processedBeneficiaryAccount.getBeneficiaryEmail()))
                        .findFirst();
        if(createdBeneficiary.isEmpty()) throw new BeneficiaryException(BeneficiaryException.class,"Faced problem while saving your beneficiary",methodName);
        log.debug("<-------------addBeneficiary(Accounts,BeneficiaryDto) BeneficiaryServiceImpl ended ----------------------------------------" +
                "------------------------------------------------------------------------------------------------->");
        return createdBeneficiary.get();
    }

    private Optional<Beneficiary> getBeneficiaryById(final Accounts fetchedAccount,final String benId) throws BeneficiaryException {
        log.debug("<-----------------getBeneficiaryById(Accounts, Long ) BeneficiaryServiceImpl started ----------------------------------" +
                "---------------------------------------------------------------------------------------------->");
        final String methodName = "getBeneficiaryById(Accounts,Long";
        if (fetchedAccount.getListOfBeneficiary().size() == 0)
            throw new BeneficiaryException(BeneficiaryException.class,
                    "No beneficiaries found for this account", methodName);
        log.debug("<-------------------------getBeneficiaryById(Accounts, Long) BeneficiaryServiceImpl ended --------------------------------" +
                "--------------------------------------------------------------------------------------------->");
        return fetchedAccount.getListOfBeneficiary().stream().
                filter(ben -> ben.getBeneficiaryId().equalsIgnoreCase(benId)).findFirst();
    }

    private PageableResponseDto<BeneficiaryDto> getAllBeneficiariesOfAnAccountByAccountNumber(final Accounts fetchedAccount,final Pageable pageable) throws AccountsException, BeneficiaryException {
        log.debug("<------------- getAllBeneficiariesOfAnAccountByAccountNumber(Accounts,Pageable) BeneficiaryServiceImpl started -------------" +
                "--------------------------------------------------------------------------------------------------->");
        final String methodName = "getAllAccountsByCustomerId(Account,Pageable) in BeneficiaryServiceImpl";
        final Optional<Page<Beneficiary>> allPagedBeneficiary = beneficiaryRepository.findAllByAccounts_AccountNumber(fetchedAccount.getAccountNumber(), pageable);
        if (allPagedBeneficiary.isEmpty())
            throw new BeneficiaryException(BeneficiaryException.class,
                    String.format("No such beneficiary present for this account ben id: %s", fetchedAccount.getAccountNumber()), methodName);
        log.debug("<---------getAllBeneficiariesOfAnAccountByAccountNumber(Accounts fetchedAccount,Pageable pageable) BeneficiaryServiceImpl ended --------------------------------------" +
                "----------------------------------------------------------------------------------------------->");
        return getPageableResponse(allPagedBeneficiary.get(), BeneficiaryDto.class);
    }

    private Beneficiary processedBeneficiaryAccount(final Beneficiary oldBeneficiaryData,final Beneficiary newBeneficiaryData) throws AccountsException {
        log.debug("<--------------processedBeneficiaryAccount(Beneficiary, Beneficiary) BeneficiaryServiceImpl started ----------------------------" +
                "-------------------------------------------------------------------------------------------------------->");
        final String newBeneficiaryName = newBeneficiaryData.getBeneficiaryName();
        final String newBeneficiaryNumber = newBeneficiaryData.getBeneficiaryAccountNumber();
        final LocalDate newBeneficiaryDOB = newBeneficiaryData.getBenDate_Of_Birth();
        final String newBeneficiaryAdharNumber = newBeneficiaryData.getBenAdharNumber();
        final AllConstantHelpers.RELATION newBeneficaryRelation = newBeneficiaryData.getRelation();
        final String newBeneficiaryPanNumber = newBeneficiaryData.getBenPanNumber();
        final String newBeneficiaryPassport = newBeneficiaryData.getBenPassportNumber();
        final String newBeneficiaryVoterId = newBeneficiaryData.getBenVoterId();
        final AllConstantHelpers.BanksSupported newBenBank=newBeneficiaryData.getBenBank();
        final String newBeneficiaryPhoneNumber=newBeneficiaryData.getBenPhoneNumber();
        final String newBeneficiaryEmail=newBeneficiaryData.getBeneficiaryEmail();
        final String newBenDrivingLicense=newBeneficiaryData.getBenDrivingLicense();

        final String oldBeneficiaryName = oldBeneficiaryData.getBeneficiaryName();
        final String oldBeneficiaryNumber = oldBeneficiaryData.getBeneficiaryAccountNumber();
        final LocalDate oldBeneficiaryDOB = oldBeneficiaryData.getBenDate_Of_Birth();
        final String oldBeneficiaryAdharNumber = oldBeneficiaryData.getBenAdharNumber();
        final AllConstantHelpers.RELATION oldBeneficiaryRelation = oldBeneficiaryData.getRelation();
        final String oldBeneficiaryPanNumber = oldBeneficiaryData.getBenPanNumber();
        final String oldBeneficiaryPassport = oldBeneficiaryData.getBenPassportNumber();
        final String oldBeneficiaryVoterId = oldBeneficiaryData.getBenVoterId();
        final AllConstantHelpers.BanksSupported oldBenBank=oldBeneficiaryData.getBenBank();
        final String oldBeneficiaryPhoneNumber=oldBeneficiaryData.getBenPhoneNumber();
        final String oldBeneficiaryEmail=oldBeneficiaryData.getBeneficiaryEmail();
        final String oldDrivingLicense=oldBeneficiaryData.getBenDrivingLicense();

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
    private Beneficiary updateBeneficiaryDetailsOfAnAccount(final Accounts fetchedAccounts,final BeneficiaryDto beneficiaryDto) throws AccountsException, BeneficiaryException {
        log.debug("<---------------updateBeneficiaryDetailsOfAnAccount(Accounts, BeneficiaryDto) BeneficiaryServiceImpl started -----------------" +
                "--------------------------------------------------------------------------------------------------------->");
        final String methodName = "updateBeneficiaryDetailsOfAnAccount(Long , BeneficiaryDto ) in BeneficiaryServiceImpl";

        //validate
        validationService.beneficiaryUpdateValidator(fetchedAccounts, beneficiaryDto, UPDATE_BEN);

        //fetch the beneficiary from beneficiaryList
        final String BENEFICIARY_ID = beneficiaryDto.getBeneficiaryId();
        if (null == BENEFICIARY_ID) throw new BeneficiaryException(BeneficiaryException.class,
                "Please enter a valid beneficiary id", methodName);
        final Optional<Beneficiary> beneficiaryAccount = fetchedAccounts.getListOfBeneficiary().stream().
                filter(beneficiary -> BENEFICIARY_ID.equalsIgnoreCase(beneficiary.getBeneficiaryId())).
                findFirst();
        if (beneficiaryAccount.isEmpty())
            throw new BeneficiaryException(BeneficiaryException.class,
                    String.format("No such beneficiary accounts exist with beneficiary id %s",
                            BENEFICIARY_ID), methodName);

        //update
        final Beneficiary newBeneficiaryData = mapToBeneficiary(beneficiaryDto);
        final Beneficiary processedAccount = processedBeneficiaryAccount(beneficiaryAccount.get(), newBeneficiaryData);
        log.debug("<---------------updateBeneficiaryDetailsOfAnAccount(Accounts, BeneficiaryDto) BeneficiaryServiceImpl ended ----------------" +
                "------------------------------------------------------------------------------------------------------>");
        return beneficiaryRepository.save(processedAccount);
    }

    private void deleteBeneficiariesForAnAccount(final Accounts fetchedAccounts,final String beneficiaryId) throws AccountsException, BeneficiaryException {
        log.debug("<---------------deleteBeneficiariesForAnAccount(Accounts, Long) BeneficiaryServiceImpl started ----------------------" +
                "------------------------------------------------------------------------------------------------>");
        final String methodName = " deleteBeneficiariesForAnAccount(Long , Long )  in BeneficiaryServiceImpl";
        if (null == beneficiaryId)
            throw new BeneficiaryException(BeneficiaryException.class, "Please provide a valid beneficiary id", methodName);

        //filter out the beneficiary list of that account
        final List<Beneficiary> filteredListOfBeneficiaries = fetchedAccounts.getListOfBeneficiary().
                stream().
                filter(beneficiary -> !beneficiaryId.equalsIgnoreCase(beneficiary.getBeneficiaryId())).
                toList();
        //set the final filtered resultant list to that account
        fetchedAccounts.setListOfBeneficiary(filteredListOfBeneficiaries);
        //delete that beneficiary
        beneficiaryRepository.deleteByBeneficiaryId(beneficiaryId);
        log.debug("<-------------deleteBeneficiariesForAnAccount(Accounts, Long ) BeneficiaryServiceImpl ended ------------------------" +
                "------------------------------------------------------------------------------------------------>");
    }


    private void deleteAllBeneficiaries(final Accounts fetchedAccounts) throws AccountsException {
        log.debug("<------------deleteAllBeneficiaries(Accounts) BeneficiaryServiceImpl started ----------------------------------------" +
                "----------------------------------------------------------------------------------------------->");
        //delete everyone
        beneficiaryRepository.deleteAllByAccounts_AccountNumber(fetchedAccounts.getAccountNumber());
        log.debug("<------------deleteAllBeneficiaries(Accounts ) BeneficiaryServiceImpl ended ------------------------------------------" +
                "------------------------------------------------------------------------------------------------>");
    }

    private PageableResponseDto<BeneficiaryDto> beneficiaryPagination(final DIRECTION sortDir,final String sortBy,final int pageNumber,final int pageSize,final Accounts fetchedAccount) throws BadApiRequestException, BeneficiaryException, AccountsException {
        log.debug("<------------beneficiaryPagination(DIRECTION,String,int ,int ,Accounts ) BeneficiaryServiceImpl started -----------------------------------" +
                "----------------------------------------------------------------------------------------------------------->");
        final String methodName="beneficiaryPagination(DIRECTION,String,int,int,Accounts) in AccountsServiceImpl";
        final Sort sort = sortDir.equals(PAGE_SORT_DIRECTION_ASCENDING) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        final PageableResponseDto<BeneficiaryDto> pageableResponseDto = getAllBeneficiariesOfAnAccountByAccountNumber(fetchedAccount,pageable);

        if (pageableResponseDto.getContent().size() == 0)
            throw new BadApiRequestException(BadApiRequestException.class,
                    String.format("Account with id %s have no beneficiary present", fetchedAccount.getAccountNumber()),
                    methodName);

        log.debug("<-------------beneficiaryPagination(DIRECTION,String,int,int ,Accounts ) BeneficiaryServiceImpl ended --------------------------------------" +
                "----------------------------------------------------------------------------------------------------------->");
        return  pageableResponseDto;
    }

    @Override
    public OutputDto postRequestBenExecutor(final PostInputRequestDto postInputDto) throws BeneficiaryException, AccountsException {
        final String methodName = "postRequestBenExecutor(InputDto) in BeneficiaryServiceImpl";
        final BeneficiaryDto beneficiaryDto = mapInputDtoToBenDto(postInputDto);

        //get the account
        final String accountNumber = postInputDto.getAccountNumber();
        final Accounts fetchedAccount = fetchAccountByAccountNumber(accountNumber);
        final AccountsDto accountsDto = mapToAccountsDto(fetchedAccount);

        //get the customer
        final Customer customer = fetchedAccount.getCustomer();
        final CustomerDto customerDto = mapToCustomerDto(customer);

        final AllConstantHelpers.BenUpdateRequest requestType = postInputDto.getBenRequest();
        if (null == requestType) throw new BeneficiaryException(BeneficiaryException.class,
                "Please provide a non null request-type", methodName);
        switch (requestType) {
            case ADD_BEN -> {
                final Beneficiary beneficiary = addBeneficiary(fetchedAccount, beneficiaryDto);

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
    public OutputDto putRequestBenExecutor(final PutInputRequestDto putInputRequestDto) throws BeneficiaryException, AccountsException {
        final String methodName = "putRequestBenExecutor(InputDto) in BeneficiaryServiceImpl";
        final BeneficiaryDto beneficiaryDto = mapPutInputRequestDtoToBenDto(putInputRequestDto);

        //get the account
        final String accountNumber = putInputRequestDto.getAccountNumber();
        final Accounts fetchedAccount = fetchAccountByAccountNumber(accountNumber);

        //get customer
        final Customer loadCustomer = fetchedAccount.getCustomer();
        final AllConstantHelpers.BenUpdateRequest requestType = putInputRequestDto.getBenRequest();
        if (null == requestType) throw new BeneficiaryException(BeneficiaryException.class,
                "Please provide a non null request-type", methodName);
        switch (requestType) {
            case UPDATE_BEN -> {
                final Beneficiary loadedBeneficiary = updateBeneficiaryDetailsOfAnAccount(fetchedAccount, beneficiaryDto);
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
    public OutputDto getRequestBenExecutor(final GetInputRequestDto getInputRequestDto) throws AccountsException, BeneficiaryException, BadApiRequestException {
        final String methodName = "getRequestBenExecutor(InputDto) in BeneficiaryServiceImpl";
        final BeneficiaryDto beneficiaryDto = mapGetRequestInputDtoToBenDto(getInputRequestDto);

        //get paging details
        final int pageNumber = getInputRequestDto.getPageNumber();
        if (pageNumber < 0) throw new BadApiRequestException(BadApiRequestException.class,
                "pageNumber cant be in negative", methodName);

        if (getInputRequestDto.getPageSize() < 0)
            throw new BadApiRequestException(BadApiRequestException.class, "Page Size can't be in negative", methodName);
        final int pageSize = (getInputRequestDto.getPageSize() == 0) ? DEFAULT_PAGE_SIZE : getInputRequestDto.getPageSize();

        final String sortBy = (null == getInputRequestDto.getSortBy()) ? "beneficiaryName" : getInputRequestDto.getSortBy();
        final DIRECTION sortDir = (null == getInputRequestDto.getSortDir()) ? DIRECTION.asc : getInputRequestDto.getSortDir();


        //get the account
        final String accountNumber = getInputRequestDto.getAccountNumber();
        final Accounts fetchedAccount = fetchAccountByAccountNumber(accountNumber);

        final AllConstantHelpers.BenUpdateRequest requestType = getInputRequestDto.getBenRequest();
        if (null == requestType) throw new BeneficiaryException(BeneficiaryException.class,
                "Please provide a non null request-type", methodName);

        StringBuilder location;
        switch (requestType) {
            case GET_BEN -> {
                location=new StringBuilder("Inside GET_BEN");
                final Optional<Beneficiary> beneficiary = getBeneficiaryById(fetchedAccount, beneficiaryDto.getBeneficiaryId());
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
                location =new StringBuilder("Inside GET_ALL_BEN");
                //validate the genuineness of sorting fields
                final Set<String> allPageableFieldsOfAccounts = getAllPageableFieldsOfBeneficiary();
                if (!allPageableFieldsOfAccounts.contains(sortBy))
                    throw new BadApiRequestException(BadApiRequestException.class,
                            String.format("%s is not a valid field of account", sortBy), String.format("Inside %s of %s", location, methodName));
                //paging & sorting
                final PageableResponseDto<BeneficiaryDto> pageableResponseDto=beneficiaryPagination(sortDir,sortBy,pageNumber,pageSize,fetchedAccount);


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
    public OutputDto deleteRequestBenExecutor(final DeleteInputRequestDto deleteInputRequestDto) throws BeneficiaryException, AccountsException {
        final String methodName = "deleteRequestBenExecutor(InputDto) in BeneficiaryServiceImpl";
        final BeneficiaryDto beneficiaryDto = mapDeleteInputRequestDtoToBenDto(deleteInputRequestDto);

        //get the account
        final String accountNUmber = deleteInputRequestDto.getAccountNumber();
        final Accounts fetchedAccount = fetchAccountByAccountNumber(accountNUmber);

        final AllConstantHelpers.BenUpdateRequest requestType = deleteInputRequestDto.getBenRequest();
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
