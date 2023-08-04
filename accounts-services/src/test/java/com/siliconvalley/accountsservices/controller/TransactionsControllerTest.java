package com.siliconvalley.accountsservices.controller;

import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.helpers.CodeRetrieverHelper;
import com.siliconvalley.accountsservices.helpers.MapperHelper;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.Customer;
import com.siliconvalley.accountsservices.model.Transactions;
import com.siliconvalley.accountsservices.service.ITransactionsService;
import com.siliconvalley.accountsservices.helpers.ObjectToJsonStringConverterHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TransactionsControllerTest {
    @MockBean
    @Qualifier("transactionsServicePrimary")
    private ITransactionsService transactionsServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Value("${test.token}")
    private String token;

    private static Accounts accounts;
    private static Customer customer;
    private static List<Transactions> transactionsList;
    private static OutputDto dto;
    private final String BASE_URL_TRANSACTIONS = "/api/v1/transactions/";

    @BeforeAll
    public static void init() {
        String branchCode = CodeRetrieverHelper.getBranchCode(AllConstantHelpers.Branch.KOLKATA);

        accounts = Accounts.builder()
                .accountNumber("1L")
                .accountType(AllConstantHelpers.AccountType.SAVINGS)
                .accountStatus(AllConstantHelpers.AccountStatus.OPEN)
                .anyActiveLoans(false)
                .approvedLoanLimitBasedOnCreditScore(BigDecimal.valueOf(500000L))
                .balance(BigDecimal.valueOf(500000L))
                .branchCode(branchCode)
                .totalOutStandingAmountPayableToBank(BigDecimal.valueOf(500000L))
                .transferLimitPerDay(BigDecimal.valueOf(25000L))
                .totLoanIssuedSoFar(BigDecimal.valueOf(450000L))
                .creditScore(750)
                .homeBranch(AllConstantHelpers.Branch.KOLKATA)
                .build();
        customer = Customer.builder()
                .customerId("1L")
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

        Transactions transactions1=Transactions.builder()
                .transactionId("1L")
                .transactedAccountNumber("1L")
                .transactionType(AllConstantHelpers.TransactionType.DEBIT)
                .description(AllConstantHelpers.DescriptionType.ELECTRICITY)
                .accounts(accounts)
                .transactionAmount(BigDecimal.valueOf(60000L))
                .build();
        transactions1.setTransactionTimeStamp(LocalDateTime.now());
        Transactions transactions2=Transactions.builder()
                .transactionId("2L")
                .transactedAccountNumber("2L")
                .transactionType(AllConstantHelpers.TransactionType.CREDIT)
                .description(AllConstantHelpers.DescriptionType.SALARY)
                .accounts(accounts)
                .transactionAmount(BigDecimal.valueOf(160000L))
                .build();
        transactions2.setTransactionTimeStamp(LocalDateTime.of(2023,06,17,05,40));


        transactionsList = new ArrayList<>(Arrays.asList(transactions2, transactions1));
        accounts.setListOfTransactions(transactionsList);
        List<TransactionsDto> transactionsDtoList=transactionsList.stream()
                .map(MapperHelper::mapToTransactionsDto)
                .toList();

        dto = OutputDto.builder()
                .defaultMessage("Account with id 1 is created for customer 1")
                .customer(MapperHelper.mapToCustomerOutputDto(MapperHelper.mapToCustomerDto(customer)))
                .accounts(MapperHelper.mapToAccountsOutputDto(MapperHelper.mapToAccountsDto(accounts)))
                .beneficiary(null)
                .transactions(null)
                .accountsListPages(null)
                .beneficiaryListPages(null)
                .beneficiaryList(null)
                .listOfAccounts(null)
                .transactionsList(transactionsDtoList)
                .build();
    }

    @Test
    @DisplayName("Test the past six months  transactions")
    public void getPastSixMonthsTransactionTest() throws Exception {
        long accountNumber=1L;
        when(transactionsServiceMock.getPastSixMonthsTransactionsForAnAccount(any())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL_TRANSACTIONS +accountNumber)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectToJsonStringConverterHelper.convertObjToJsonString(dto))
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionsList").exists());
    }

    @Test
    @DisplayName("Test the transactions executor")
    public void transactionsExecutorTest() throws Exception {
        when(transactionsServiceMock.transactionsExecutor(any())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL_TRANSACTIONS + "exe")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectToJsonStringConverterHelper.convertObjToJsonString(dto))
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionsList").exists());
    }
}
