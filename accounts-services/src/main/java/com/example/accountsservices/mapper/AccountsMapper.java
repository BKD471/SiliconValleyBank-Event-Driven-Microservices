package com.example.accountsservices.mapper;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Transactions;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountsMapper {

    /**
     * @param accountsDto
     * @paramType AccountsDto
     * @ReturnType Accounts
     */
    public static Accounts mapToAccounts(AccountsDto accountsDto) {
        Accounts account = new Accounts();
        account.setAccountNumber(accountsDto.getAccountNumber());
        account.setCustomerId(accountsDto.getCustomerId());
        account.setDateOfBirth(accountsDto.getDateOfBirth());
        account.setCustomerAge(accountsDto.getCustomerAge());
        account.setBalance(accountsDto.getBalance());
        account.setAccountType(accountsDto.getAccountType());
        account.setBranchAddress(accountsDto.getBranchAddress());
        account.setPhoneNumber(accountsDto.getPhoneNumber());
        account.setAdharNumber(accountsDto.getAdharNumber());
        account.setPanNumber(accountsDto.getPanNumber());
        account.setVoterId(accountsDto.getVoterId());
        account.setDrivingLicense(accountsDto.getDrivingLicense());
        account.setPassportNumber(accountsDto.getPassportNumber());
        account.setListOfBeneficiary(accountsDto.getListOfBeneficiary());
        account.setListOfTransactions(accountsDto.getListOfTransactions());
        return account;
    }

    /**
     * @param accounts
     * @paramType Accounts
     * @ReturnType AccountsDto
     */
    public static AccountsDto mapToAccountsDto(Accounts accounts) {
        AccountsDto accountsDto = new AccountsDto();
        accountsDto.setAccountNumber(accounts.getAccountNumber());
        accountsDto.setCustomerId(accounts.getCustomerId());
        accountsDto.setDateOfBirth(accounts.getDateOfBirth());
        accountsDto.setCustomerAge(accounts.getCustomerAge());
        accountsDto.setBalance(accounts.getBalance());
        accountsDto.setAccountType(accounts.getAccountType());
        accountsDto.setBranchAddress(accounts.getBranchAddress());
        accountsDto.setPhoneNumber(accounts.getPhoneNumber());
        accountsDto.setAdharNumber(accounts.getAdharNumber());
        accountsDto.setPanNumber(accounts.getPanNumber());
        accountsDto.setVoterId(accounts.getVoterId());
        accountsDto.setDrivingLicense(accounts.getDrivingLicense());
        accountsDto.setPassportNumber(accounts.getPassportNumber());
        accountsDto.setListOfBeneficiary(accounts.getListOfBeneficiary());
        accountsDto.setListOfTransactions(accounts.getListOfTransactions());
        return accountsDto;
    }

    public static BeneficiaryDto mapToBeneficiaryDto(Beneficiary beneficiary){
        BeneficiaryDto beneficiaryDto=new BeneficiaryDto();
        beneficiaryDto.setBeneficiaryId(beneficiary.getBeneficiaryId());
        beneficiaryDto.setBeneficiaryName(beneficiary.getBeneficiaryName());
        beneficiaryDto.setAge(beneficiary.getAge());
        beneficiaryDto.setBeneficiaryAccountNumber(beneficiary.getBeneficiaryAccountNumber());
        beneficiaryDto.setAdharNumber(beneficiary.getAdharNumber());
        beneficiaryDto.setRelation(beneficiary.getRelation());
        beneficiaryDto.setVoterId(beneficiary.getVoterId());
        beneficiaryDto.setPanNumber(beneficiary.getPanNumber());
        beneficiaryDto.setPassPort(beneficiary.getPassPort());
        beneficiaryDto.setDate_Of_Birth(beneficiary.getDate_Of_Birth());
        beneficiaryDto.setAccounts(beneficiary.getAccounts());
        return beneficiaryDto;
    }

    public static Beneficiary mapToBeneficiary(BeneficiaryDto beneficiaryDto){
        Beneficiary beneficiary=new Beneficiary();
        beneficiary.setBeneficiaryName(beneficiaryDto.getBeneficiaryName());
        beneficiary.setBeneficiaryAccountNumber(beneficiaryDto.getBeneficiaryAccountNumber());
        beneficiary.setAdharNumber(beneficiaryDto.getAdharNumber());
        beneficiary.setRelation(beneficiaryDto.getRelation());
        beneficiary.setVoterId(beneficiaryDto.getVoterId());
        beneficiary.setPanNumber(beneficiaryDto.getPanNumber());
        beneficiary.setPassPort(beneficiaryDto.getPassPort());
        beneficiary.setDate_Of_Birth(beneficiaryDto.getDate_Of_Birth());
        beneficiary.setAccounts(beneficiaryDto.getAccounts());
        return beneficiary;
    }
}
