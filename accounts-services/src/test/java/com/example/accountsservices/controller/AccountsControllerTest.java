package com.example.accountsservices.controller;



import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.helpers.AllEnumConstantHelpers;
import com.example.accountsservices.helpers.CodeRetrieverHelper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.service.IAccountsService;
import com.example.accountsservices.service.IFileService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collections;

import static com.example.accountsservices.helpers.MapperHelper.*;
import static com.example.accountsservices.helpers.ObjectToJsonStringConverterHelper.convertObjToJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
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

    @MockBean
    @Qualifier("fileServicePrimary")
    private IFileService fileServiceMock;

    private InputStream is;

    private final String BASE_URL_ACCOUNTS="/api/v1/accounts/";

    @Autowired
    private MockMvc mockMvc;

    @Value("${test.token}")
    private String token;


    private static Accounts accounts;
    private static Customer customer;
    private static InputStream stubInputStream;
    private static OutputDto dto;
    @BeforeAll
    public static void init() throws IOException {
         stubInputStream =
                IOUtils.toInputStream("some test data for my input stream", "UTF-8");

        String branchCode=CodeRetrieverHelper.getBranchCode(AllEnumConstantHelpers.Branch.KOLKATA);
        accounts = Accounts.builder()
                .accountNumber(1L)
                .accountType(AllEnumConstantHelpers.AccountType.SAVINGS)
                .accountStatus(AllEnumConstantHelpers.AccountStatus.OPEN)
                .anyActiveLoans(false)
                .approvedLoanLimitBasedOnCreditScore(500000L)
                .balance(60000L)
                .branchCode(branchCode)
                .totalOutStandingAmountPayableToBank(500000L)
                .transferLimitPerDay(25000L)
                .totLoanIssuedSoFar(450000L)
                .creditScore(750)
                .homeBranch(AllEnumConstantHelpers.Branch.KOLKATA)
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
    }

    @Test
    @DisplayName("Test the create account")
    public void createAccountTest() throws  Exception{
      when(accountsServiceMock.accountSetUp(any())).thenReturn(dto);
      this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL_ACCOUNTS+"create")
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
        this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL_ACCOUNTS+"get")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+token)
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
        this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL_ACCOUNTS+"post")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+token)
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
        this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL_ACCOUNTS+"put")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+token)
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
        this.mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL_ACCOUNTS+"delete")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjToJsonString(dto))
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print()).andExpect(status().isAccepted())
                .andExpect(jsonPath("$.accounts").exists());
    }

    @Test
    public  void uploadImageTest() throws Exception{
        when(accountsServiceMock.putRequestExecutor(any())).thenReturn(dto);
        long customerId=1L;

        MockMultipartFile mockMultipartFile = new MockMultipartFile("customerImage", "upload.png", "multipart/form-data", is);
        ResultActions mvcResult= this.mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT,BASE_URL_ACCOUNTS+"upload/image/"+customerId)
                        .file(mockMultipartFile)
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted());
        Assertions.assertNotNull(mvcResult.andExpect(status().isAccepted())
                .andReturn().getResponse().getContentAsString());
    }

    @Test
    public  void serveUserImageTest() throws Exception{
        when(fileServiceMock.getResource(anyString(),anyString())).thenReturn(stubInputStream);

        long customerId=1L;
        this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL_ACCOUNTS+"/serve/image/"+customerId)
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+token)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                ).andDo(print()).andExpect(status().isOk());
    }
}
