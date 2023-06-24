package com.example.accountsservices.controller;



import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.helpers.CodeRetrieverHelper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.service.IAccountsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AccountsControllerTest {

    @MockBean
    @Qualifier("accountsServicePrimary")
    private IAccountsService accountsServiceMock;

    @Autowired
    private MockMvc mockMvc;

    Accounts accounts;
    Customer customer;
    Beneficiary beneficiary;
    OutputDto dto;
    ObjectMapper objectMapper;
    @BeforeEach
    public void init(){
        String branchCode=CodeRetrieverHelper.getBranchCode(Accounts.Branch.KOLKATA);
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
        accounts.setCustomer(customer);
        dto=OutputDto.builder()
                .defaultMessage("Account with id 1 is created for customer 1")
                .customer(mapToCustomerOutputDto(mapToCustomerDto(customer)))
                .accounts(mapToAccountsOutputDto(mapToAccountsDto(accounts)))
                .beneficiary(null)
                .transactions(null)
                .accountsListPages(null)
                .beneficiaryListPages(null)
                .beneficiaryList(null)
                .listOfAccounts(null)
                .transactionsList(null)
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    @Test
    @DisplayName("Test the create account")
    public void createAccountTest() throws  Exception{
      when(accountsServiceMock.accountSetUp(any())).thenReturn(dto);
      this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts/create")
              .contentType(MediaType.APPLICATION_JSON)
              .content(convertObjToJsonString(dto))
              .accept(MediaType.APPLICATION_JSON)
      ).andDo(print()).andExpect(status().isCreated())
              .andExpect(jsonPath("$.accounts").exists());
    }

    @Test
    @DisplayName("Test the get requests")
    public void getRequestForChangeTest() throws Exception {
        when(accountsServiceMock.getRequestExecutor(any())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/get")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiaGFza2FyQGdtYWlsLmNvbSIsImlhdCI6MTY4NzYzMDQ2OCwiZXhwIjoxNjg3NjQ4NDY4fQ.TBQCCSem8346uXSkrR27Md3J24hakaj8xYCA1nwrZw0h8zyZM6dmFJxTUrDOkXPZsXHVmBuPcRzpp67959H3Og")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjToJsonString(dto))
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.accounts").exists());
    }

    @Test
    @DisplayName("Test the post requests")
    public void postRequestForChangeTest() throws Exception {
        when(accountsServiceMock.postRequestExecutor(any())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts/post")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiaGFza2FyQGdtYWlsLmNvbSIsImlhdCI6MTY4NzYzMDQ2OCwiZXhwIjoxNjg3NjQ4NDY4fQ.TBQCCSem8346uXSkrR27Md3J24hakaj8xYCA1nwrZw0h8zyZM6dmFJxTUrDOkXPZsXHVmBuPcRzpp67959H3Og")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjToJsonString(dto))
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.accounts").exists());
    }

    @Test
    @DisplayName("Test the put requests")
    public void putRequestForChangeTest() throws Exception {
        when(accountsServiceMock.putRequestExecutor(any())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts/put")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiaGFza2FyQGdtYWlsLmNvbSIsImlhdCI6MTY4NzYzMDQ2OCwiZXhwIjoxNjg3NjQ4NDY4fQ.TBQCCSem8346uXSkrR27Md3J24hakaj8xYCA1nwrZw0h8zyZM6dmFJxTUrDOkXPZsXHVmBuPcRzpp67959H3Og")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjToJsonString(dto))
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print()).andExpect(status().isAccepted())
                .andExpect(jsonPath("$.accounts").exists());
    }

    @Test
    @DisplayName("Test the delete requests")
    public void deleteRequestForChangeTest() throws Exception {
        when(accountsServiceMock.deleteRequestExecutor(any())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/accounts/delete")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiaGFza2FyQGdtYWlsLmNvbSIsImlhdCI6MTY4NzYzMDQ2OCwiZXhwIjoxNjg3NjQ4NDY4fQ.TBQCCSem8346uXSkrR27Md3J24hakaj8xYCA1nwrZw0h8zyZM6dmFJxTUrDOkXPZsXHVmBuPcRzpp67959H3Og")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjToJsonString(dto))
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print()).andExpect(status().isAccepted())
                .andExpect(jsonPath("$.accounts").exists());
    }

    private String convertObjToJsonString(Object dto) {
        try{
            ObjectWriter ow=new ObjectMapper().writer().withDefaultPrettyPrinter();
            return ow.writeValueAsString(ow);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
