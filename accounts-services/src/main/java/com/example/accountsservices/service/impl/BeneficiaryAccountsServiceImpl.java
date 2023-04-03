package com.example.accountsservices.service.impl;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.mapper.AccountsMapper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.BeneficiaryRepository;
import com.example.accountsservices.service.AbstractAccountsService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class BeneficiaryAccountsServiceImpl extends AbstractAccountsService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final AccountsRepository accountsRepository;

    BeneficiaryAccountsServiceImpl(AccountsRepository accountsRepository,
                                   BeneficiaryRepository beneficiaryRepository) {
        this.accountsRepository = accountsRepository;
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
    public AccountsDto addBeneficiary(Long customerId, Long accountNumber, BeneficiaryDto beneficiaryDto) throws AccountsException {

        //Updating the beneficiary info & saving it
        Beneficiary beneficiaryAccount = AccountsMapper.mapToBeneficiary(beneficiaryDto);
        Beneficiary savedBeneficiaryAccount = beneficiaryRepository.save(beneficiaryAccount);
        Beneficiary processedBeneficiaryAccount = setBeneficiaryAgeFromDOB(savedBeneficiaryAccount);
        Beneficiary savedAndProcessedBeneficiaryAccount = beneficiaryRepository.save(processedBeneficiaryAccount);

        //Get the account in which we add beneficiary
        Optional<Accounts> fetchedAccounts = Optional.ofNullable(accountsRepository.findByCustomerIdAndAccountNumber(customerId, accountNumber));
        if (fetchedAccounts.isEmpty()) throw new AccountsException("No such accounts");

        //add the beneficiary to account
        List<Beneficiary> beneficiaries = new ArrayList<>();
        beneficiaries.add(savedAndProcessedBeneficiaryAccount);
        fetchedAccounts.get().setListOfBeneficiary(beneficiaries);
        return AccountsMapper.mapToAccountsDto(fetchedAccounts.get());
    }


    @Override
    public List<BeneficiaryDto> getAllBeneficiariesOfAnAccountByCustomerIdAndLoanNumber(Long customerId, Long accountNumber) throws BeneficiaryException {
        Optional<Accounts> fetchedAccount = Optional.ofNullable(accountsRepository.findByCustomerIdAndAccountNumber(customerId, accountNumber));
        if (fetchedAccount.isEmpty()) throw new BeneficiaryException("No beneficiaries present for this account");
        return fetchedAccount.get().getListOfBeneficiary().stream().map(AccountsMapper::mapToBeneficiaryDto).collect(Collectors.toList());
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
     * @param customerId
     * @param beneficiaryDto
     * @return
     */
    @Override
    public BeneficiaryDto updateBeneficiaryDetailsOfaCustomerByBeneficiaryId(Long customerId, Long accountNumber, BeneficiaryDto beneficiaryDto) throws AccountsException, BeneficiaryException {
        //fetch the account
        Optional<Accounts> fetchedAccounts = Optional.ofNullable(accountsRepository.findByCustomerIdAndAccountNumber(customerId, accountNumber));
        if (fetchedAccounts.isEmpty())
            throw new AccountsException(String.format("No such accounts present with customer id %s or accountNumber %s", customerId, accountNumber));

        //fetch the beneficiary from beneficiaryList
        Long beneficiaryId = beneficiaryDto.getBeneficiaryId();
        Optional<Beneficiary> beneficiaryAccount = fetchedAccounts.get().getListOfBeneficiary().stream().filter(beneficiary -> beneficiary.getBeneficiaryId().equals(beneficiaryId)).findFirst();
        if (beneficiaryAccount.isEmpty())
            throw new BeneficiaryException(String.format("No such beneficiary accounts exist with beneficiary id %s", beneficiaryId));

        //update
        Beneficiary newBeneficiaryData = AccountsMapper.mapToBeneficiary(beneficiaryDto);
        Beneficiary processedAccount = processedBeneficiaryAccount(beneficiaryAccount.get(), newBeneficiaryData);
        Beneficiary savedProcessedAccount = beneficiaryRepository.save(processedAccount);
        return AccountsMapper.mapToBeneficiaryDto(savedProcessedAccount);
    }

    public void deleteBeneficiaries(Long beneficiaryId) {

    }
}
