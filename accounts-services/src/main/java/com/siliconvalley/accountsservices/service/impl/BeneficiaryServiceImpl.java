package com.siliconvalley.accountsservices.service.impl;


import com.siliconvalley.accountsservices.dto.baseDtos.AccountsDto;
import com.siliconvalley.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.siliconvalley.accountsservices.dto.baseDtos.CustomerDto;
import com.siliconvalley.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.responseDtos.PageableResponseDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.BadApiRequestException;
import com.siliconvalley.accountsservices.exception.BeneficiaryException;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.Beneficiary;
import com.siliconvalley.accountsservices.model.Customer;
import com.siliconvalley.accountsservices.repository.IAccountsRepository;
import com.siliconvalley.accountsservices.repository.IBeneficiaryRepository;
import com.siliconvalley.accountsservices.repository.ICustomerRepository;
import com.siliconvalley.accountsservices.service.AbstractService;
import com.siliconvalley.accountsservices.service.IBeneficiaryService;
import com.siliconvalley.accountsservices.service.IValidationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.DIRECTION;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.validateBenType.*;
import static com.siliconvalley.accountsservices.helpers.CodeRetrieverHelper.getBankCode;
import static com.siliconvalley.accountsservices.helpers.MapperHelper.*;
import static com.siliconvalley.accountsservices.helpers.PagingHelper.*;
import static java.util.Objects.isNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Service("beneficiaryServicePrimary")
public class BeneficiaryServiceImpl extends AbstractService implements IBeneficiaryService {
    private final IBeneficiaryRepository beneficiaryRepository;
    private final IAccountsRepository accountsRepository;
    private final IValidationService validationService;

    BeneficiaryServiceImpl(final IAccountsRepository accountsRepository,
                           final ICustomerRepository customerRepository,
                           final IBeneficiaryRepository beneficiaryRepository,
                           final IValidationService validationService) {
        super(accountsRepository, customerRepository);
        this.accountsRepository = accountsRepository;
        this.beneficiaryRepository = beneficiaryRepository;
        this.validationService=validationService;
    }

    private Beneficiary setBeneficiaryAgeFromDOB(final Beneficiary beneficiary) {
        log.debug("<-------------------setBeneficiaryAgeFromDOB(Beneficiary) BeneficiaryServiceImpl started ---------------------------------" +
                "------------------------------------------------------------------------------------------------------>");
        final LocalDate dob = beneficiary.getBenDate_Of_Birth();
        final LocalDate now=LocalDate.now();
        final int age=Period.between(dob,now).getYears();
        beneficiary.setBenAge(age);
        log.debug("<---------------setBeneficiaryAgeFromDOB(Beneficiary) BeneficiaryServiceImpl ended ----------------------------------------" +
                "------------------------------------------------------------------------------------------------------->");
        return beneficiary;
    }

    //For validating Unhappy Paths

    private Beneficiary addBeneficiary(final Accounts fetchedAccount, final BeneficiaryDto beneficiaryDto) throws AccountsException, BeneficiaryException {
        log.debug("<-----------------addBeneficiary(Accounts,BeneficiaryDto) BeneficiaryServiceImpl started ---------------------------------" +
                "---------------------------------------------------------------------------------------------->");
        final String methodName="addBeneficiary(Accounts,BeneficiaryDto) in BeneficiaryServiceImpl";

        validationService.beneficiaryUpdateValidator(fetchedAccount, beneficiaryDto, ADD_BEN);
        final Beneficiary beneficiaryAccount = mapToBeneficiary(beneficiaryDto);
        beneficiaryAccount.setBankCode(getBankCode(beneficiaryAccount.getBenBank()));
        final Beneficiary processedBeneficiaryAccount = setBeneficiaryAgeFromDOB(beneficiaryAccount);

        final Set<Beneficiary> beneficiaryList = new LinkedHashSet<>();
        beneficiaryList.add(processedBeneficiaryAccount);
        fetchedAccount.setListOfBeneficiary(beneficiaryList);
        processedBeneficiaryAccount.setAccounts(fetchedAccount);

        final String beneficiaryId= UUID.randomUUID().toString();
        processedBeneficiaryAccount.setBeneficiaryId(beneficiaryId);

        final Accounts savedAccounts=accountsRepository.save(fetchedAccount);
        final Beneficiary createdBeneficiary=savedAccounts.getListOfBeneficiary().stream().
                filter(ben->ben.getBeneficiaryEmail().
                        equalsIgnoreCase(processedBeneficiaryAccount.getBeneficiaryEmail())).findFirst()
                .orElseThrow(()-> new BeneficiaryException(BeneficiaryException.class,
                        "Faced problem while saving your beneficiary",methodName));

        log.debug("<-------------addBeneficiary(Accounts,BeneficiaryDto) BeneficiaryServiceImpl ended ----------------------------------------" +
                "------------------------------------------------------------------------------------------------->");
        return createdBeneficiary;
    }

    private Optional<Beneficiary> getBeneficiaryById(final Accounts fetchedAccount,final String benId) throws BeneficiaryException {
        log.debug("<-----------------getBeneficiaryById(Accounts, Long ) BeneficiaryServiceImpl started ----------------------------------" +
                "---------------------------------------------------------------------------------------------->");
        final String methodName = "getBeneficiaryById(Accounts,Long";
        if (isEmpty(fetchedAccount.getListOfBeneficiary()))
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
        final Page<Beneficiary> allPagedBeneficiary =
                beneficiaryRepository.findAllByAccounts_AccountNumber(fetchedAccount.getAccountNumber(), pageable)
                        .orElseThrow(()->new BeneficiaryException(BeneficiaryException.class,
                                String.format("No such beneficiary present for this account ben id: %s",
                                        fetchedAccount.getAccountNumber()), methodName));

        log.debug("<---------getAllBeneficiariesOfAnAccountByAccountNumber(Accounts fetchedAccount,Pageable pageable) BeneficiaryServiceImpl ended --------------------------------------" +
                "----------------------------------------------------------------------------------------------->");
        return getPageableResponse(allPagedBeneficiary, BeneficiaryDto.class);
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


        BiPredicate<String,String> isALLowedToUpdate=(newRecord,oldRecord)->StringUtils.isNotBlank(newRecord) && !newRecord.equalsIgnoreCase(oldRecord);
        BiPredicate<LocalDate,LocalDate> isAllowedToUpdateForDate=(newDate,oldDate)->!isNull(newDate) && !newDate.equals(oldDate);
        BiPredicate<Object,Object> isAllowedForToUpdateForObject=(newObject,oldObject)->!isNull(newObject) && !newObject.equals(oldObject);

        if (isALLowedToUpdate.test(newBeneficiaryName,oldBeneficiaryName))
            oldBeneficiaryData.setBeneficiaryName(newBeneficiaryName);

        if (isALLowedToUpdate.test(newBeneficiaryNumber,oldBeneficiaryNumber))
            oldBeneficiaryData.setBeneficiaryAccountNumber(newBeneficiaryNumber);

        if (isAllowedToUpdateForDate.test(newBeneficiaryDOB,oldBeneficiaryDOB)) {
            oldBeneficiaryData.setBenDate_Of_Birth(newBeneficiaryDOB);
            LocalDate now=LocalDate.now();
            final int age= Period.between(newBeneficiaryDOB,now).getYears();
            oldBeneficiaryData.setBenAge(age);
        }

        if (isALLowedToUpdate.test(newBeneficiaryAdharNumber,oldBeneficiaryAdharNumber))
            oldBeneficiaryData.setBenAdharNumber(newBeneficiaryAdharNumber);

        if (isAllowedForToUpdateForObject.test(newBeneficaryRelation,oldBeneficiaryRelation))
            oldBeneficiaryData.setRelation(newBeneficaryRelation);

        if (isALLowedToUpdate.test(newBeneficiaryPanNumber,oldBeneficiaryPanNumber))
            oldBeneficiaryData.setBenPanNumber(newBeneficiaryPanNumber);

        if (isALLowedToUpdate.test(newBeneficiaryPassport,oldBeneficiaryPassport))
            oldBeneficiaryData.setBenPassportNumber(newBeneficiaryPassport);

        if (isALLowedToUpdate.test(newBeneficiaryVoterId,oldBeneficiaryVoterId))
            oldBeneficiaryData.setBenVoterId(newBeneficiaryVoterId);

        if(isALLowedToUpdate.test(newBeneficiaryPhoneNumber,oldBeneficiaryPhoneNumber))
            oldBeneficiaryData.setBenPhoneNumber(newBeneficiaryPhoneNumber);

        if(isALLowedToUpdate.test(newBeneficiaryEmail,oldBeneficiaryEmail))
            oldBeneficiaryData.setBeneficiaryEmail(newBeneficiaryEmail);

        if(isAllowedForToUpdateForObject.test(newBenBank,oldBenBank)) {
            oldBeneficiaryData.setBenBank(newBenBank);
            oldBeneficiaryData.setBankCode(getBankCode(newBenBank));}

        if(isALLowedToUpdate.test(newBenDrivingLicense,oldDrivingLicense))
            oldBeneficiaryData.setBenDrivingLicense(newBenDrivingLicense);

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
        validationService.beneficiaryUpdateValidator(fetchedAccounts, beneficiaryDto, UPDATE_BEN);

        final String BENEFICIARY_ID = beneficiaryDto.beneficiaryId();
        if (isBlank(BENEFICIARY_ID)) throw new BeneficiaryException(BeneficiaryException.class,
                "Please enter a valid beneficiary id", methodName);

        final Beneficiary beneficiaryAccount = fetchedAccounts.getListOfBeneficiary().stream().
                filter(beneficiary -> BENEFICIARY_ID.equalsIgnoreCase(beneficiary.getBeneficiaryId())).
                findFirst().orElseThrow(()->new BeneficiaryException(BeneficiaryException.class,
                    String.format("No such beneficiary accounts exist with beneficiary id %s",
                            BENEFICIARY_ID), methodName));

        final Beneficiary newBeneficiaryData = mapToBeneficiary(beneficiaryDto);
        final Beneficiary processedAccount = processedBeneficiaryAccount(beneficiaryAccount, newBeneficiaryData);
        log.debug("<---------------updateBeneficiaryDetailsOfAnAccount(Accounts, BeneficiaryDto) BeneficiaryServiceImpl ended ----------------" +
                "------------------------------------------------------------------------------------------------------>");
        return beneficiaryRepository.save(processedAccount);
    }

    private void deleteBeneficiariesForAnAccount(final Accounts fetchedAccounts,final String beneficiaryId) throws AccountsException, BeneficiaryException {
        log.debug("<---------------deleteBeneficiariesForAnAccount(Accounts, Long) BeneficiaryServiceImpl started ----------------------" +
                "------------------------------------------------------------------------------------------------>");
        final String methodName = " deleteBeneficiariesForAnAccount(Long , Long )  in BeneficiaryServiceImpl";
        if (isBlank(beneficiaryId))
            throw new BeneficiaryException(BeneficiaryException.class, "Please provide a valid beneficiary id", methodName);

        BeneficiaryDto beneficiaryDto=new BeneficiaryDto.Builder().beneficiaryId(beneficiaryId).benAge(0).build();
        validationService.beneficiaryUpdateValidator(fetchedAccounts,beneficiaryDto,DELETE_BEN);

        //delete that beneficiary
        Predicate<Beneficiary> removeDeletedBeneficiary=(beneficiary) ->
                beneficiary.getBeneficiaryId().equalsIgnoreCase(beneficiaryId);
        Set<Beneficiary> beneficiaries=fetchedAccounts.getListOfBeneficiary()
                .stream().toList().stream()
                .filter(beneficiary -> removeDeletedBeneficiary.negate().test(beneficiary))
                .collect(Collectors.toSet());

        beneficiaryRepository.deleteAllByIdInBatch(Collections.singleton(beneficiaryId));
        fetchedAccounts.setListOfBeneficiary(beneficiaries);
        accountsRepository.save(fetchedAccounts);

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

        if (isEmpty(pageableResponseDto.content()))
            throw new BadApiRequestException(BadApiRequestException.class,
                    String.format("Account with id %s have no beneficiary present", fetchedAccount.getAccountNumber()),
                    methodName);

        log.debug("<-------------beneficiaryPagination(DIRECTION,String,int,int ,Accounts ) BeneficiaryServiceImpl ended --------------------------------------" +
                "----------------------------------------------------------------------------------------------------------->");
        return  pageableResponseDto;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public OutputDto postRequestBenExecutor(final PostInputRequestDto postInputDto) throws BeneficiaryException, AccountsException {
        final String methodName = "postRequestBenExecutor(InputDto) in BeneficiaryServiceImpl";
        final BeneficiaryDto beneficiaryDto = mapInputDtoToBenDto(postInputDto);

        final String accountNumber = postInputDto.getAccountNumber();
        final Accounts fetchedAccount = fetchAccountByAccountNumber(accountNumber);
        final AccountsDto accountsDto = mapToAccountsDto(fetchedAccount);

        final Customer customer = fetchedAccount.getCustomer();
        final CustomerDto customerDto = mapToCustomerDto(customer);

        final AllConstantHelpers.BenUpdateRequest requestType = postInputDto.getBenRequest();
        if (isNull(requestType)) throw new BeneficiaryException(BeneficiaryException.class,
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
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public OutputDto putRequestBenExecutor(final PutInputRequestDto putInputRequestDto) throws BeneficiaryException, AccountsException {
        final String methodName = "putRequestBenExecutor(InputDto) in BeneficiaryServiceImpl";
        final BeneficiaryDto beneficiaryDto = mapPutInputRequestDtoToBenDto(putInputRequestDto);

        final String accountNumber = putInputRequestDto.getAccountNumber();
        final Accounts fetchedAccount = fetchAccountByAccountNumber(accountNumber);

        final Customer loadCustomer = fetchedAccount.getCustomer();
        final AllConstantHelpers.BenUpdateRequest requestType = putInputRequestDto.getBenRequest();
        if (isNull(requestType)) throw new BeneficiaryException(BeneficiaryException.class,
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
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public OutputDto getRequestBenExecutor(final GetInputRequestDto getInputRequestDto) throws AccountsException, BeneficiaryException, BadApiRequestException {
        final String methodName = "getRequestBenExecutor(InputDto) in BeneficiaryServiceImpl";
        final BeneficiaryDto beneficiaryDto = mapGetRequestInputDtoToBenDto(getInputRequestDto);

        final int pageNumber = getInputRequestDto.getPageNumber();
        if (pageNumber < 0) throw new BadApiRequestException(BadApiRequestException.class,
                "pageNumber cant be in negative", methodName);

        if (getInputRequestDto.getPageSize() < 0)
            throw new BadApiRequestException(BadApiRequestException.class, "Page Size can't be in negative", methodName);
        final int pageSize = (getInputRequestDto.getPageSize() == 0) ? DEFAULT_PAGE_SIZE : getInputRequestDto.getPageSize();

        final String sortBy = (isBlank(getInputRequestDto.getSortBy())) ? "beneficiaryName" : getInputRequestDto.getSortBy();
        final DIRECTION sortDir = (isNull(getInputRequestDto.getSortDir())) ? DIRECTION.asc : getInputRequestDto.getSortDir();

        final String accountNumber = getInputRequestDto.getAccountNumber();
        final Accounts fetchedAccount = fetchAccountByAccountNumber(accountNumber);

        final AllConstantHelpers.BenUpdateRequest requestType = getInputRequestDto.getBenRequest();
        if (isNull(requestType)) throw new BeneficiaryException(BeneficiaryException.class,
                "Please provide a non null request-type", methodName);

        final String location;
        switch (requestType) {
            case GET_BEN -> {
                location="Inside GET_BEN";
                final Beneficiary beneficiary = getBeneficiaryById(fetchedAccount, beneficiaryDto.beneficiaryId())
                        .orElseThrow(()-> new BeneficiaryException(BeneficiaryException.class, String.format("No such beneficiaries present with id:%s",
                            beneficiaryDto.beneficiaryId()), String.format("%s of %s", location, methodName)));

                return OutputDto.builder()
                        .customer(mapToCustomerOutputDto(mapToCustomerDto(fetchedAccount.getCustomer())))
                        .accounts(mapToAccountsOutputDto(mapToAccountsDto(fetchedAccount)))
                        .beneficiary(mapToBeneficiaryDto(beneficiary))
                        .defaultMessage(String.format("Fetched beneficiary with id:%s",beneficiaryDto.beneficiaryId()))
                        .build();
            }
            case GET_ALL_BEN -> {
                location="Inside GET_ALL_BEN";
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
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public OutputDto deleteRequestBenExecutor(final DeleteInputRequestDto deleteInputRequestDto) throws BeneficiaryException, AccountsException {
        final String methodName = "deleteRequestBenExecutor(InputDto) in BeneficiaryServiceImpl";
        final BeneficiaryDto beneficiaryDto = mapDeleteInputRequestDtoToBenDto(deleteInputRequestDto);

        final String accountNUmber = deleteInputRequestDto.getAccountNumber();
        final Accounts fetchedAccount = fetchAccountByAccountNumber(accountNUmber);

        final AllConstantHelpers.BenUpdateRequest requestType = deleteInputRequestDto.getBenRequest();
        if (isNull(requestType)) throw new BeneficiaryException(BeneficiaryException.class,
                "Please provide a non null request-type", methodName);
        switch (requestType) {
            case DELETE_BEN -> {
                deleteBeneficiariesForAnAccount(fetchedAccount, beneficiaryDto.beneficiaryId());
                return OutputDto.builder()
                        .defaultMessage(String.format("Beneficiary with id:%s has been deleted",beneficiaryDto.beneficiaryId()))
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
