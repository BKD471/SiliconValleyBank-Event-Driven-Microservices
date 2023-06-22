package com.example.accountsservices.service;


import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.helpers.CodeRetrieverHelper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.model.Transactions;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.TransactionsRepository;
import com.example.accountsservices.service.impl.TransactionsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionsServiceTests {
    @Autowired
    @Qualifier("transactionsServicePrimary")
    ITransactionsService transactionsService;

    @MockBean
    AccountsRepository accountsRepository;

    @MockBean
    TransactionsRepository transactionsRepository;

    Accounts accounts;
    Customer customer;

    @BeforeEach
    public void init() {
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
        accounts.setCustomer(customer);
        customer.setAccounts(Collections.singletonList(accounts));
    }

    @Test
    public void pastSixMonthsTransactionsForAnAccountTest() {
        Transactions transactions1=Transactions.builder()
                .transactionId(1L)
                .transactedAccountNumber("1L")
                .transactionType(Transactions.TransactionType.DEBIT)
                .description(Transactions.DescriptionType.ELECTRICITY)
                .accounts(accounts)
                .transactionAmount(60000L)
                .build();
        transactions1.setTransactionTimeStamp(LocalDateTime.now());
        Transactions transactions2=Transactions.builder()
                .transactionId(2L)
                .transactedAccountNumber("2L")
                .transactionType(Transactions.TransactionType.CREDIT)
                .description(Transactions.DescriptionType.SALARY)
                .accounts(accounts)
                .transactionAmount(160000L)
                .build();
        transactions2.setTransactionTimeStamp(LocalDateTime.of(2023,06,17,05,40));


        List<Transactions> transactionsList = new ArrayList<>(Arrays.asList(transactions2, transactions1));
        accounts.setListOfTransactions(transactionsList);
        when(accountsRepository.findByAccountNumber(anyLong())).thenReturn(Optional.of(accounts));

        OutputDto response=transactionsService.getPastSixMonthsTransactionsForAnAccount(1L);
        assertNotNull(response.getAccounts().getListOfTransactions());
        assertEquals(2,response.getAccounts().getListOfTransactions().size());
    }
}
