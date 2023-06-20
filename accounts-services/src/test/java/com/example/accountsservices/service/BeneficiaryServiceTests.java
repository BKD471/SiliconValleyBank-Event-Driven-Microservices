package com.example.accountsservices.service;

import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.helpers.CodeRetrieverHelper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.BeneficiaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BeneficiaryServiceTests {

   @Autowired
   @Qualifier("beneficiaryServicePrimary")
   private IBeneficiaryService beneficiaryService;

    @MockBean
    BeneficiaryRepository beneficiaryRepository;
    @MockBean
    AccountsRepository accountsRepository;

    Beneficiary beneficiary;
    Accounts accounts;
    Customer customer;

    @BeforeEach
    public void setUp() {
        String branchCode = CodeRetrieverHelper.getBranchCode(Accounts.Branch.KOLKATA);


        accounts = Accounts.builder()
                .accountNumber(1L)
                .accountType(Accounts.AccountType.SAVINGS)
                .accountStatus(Accounts.AccountStatus.OPEN)
                .anyActiveLoans(false)
                .approvedLoanLimitBasedOnCreditScore(500000L)
                .balance(60000L)
                .branchCode(branchCode)
                .totalOutStandingAmountPayableToBank(500000L)
                .transferLimitPerDay(25000L)
                .totLoanIssuedSoFar(450000L)
                .creditScore(750)
                .homeBranch(Accounts.Branch.KOLKATA)
                .build();
        customer = Customer.builder()
                .customerId(1L)
                .age(25)
                .name("phoenix")
                .email("phoenix@gmail.com")
                .phoneNumber("+91-9876543217")
                .address("address")
                .adharNumber("adhar")
                .drivingLicense("driving")
                .panNumber("pan")
                .passportNumber("passport")
                .imageName("img.png")
                .DateOfBirth(LocalDate.of(1997, 12, 01))
                .voterId("voter")
                .build();



        beneficiary = Beneficiary.builder()
                .beneficiaryId(1L)
                .beneficiaryAccountNumber(1L)
                .beneficiaryName("ben 1")
                .beneficiaryEmail("ben1@gmail.com")
                .address("ben 123 street")
                .benAdharNumber("1234-5678-9999")
                .benDrivingLicense("driving-no-1")
                .benPassportNumber("passport-no-1")
                .benPhoneNumber("+91-123456789")
                .benPanNumber("GMDPD7592K")
                .benBank(Beneficiary.BanksSupported.AXIS)
                .bankCode(CodeRetrieverHelper.getBankCode(Beneficiary.BanksSupported.AXIS))
                .imageName("img1.png")
                .BenDate_Of_Birth(LocalDate.of(1997, 12, 01))
                .benVoterId("ben voter 1")
                .benAge(25)
                .relation(Beneficiary.RELATION.SON)
                .accounts(accounts)
                .build();

        accounts.setCustomer(customer);
        accounts.setListOfBeneficiary(Arrays.asList(beneficiary));
    }


    @Test
    public void addBeneficiaryTest(){
        String bankCOde=CodeRetrieverHelper.getBankCode(Beneficiary.BanksSupported.AXIS);
        LocalDate dob=LocalDate.of(1997,12,01);
        int age= Period.between(dob,LocalDate.now()).getYears();


        when(accountsRepository.findByAccountNumber(anyLong()))
                .thenReturn(Optional.of(accounts));
        when(accountsRepository.save(any())).thenReturn(accounts);

        PostInputRequestDto request= PostInputRequestDto.builder()
                .accountNumber(1L)
                .benRequest(BeneficiaryDto.BenUpdateRequest.ADD_BEN)
                .name("ben 2")
                .email("ben2@gmail.com")
                .beneficiaryAccountNumber(3L)
                .bloodRelation(Beneficiary.RELATION.SON)
                .dateOfBirthInYYYYMMDD(String.valueOf(dob))
                .benBank(Beneficiary.BanksSupported.AXIS)
                .adharNumber("1234-5678-8888")
                .address("ben 456 street")
                .phoneNumber("+91-987654321")
                .panNumber("GMDPD9876K")
                .passportNumber("passport-no-2")
                .voterId("ben voter 2")
                .drivingLicense("driving-no-2")
                .build();

        OutputDto response=beneficiaryService.postRequestBenExecutor(request);
        assertEquals(bankCOde,response.getBeneficiary().getBankCode());
        assertEquals(age,response.getBeneficiary().getBenAge());
        assertEquals(request.getEmail(),response.getBeneficiary().getBeneficiaryEmail());
    }
}
