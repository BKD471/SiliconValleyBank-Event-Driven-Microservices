package com.example.accountsservices.service;

import com.example.accountsservices.helpers.CodeRetrieverHelper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Customer;
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

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountsServiceTests {
    @Autowired
    private AccountsServiceImpl accountsService;

    @MockBean
    private CustomerRepository customerRepository;

    Customer customer;
    Accounts accounts;
    @BeforeEach
    public void setUp(){
        String branchCode= CodeRetrieverHelper.getBranchCode(Accounts.Branch.KOLKATA);
        accounts=Accounts.builder()
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

        customer=Customer.builder()
                .customerId(1L)
                .age(25)
                .email("email@gmail.com")
                .address("address")
                .adharNumber("adhar")
                .drivingLicense("driving")
                .panNumber("pan")
                .passportNumber("passport")
                .imageName("img.png")
                .DateOfBirth(LocalDate.of(1997,12,01))
                .voterId("voter")
                .accounts(Collections.singletonList(accounts))
                .build();

        accounts.setCustomer(customer);

    }


    @Test
    public void testCreateAccount(){
       when(customerRepository.save(any())) .thenReturn(customer);
       ///likhenge baad m :)
    }
}
