package com.example.accountsservices.service;


import com.example.accountsservices.dto.baseDtos.TransactionsDto;
import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.exception.TransactionException;
import com.example.accountsservices.helpers.AllEnumConstantHelpers;
import com.example.accountsservices.helpers.CodeRetrieverHelper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.model.Transactions;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.TransactionsRepository;
import org.junit.jupiter.api.BeforeAll;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    AccountsRepository accountsRepositoryMock;

    @MockBean
    TransactionsRepository transactionsRepositoryMock;

    private static Accounts accounts;
    private static Customer customer;

    @BeforeAll
    public static void init() {
        String branchCode = CodeRetrieverHelper.getBranchCode(AllEnumConstantHelpers.Branch.KOLKATA);

        accounts = Accounts.builder()
                .accountNumber(1L)
                .accountType(AllEnumConstantHelpers.AccountType.SAVINGS)
                .accountStatus(AllEnumConstantHelpers.AccountStatus.OPEN)
                .anyActiveLoans(false)
                .approvedLoanLimitBasedOnCreditScore(500000L)
                .balance(500000L)
                .branchCode(branchCode)
                .totalOutStandingAmountPayableToBank(500000L)
                .transferLimitPerDay(25000L)
                .totLoanIssuedSoFar(450000L)
                .creditScore(750)
                .homeBranch(AllEnumConstantHelpers.Branch.KOLKATA)
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
        when(accountsRepositoryMock.findByAccountNumber(anyLong()))
                .thenReturn(Optional.of(accounts));

        OutputDto response=transactionsService.getPastSixMonthsTransactionsForAnAccount(1L);
        assertNotNull(response.getAccounts().getListOfTransactions());
        assertEquals(2,response.getAccounts().getListOfTransactions().size());
    }

    @Test
    public void payOrDepositMoneyTestForCredit(){
        String branchCode=CodeRetrieverHelper.getBranchCode(AllEnumConstantHelpers.Branch.KOLKATA);
        when(accountsRepositoryMock.findByAccountNumber(anyLong()))
                .thenReturn(Optional.of(accounts));

        TransactionsDto transactionsDto=TransactionsDto.builder()
                .transactionId(1L)
                .accountNumber(1L)
                .transactionAmount(100000L)
                .transactionType(Transactions.TransactionType.CREDIT)
                .transactedAccountNumber("123")
                .description(Transactions.DescriptionType.SALARY)
                .transactionTimeStamp(LocalDateTime.now())
                .build();

        long finalBalance=accounts.getBalance()+transactionsDto.getTransactionAmount();
        Accounts accountStateAfterTransaction=Accounts.builder()
                .accountNumber(1L)
                .accountType(AllEnumConstantHelpers.AccountType.SAVINGS)
                .accountStatus(AllEnumConstantHelpers.AccountStatus.OPEN)
                .anyActiveLoans(false)
                .approvedLoanLimitBasedOnCreditScore(500000L)
                .branchCode(branchCode)
                .totalOutStandingAmountPayableToBank(500000L)
                .transferLimitPerDay(25000L)
                .totLoanIssuedSoFar(450000L)
                .creditScore(750)
                .homeBranch(AllEnumConstantHelpers.Branch.KOLKATA)
                .balance(finalBalance)
                .build();

        Transactions transactionsState= Transactions.builder()
                .transactionId(1L)
                .transactionAmount(100000L)
                .transactionType(Transactions.TransactionType.CREDIT)
                .transactedAccountNumber("123")
                .description(Transactions.DescriptionType.SALARY)
                .accounts(accountStateAfterTransaction)
                .build();
        transactionsState.setTransactionTimeStamp(LocalDateTime.now());
        accountStateAfterTransaction.setListOfTransactions(Collections.singletonList(transactionsState));

        when(accountsRepositoryMock.save(any()))
                .thenReturn(accountStateAfterTransaction);
        when(transactionsRepositoryMock.save(any()))
                .thenReturn(transactionsState);

        OutputDto response=transactionsService.transactionsExecutor(transactionsDto);
        assertNotNull(response.getTransactions());
        assertEquals(finalBalance,response.getAccounts().getBalance());
    }

    @Test
    public void payOrDepositMoneyTestForDebit(){
        String branchCode=CodeRetrieverHelper.getBranchCode(AllEnumConstantHelpers.Branch.KOLKATA);
        when(accountsRepositoryMock.findByAccountNumber(anyLong()))
                .thenReturn(Optional.of(accounts));

        TransactionsDto transactionsDto=TransactionsDto.builder()
                .transactionId(1L)
                .accountNumber(1L)
                .transactionAmount(100000L)
                .transactionType(Transactions.TransactionType.DEBIT)
                .transactedAccountNumber("123")
                .description(Transactions.DescriptionType.ELECTRICITY)
                .transactionTimeStamp(LocalDateTime.now())
                .build();

        long finalBalance=accounts.getBalance()-transactionsDto.getTransactionAmount();
        Accounts accountStateAfterTransaction=Accounts.builder()
                .accountNumber(1L)
                .accountType(AllEnumConstantHelpers.AccountType.SAVINGS)
                .accountStatus(AllEnumConstantHelpers.AccountStatus.OPEN)
                .anyActiveLoans(false)
                .approvedLoanLimitBasedOnCreditScore(500000L)
                .branchCode(branchCode)
                .totalOutStandingAmountPayableToBank(500000L)
                .transferLimitPerDay(25000L)
                .totLoanIssuedSoFar(450000L)
                .creditScore(750)
                .homeBranch(AllEnumConstantHelpers.Branch.KOLKATA)
                .balance(finalBalance)
                .build();

        Transactions transactionsState= Transactions.builder()
                .transactionId(1L)
                .transactionAmount(100000L)
                .transactionType(Transactions.TransactionType.DEBIT)
                .transactedAccountNumber("123")
                .description(Transactions.DescriptionType.ELECTRICITY)
                .accounts(accountStateAfterTransaction)
                .build();
        transactionsState.setTransactionTimeStamp(LocalDateTime.now());
        accountStateAfterTransaction.setListOfTransactions(Collections.singletonList(transactionsState));

        when(accountsRepositoryMock.save(any()))
                .thenReturn(accountStateAfterTransaction);
        when(transactionsRepositoryMock.save(any()))
                .thenReturn(transactionsState);

        OutputDto response=transactionsService.transactionsExecutor(transactionsDto);
        assertNotNull(response.getTransactions());
        assertEquals(finalBalance,response.getAccounts().getBalance());
    }

    @Test
    public void payOrDepositMoneyTestFailedForDebitWhenAmountExceedsBalance(){
        when(accountsRepositoryMock.findByAccountNumber(anyLong()))
                .thenReturn(Optional.of(accounts));

        TransactionsDto transactionsDto=TransactionsDto.builder()
                .transactionId(1L)
                .accountNumber(1L)
                .transactionAmount(800000L)
                .transactionType(Transactions.TransactionType.DEBIT)
                .transactedAccountNumber("123")
                .description(Transactions.DescriptionType.ELECTRICITY)
                .transactionTimeStamp(LocalDateTime.now())
                .build();

        assertThrows(TransactionException.class,()->{
            transactionsService.transactionsExecutor(transactionsDto);
        });
    }

    @Test
    public void payOrDepositMoneyTestForDebitFORRent(){
        String branchCode=CodeRetrieverHelper.getBranchCode(AllEnumConstantHelpers.Branch.KOLKATA);
        when(accountsRepositoryMock.findByAccountNumber(anyLong()))
                .thenReturn(Optional.of(accounts));

        TransactionsDto transactionsDto=TransactionsDto.builder()
                .transactionId(1L)
                .accountNumber(1L)
                .transactionAmount(100000L)
                .transactionType(Transactions.TransactionType.DEBIT)
                .transactedAccountNumber("123")
                .description(Transactions.DescriptionType.RENT)
                .transactionTimeStamp(LocalDateTime.now())
                .build();

        long finalBalance=accounts.getBalance()-transactionsDto.getTransactionAmount();
        Accounts accountStateAfterTransaction=Accounts.builder()
                .accountNumber(1L)
                .accountType(AllEnumConstantHelpers.AccountType.SAVINGS)
                .accountStatus(AllEnumConstantHelpers.AccountStatus.OPEN)
                .anyActiveLoans(false)
                .approvedLoanLimitBasedOnCreditScore(500000L)
                .branchCode(branchCode)
                .totalOutStandingAmountPayableToBank(500000L)
                .transferLimitPerDay(25000L)
                .totLoanIssuedSoFar(450000L)
                .creditScore(750)
                .homeBranch(AllEnumConstantHelpers.Branch.KOLKATA)
                .balance(finalBalance)
                .build();

        Transactions transactionsState= Transactions.builder()
                .transactionId(1L)
                .transactionAmount(100000L)
                .transactionType(Transactions.TransactionType.DEBIT)
                .transactedAccountNumber("123")
                .description(Transactions.DescriptionType.RENT)
                .accounts(accountStateAfterTransaction)
                .build();
        transactionsState.setTransactionTimeStamp(LocalDateTime.now());
        accountStateAfterTransaction.setListOfTransactions(Collections.singletonList(transactionsState));

        when(accountsRepositoryMock.save(any()))
                .thenReturn(accountStateAfterTransaction);
        when(transactionsRepositoryMock.save(any()))
                .thenReturn(transactionsState);

        OutputDto response=transactionsService.transactionsExecutor(transactionsDto);
        assertNotNull(response.getTransactions());
        assertEquals(finalBalance,response.getAccounts().getBalance());
    }

    @Test
    public void payOrDepositMoneyTestForDebitForFamilyEXPENSE(){
        String branchCode=CodeRetrieverHelper.getBranchCode(AllEnumConstantHelpers.Branch.KOLKATA);
        when(accountsRepositoryMock.findByAccountNumber(anyLong()))
                .thenReturn(Optional.of(accounts));

        TransactionsDto transactionsDto=TransactionsDto.builder()
                .transactionId(1L)
                .accountNumber(1L)
                .transactionAmount(100000L)
                .transactionType(Transactions.TransactionType.DEBIT)
                .transactedAccountNumber("123")
                .description(Transactions.DescriptionType.FAMILY)
                .transactionTimeStamp(LocalDateTime.now())
                .build();

        long finalBalance=accounts.getBalance()-transactionsDto.getTransactionAmount();
        Accounts accountStateAfterTransaction=Accounts.builder()
                .accountNumber(1L)
                .accountType(AllEnumConstantHelpers.AccountType.SAVINGS)
                .accountStatus(AllEnumConstantHelpers.AccountStatus.OPEN)
                .anyActiveLoans(false)
                .approvedLoanLimitBasedOnCreditScore(500000L)
                .branchCode(branchCode)
                .totalOutStandingAmountPayableToBank(500000L)
                .transferLimitPerDay(25000L)
                .totLoanIssuedSoFar(450000L)
                .creditScore(750)
                .homeBranch(AllEnumConstantHelpers.Branch.KOLKATA)
                .balance(finalBalance)
                .build();

        Transactions transactionsState= Transactions.builder()
                .transactionId(1L)
                .transactionAmount(100000L)
                .transactionType(Transactions.TransactionType.DEBIT)
                .transactedAccountNumber("123")
                .description(Transactions.DescriptionType.FAMILY)
                .accounts(accountStateAfterTransaction)
                .build();
        transactionsState.setTransactionTimeStamp(LocalDateTime.now());
        accountStateAfterTransaction.setListOfTransactions(Collections.singletonList(transactionsState));

        when(accountsRepositoryMock.save(any()))
                .thenReturn(accountStateAfterTransaction);
        when(transactionsRepositoryMock.save(any()))
                .thenReturn(transactionsState);

        OutputDto response=transactionsService.transactionsExecutor(transactionsDto);
        assertNotNull(response.getTransactions());
        assertEquals(finalBalance,response.getAccounts().getBalance());
    }

    @Test
    public void payOrDepositMoneyTestForDebitForInvestMent(){
        String branchCode=CodeRetrieverHelper.getBranchCode(AllEnumConstantHelpers.Branch.KOLKATA);
        when(accountsRepositoryMock.findByAccountNumber(anyLong()))
                .thenReturn(Optional.of(accounts));

        TransactionsDto transactionsDto=TransactionsDto.builder()
                .transactionId(1L)
                .accountNumber(1L)
                .transactionAmount(100000L)
                .transactionType(Transactions.TransactionType.DEBIT)
                .transactedAccountNumber("123")
                .description(Transactions.DescriptionType.INVESTMENT)
                .transactionTimeStamp(LocalDateTime.now())
                .build();

        long finalBalance=accounts.getBalance()-transactionsDto.getTransactionAmount();
        Accounts accountStateAfterTransaction=Accounts.builder()
                .accountNumber(1L)
                .accountType(AllEnumConstantHelpers.AccountType.SAVINGS)
                .accountStatus(AllEnumConstantHelpers.AccountStatus.OPEN)
                .anyActiveLoans(false)
                .approvedLoanLimitBasedOnCreditScore(500000L)
                .branchCode(branchCode)
                .totalOutStandingAmountPayableToBank(500000L)
                .transferLimitPerDay(25000L)
                .totLoanIssuedSoFar(450000L)
                .creditScore(750)
                .homeBranch(AllEnumConstantHelpers.Branch.KOLKATA)
                .balance(finalBalance)
                .build();

        Transactions transactionsState= Transactions.builder()
                .transactionId(1L)
                .transactionAmount(100000L)
                .transactionType(Transactions.TransactionType.DEBIT)
                .transactedAccountNumber("123")
                .description(Transactions.DescriptionType.INVESTMENT)
                .accounts(accountStateAfterTransaction)
                .build();
        transactionsState.setTransactionTimeStamp(LocalDateTime.now());
        accountStateAfterTransaction.setListOfTransactions(Collections.singletonList(transactionsState));

        when(accountsRepositoryMock.save(any()))
                .thenReturn(accountStateAfterTransaction);
        when(transactionsRepositoryMock.save(any()))
                .thenReturn(transactionsState);

        OutputDto response=transactionsService.transactionsExecutor(transactionsDto);
        assertNotNull(response.getTransactions());
        assertEquals(finalBalance,response.getAccounts().getBalance());
    }

    @Test
    public void payOrDepositMoneyTestForDebitForEShopping(){
        String branchCode=CodeRetrieverHelper.getBranchCode(AllEnumConstantHelpers.Branch.KOLKATA);
        when(accountsRepositoryMock.findByAccountNumber(anyLong()))
                .thenReturn(Optional.of(accounts));

        TransactionsDto transactionsDto=TransactionsDto.builder()
                .transactionId(1L)
                .accountNumber(1L)
                .transactionAmount(100000L)
                .transactionType(Transactions.TransactionType.DEBIT)
                .transactedAccountNumber("123")
                .description(Transactions.DescriptionType.E_SHOPPING)
                .transactionTimeStamp(LocalDateTime.now())
                .build();

        long finalBalance=accounts.getBalance()-transactionsDto.getTransactionAmount();
        Accounts accountStateAfterTransaction=Accounts.builder()
                .accountNumber(1L)
                .accountType(AllEnumConstantHelpers.AccountType.SAVINGS)
                .accountStatus(AllEnumConstantHelpers.AccountStatus.OPEN)
                .anyActiveLoans(false)
                .approvedLoanLimitBasedOnCreditScore(500000L)
                .branchCode(branchCode)
                .totalOutStandingAmountPayableToBank(500000L)
                .transferLimitPerDay(25000L)
                .totLoanIssuedSoFar(450000L)
                .creditScore(750)
                .homeBranch(AllEnumConstantHelpers.Branch.KOLKATA)
                .balance(finalBalance)
                .build();

        Transactions transactionsState= Transactions.builder()
                .transactionId(1L)
                .transactionAmount(100000L)
                .transactionType(Transactions.TransactionType.DEBIT)
                .transactedAccountNumber("123")
                .description(Transactions.DescriptionType.E_SHOPPING)
                .accounts(accountStateAfterTransaction)
                .build();
        transactionsState.setTransactionTimeStamp(LocalDateTime.now());
        accountStateAfterTransaction.setListOfTransactions(Collections.singletonList(transactionsState));

        when(accountsRepositoryMock.save(any()))
                .thenReturn(accountStateAfterTransaction);
        when(transactionsRepositoryMock.save(any()))
                .thenReturn(transactionsState);

        OutputDto response=transactionsService.transactionsExecutor(transactionsDto);
        assertNotNull(response.getTransactions());
        assertEquals(finalBalance,response.getAccounts().getBalance());
    }
}
