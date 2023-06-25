package com.example.accountsservices.controller;


import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.helpers.CodeRetrieverHelper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.service.IBeneficiaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDate;
import java.util.Collections;

import static com.example.accountsservices.helpers.MapperHelper.*;
import static com.example.accountsservices.helpers.MapperHelper.mapToAccountsDto;
import static com.example.accountsservices.helpers.ObjectToJsonStringConverterHelper.convertObjToJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BeneficiaryControllerTest {

    @MockBean
    @Qualifier("beneficiaryServicePrimary")
    private IBeneficiaryService beneficiaryServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Value("${test.token}")
    private String token;

    Accounts accounts;
    Customer customer;
    Beneficiary beneficiary;
    OutputDto dto;
    ObjectMapper objectMapper;

    private final String BASE_URL_BENEFICIARY="/api/v1/beneficiary/";
    @BeforeEach
    public void init(){
        String branchCode= CodeRetrieverHelper.getBranchCode(Accounts.Branch.KOLKATA);
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

        accounts.setCreatedDate(LocalDate.of(1990,12,01));

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
                .accounts(Collections.singletonList(accounts))
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
                .relation(Beneficiary.RELATION.SON)
                .accounts(accounts)
                .build();

        accounts.setCustomer(customer);
        accounts.setListOfBeneficiary(Collections.singletonList(beneficiary));
        customer.setAccounts(Collections.singletonList(accounts));

        dto=OutputDto.builder()
                .defaultMessage("Account with id 1 is created for customer 1")
                .customer(mapToCustomerOutputDto(mapToCustomerDto(customer)))
                .accounts(mapToAccountsOutputDto(mapToAccountsDto(accounts)))
                .beneficiary(mapToBeneficiaryDto(beneficiary))
                .transactions(null)
                .accountsListPages(null)
                .beneficiaryListPages(null)
                .beneficiaryList(null)
                .listOfAccounts(null)
                .transactionsList(null)
                .build();
    }

    @Test
    @DisplayName("Test the get requests")
    public void getRequestBenForChangeTest() throws Exception {
        when(beneficiaryServiceMock.getRequestBenExecutor(any())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL_BENEFICIARY+"get")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjToJsonString(dto))
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.beneficiary").exists());
    }

    @Test
    @DisplayName("Test the post requests")
    public void postRequestForChangeTest() throws Exception {
        when(beneficiaryServiceMock.postRequestBenExecutor(any())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL_BENEFICIARY+"post")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjToJsonString(dto))
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.beneficiary").exists());
    }

    @Test
    @DisplayName("Test the put requests")
    public void putRequestForChangeTest() throws Exception {
        when(beneficiaryServiceMock.putRequestBenExecutor(any())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL_BENEFICIARY+"put")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjToJsonString(dto))
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print()).andExpect(status().isAccepted())
                .andExpect(jsonPath("$.beneficiary").exists());
    }

    @Test
    @DisplayName("Test the delete requests")
    public void deleteRequestForChangeTest() throws Exception {
        when(beneficiaryServiceMock.deleteRequestBenExecutor(any())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL_BENEFICIARY+"delete")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjToJsonString(dto))
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print()).andExpect(status().isAccepted())
                .andExpect(jsonPath("$.beneficiary").exists());
    }
}
