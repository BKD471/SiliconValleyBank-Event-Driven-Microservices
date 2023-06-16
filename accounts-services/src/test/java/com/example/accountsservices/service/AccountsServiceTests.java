package com.example.accountsservices.service;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.helpers.CodeRetrieverHelper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.CustomerRepository;
import com.example.accountsservices.service.impl.AccountsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountsServiceTests {
    @Autowired
    private AccountsServiceImpl accountsService;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private AccountsRepository accountsRepository;

    Customer customer;
    Accounts accounts;

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
                .address("address")
                .adharNumber("adhar")
                .drivingLicense("driving")
                .panNumber("pan")
                .passportNumber("passport")
                .imageName("img.png")
                .DateOfBirth(LocalDate.of(1997, 12, 01))
                .voterId("voter")
                .accounts(Collections.singletonList(accounts))
                .build();

        accounts.setCustomer(customer);

    }


    @Test
    public void testCreateAccount() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(accountsRepository.findByAccountNumber(anyLong())).thenReturn(Optional.of(accounts));
        when(customerRepository.save(any())).thenReturn(customer);

        String branchCode = CodeRetrieverHelper.getBranchCode(Accounts.Branch.KOLKATA);
        PostInputRequestDto postInputRequestDto = PostInputRequestDto.builder()
                .updateRequest(AccountsDto.UpdateRequest.CREATE_ACC)
                .name("phoenix")
                .email("phoenix@gmail.com")
                .password("pass")
                .phoneNumber("91-1234567890")
                .homeBranch(Accounts.Branch.KOLKATA)
                .dateOfBirthInYYYYMMDD(String.valueOf(LocalDate.of(1997, 12, 01)))
                .adharNumber("adhar")
                .panNumber("pan")
                .voterId("voter")
                .address("address")
                .drivingLicense("driving")
                .passportNumber("passport")
                .accountType(Accounts.AccountType.SAVINGS)
                .branchCode(branchCode)
                .transferLimitPerDay(25000L)
                .creditScore(750)
                .age(25)
                .build();
        OutputDto response = accountsService.accountSetUp(postInputRequestDto);

        assertEquals("phoenix@gmail.com", response.getCustomer().getEmail());
        assertEquals("passport", response.getCustomer().getPassportNumber());
        assertEquals("address", response.getCustomer().getAddress());
        assertEquals("adhar", response.getCustomer().getAdharNumber());
        assertEquals("voter", response.getCustomer().getVoterId());
        assertEquals("driving", response.getCustomer().getDrivingLicense());
        assertEquals("pan", response.getCustomer().getPanNumber());
        assertEquals(25, response.getCustomer().getAge());
        assertEquals(LocalDate.of(1997, 12, 01), response.getCustomer().getDateOfBirth());
        assertEquals(60000L, response.getAccounts().getBalance());
        assertEquals(25000L, response.getAccounts().getTransferLimitPerDay());
        assertEquals(750, response.getAccounts().getCreditScore());
    }

    @Test
    public void testAddAccount() throws IOException {
        String branchCode = CodeRetrieverHelper.getBranchCode(Accounts.Branch.CHENNAI);
        when(accountsRepository.findByAccountNumber(anyLong())).thenReturn(Optional.of(accounts));
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        Accounts processedAccount = Accounts.builder()
                .accountNumber(2L)
                .accountType(Accounts.AccountType.SAVINGS)
                .accountStatus(Accounts.AccountStatus.OPEN)
                .anyActiveLoans(false)
                .approvedLoanLimitBasedOnCreditScore(900000L)
                .balance(90000L)
                .branchCode(branchCode)
                .totalOutStandingAmountPayableToBank(500000L)
                .transferLimitPerDay(85000L)
                .totLoanIssuedSoFar(550000L)
                .creditScore(850)
                .homeBranch(Accounts.Branch.CHENNAI)
                .build();

        when(accountsRepository.save(any())).thenReturn(processedAccount);


        PutInputRequestDto putInputRequestDto = PutInputRequestDto.builder()
                .customerId(1L)
                .accountType(Accounts.AccountType.SAVINGS)
                .updateRequest(AccountsDto.UpdateRequest.ADD_ACCOUNT)
                .homeBranch(Accounts.Branch.CHENNAI)
                .build();
        OutputDto response = accountsService.putRequestExecutor(putInputRequestDto);
        assertEquals(850, response.getAccounts().getCreditScore());
        assertEquals(90000L, response.getAccounts().getBalance());
        assertEquals(Accounts.Branch.CHENNAI, response.getAccounts().getHomeBranch());
    }

    @Test
    public void updateHomeBranchTest() throws IOException {
        String newBranchCode = CodeRetrieverHelper.getBranchCode(Accounts.Branch.BANGALORE);

        when(accountsRepository.findByAccountNumber(anyLong())).thenReturn(Optional.of(accounts));
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        PutInputRequestDto putInputRequestDto = PutInputRequestDto.builder()
                .accountNumber(1L)
                .homeBranch(Accounts.Branch.BANGALORE)
                .updateRequest(AccountsDto.UpdateRequest.UPDATE_HOME_BRANCH).build();

        Accounts savedAccount = Accounts.builder()
                .accountNumber(1L)
                .homeBranch(Accounts.Branch.BANGALORE)
                .branchCode(newBranchCode)
                .accountType(Accounts.AccountType.CURRENT)
                .build();
        savedAccount.setCustomer(customer);
        when(accountsRepository.save(any())).thenReturn(savedAccount);
        OutputDto response = accountsService.putRequestExecutor(putInputRequestDto);

        assertEquals(Accounts.Branch.BANGALORE, response.getAccounts().getHomeBranch());
        assertEquals(newBranchCode, response.getAccounts().getBranchCode());
    }

    @Test
    public void updateHomeBranchFailedTest() throws IOException {
        String newBranchCode = CodeRetrieverHelper.getBranchCode(Accounts.Branch.KOLKATA);

        when(accountsRepository.findByAccountNumber(anyLong())).thenReturn(Optional.of(accounts));
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        PutInputRequestDto putInputRequestDto = PutInputRequestDto.builder()
                .accountNumber(1L)
                .homeBranch(Accounts.Branch.KOLKATA)
                .updateRequest(AccountsDto.UpdateRequest.UPDATE_HOME_BRANCH).build();

        assertThrows(AccountsException.class, () -> {
            accountsService.putRequestExecutor(putInputRequestDto);
        });
    }
}
