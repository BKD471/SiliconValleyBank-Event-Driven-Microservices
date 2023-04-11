package com.example.accountsservices.service.impl;


import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.mapper.Mapper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.BeneficiaryRepository;
import com.example.accountsservices.service.AbstractAccountsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BeneficiaryServiceImpl extends AbstractAccountsService {
    private final BeneficiaryRepository beneficiaryRepository;

    BeneficiaryServiceImpl(AccountsRepository accountsRepository,
                           BeneficiaryRepository beneficiaryRepository) {
        super(accountsRepository);
        this.beneficiaryRepository = beneficiaryRepository;
    }

    private Beneficiary setBeneficiaryAgeFromDOB(Beneficiary beneficiary) {
        //initialize the age of beneficiaries
        int dobYear = beneficiary.getDate_Of_Birth().getYear();
        int now = LocalDate.now().getYear();
        beneficiary.setAge(now - dobYear);
        return beneficiary;
    }

    @Override
    public BeneficiaryDto addBeneficiary( Long accountNumber, BeneficiaryDto beneficiaryDto) throws AccountsException {

        //Updating the beneficiary info & saving it
        Beneficiary beneficiaryAccount = Mapper.mapToBeneficiary(beneficiaryDto);
        Beneficiary savedBeneficiaryAccount = beneficiaryRepository.save(beneficiaryAccount);
        Beneficiary processedBeneficiaryAccount = setBeneficiaryAgeFromDOB(savedBeneficiaryAccount);
        Beneficiary savedAndProcessedBeneficiaryAccount = beneficiaryRepository.save(processedBeneficiaryAccount);

        //Get the account in which we add beneficiary
        Accounts fetchedAccount = fetchAccountByAccountNumber(accountNumber);

        //add the beneficiary to account
        List<Beneficiary> beneficiaries = new ArrayList<>();
        beneficiaries.add(savedAndProcessedBeneficiaryAccount);
        fetchedAccount.setListOfBeneficiary(beneficiaries);
        return Mapper.mapToBeneficiaryDto(savedAndProcessedBeneficiaryAccount);
    }


    @Override
    public List<BeneficiaryDto> getAllBeneficiariesOfAnAccountByAccountNumber(Long accountNumber) throws AccountsException {
        //get the account
        Accounts fetchedAccount = fetchAccountByAccountNumber(accountNumber);
        //get all beneficiaries
        return fetchedAccount.getListOfBeneficiary().stream().map(Mapper::mapToBeneficiaryDto).collect(Collectors.toList());
    }

    private Beneficiary processedBeneficiaryAccount(Beneficiary oldBeneficiaryData, Beneficiary newBeneficiaryData) {
        String newBeneficiaryName = newBeneficiaryData.getBeneficiaryName();
        Long newBeneficiaryNumber = newBeneficiaryData.getBeneficiaryAccountNumber();
        LocalDate newBeneficiaryDOB = newBeneficiaryData.getDate_Of_Birth();
        String newBeneficiaryAdharNumber = newBeneficiaryData.getAdharNumber();
        Beneficiary.RELATION newBeneficaryRelation = newBeneficiaryData.getRelation();
        String newBeneficiaryPanNumber = newBeneficiaryData.getPanNumber();
        String newBeneficiaryPassport = newBeneficiaryData.getPassPort();
        String newBeneficiaryVoterId = newBeneficiaryData.getVoterId();

        String oldBeneficiaryName = oldBeneficiaryData.getBeneficiaryName();
        Long oldBeneficiaryNumber = oldBeneficiaryData.getBeneficiaryAccountNumber();
        LocalDate oldBeneficiaryDOB = oldBeneficiaryData.getDate_Of_Birth();
        String oldBeneficiaryAdharNumber = oldBeneficiaryData.getAdharNumber();
        Beneficiary.RELATION oldBeneficiaryRelation = oldBeneficiaryData.getRelation();
        String oldBeneficiaryPanNumber = oldBeneficiaryData.getPanNumber();
        String oldBeneficiaryPassport = oldBeneficiaryData.getPassPort();
        String oldBeneficiaryVoterId = oldBeneficiaryData.getVoterId();

        if (null != newBeneficiaryName && !newBeneficiaryName.equalsIgnoreCase(oldBeneficiaryName))
            newBeneficiaryData.setBeneficiaryName(newBeneficiaryName);

        if (null != newBeneficiaryNumber && !newBeneficiaryNumber.equals(oldBeneficiaryNumber))
            newBeneficiaryData.setBeneficiaryAccountNumber(newBeneficiaryNumber);

        if (null != newBeneficiaryDOB && !newBeneficiaryDOB.equals(oldBeneficiaryDOB))
            newBeneficiaryData.setDate_Of_Birth(newBeneficiaryDOB);

        if (null != newBeneficiaryAdharNumber && !newBeneficiaryAdharNumber.equalsIgnoreCase(oldBeneficiaryAdharNumber))
            newBeneficiaryData.setBeneficiaryName(newBeneficiaryName);

        if (null != newBeneficaryRelation && !newBeneficaryRelation.equals(oldBeneficiaryRelation))
            newBeneficiaryData.setRelation(newBeneficaryRelation);

        if (null != newBeneficiaryPanNumber && !newBeneficiaryPanNumber.equalsIgnoreCase(oldBeneficiaryPanNumber))
            newBeneficiaryData.setPanNumber(newBeneficiaryPanNumber);

        if (null != newBeneficiaryPassport && !newBeneficiaryPassport.equalsIgnoreCase(oldBeneficiaryPassport))
            newBeneficiaryData.setPassPort(newBeneficiaryPassport);

        if (null != newBeneficiaryVoterId && !newBeneficiaryVoterId.equalsIgnoreCase(oldBeneficiaryVoterId))
            newBeneficiaryData.setVoterId(newBeneficiaryVoterId);

        return newBeneficiaryData;
    }

    /**
     * @param accountNumber
     * @param beneficiaryDto
     * @return
     */
    @Override
    public BeneficiaryDto updateBeneficiaryDetailsOfanAccount(Long accountNumber, BeneficiaryDto beneficiaryDto) throws AccountsException, BeneficiaryException {
        String methodName="updateBeneficiaryDetailsOfanAccount(Long , BeneficiaryDto ) in BeneficiaryServiceImpl";
        //fetch the account
        Accounts fetchedAccounts = fetchAccountByAccountNumber(accountNumber);

        //fetch the beneficiary from beneficiaryList
        Long BENEFICIARY_ID = beneficiaryDto.getBeneficiaryId();
        if(null==BENEFICIARY_ID) throw  new BeneficiaryException("Please enter a valid beneficiary id",methodName);
        Optional<Beneficiary> beneficiaryAccount = fetchedAccounts.getListOfBeneficiary().stream().filter(beneficiary -> BENEFICIARY_ID.equals(beneficiary.getBeneficiaryId())).findFirst();
        if (beneficiaryAccount.isEmpty())
            throw new BeneficiaryException(String.format("No such beneficiary accounts exist with beneficiary id %s", BENEFICIARY_ID),methodName);

        //update
        Beneficiary newBeneficiaryData = Mapper.mapToBeneficiary(beneficiaryDto);
        Beneficiary processedAccount = processedBeneficiaryAccount(beneficiaryAccount.get(), newBeneficiaryData);
        Beneficiary savedProcessedAccount = beneficiaryRepository.save(processedAccount);
        return Mapper.mapToBeneficiaryDto(savedProcessedAccount);
    }

    @Override
    public void deleteBeneficiariesForAnAccount(Long accountNumber, Long beneficiaryId) throws AccountsException, BeneficiaryException {

        String methodName=" deleteBeneficiariesForAnAccount(Long , Long )  in BeneficiaryServiceImpl";
        if (null == beneficiaryId) throw new BeneficiaryException("Please provide a valid beneficiary id",methodName);
        //fetch the account
        Accounts fetchedAccounts = fetchAccountByAccountNumber(accountNumber);

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

    @Override
    public void deleteAllBeneficiaries(Long accountNumber) throws AccountsException {
        //fetch the account
        Accounts fetchedAccounts = fetchAccountByAccountNumber(accountNumber);
        //delete everyone
        fetchedAccounts.getListOfBeneficiary().forEach(beneficiary ->
                beneficiaryRepository.deleteByBeneficiaryId(beneficiary.getBeneficiaryId()));
        //set an empty list
        fetchedAccounts.setListOfBeneficiary(new ArrayList<>());
    }
}
