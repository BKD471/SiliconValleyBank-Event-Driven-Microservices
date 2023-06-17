package com.example.accountsservices.service;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.example.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.CustomerException;
import com.example.accountsservices.helpers.CodeRetrieverHelper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountsServiceTests {
    @Qualifier("accountsServicePrimary")
    @Autowired
    private IAccountsService accountsService;

    @Qualifier("fileServicePrimary")
    @Autowired
    private IFileService fileService;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private AccountsRepository accountsRepository;

    Customer customer;
    Accounts accounts;

    private final int MAX_PERMISSIBLE_ACCOUNTS = 5;

    @Value("${customer.profile.images.path}")
    private String IMAGE_PATH;

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
                .accounts(Collections.singletonList(accounts))
                .build();
        accounts.setCustomer(customer);
    }


    @Test
    public void createAccountTest() {
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

        assertEquals("phoenix@gmail.com", response.getCustomer().getEmail(),"Customer Email should have matched");
        assertEquals("passport", response.getCustomer().getPassportNumber(),"Customer Passport should have matched");
        assertEquals("address", response.getCustomer().getAddress(),"Customer address should have matched");
        assertEquals("adhar", response.getCustomer().getAdharNumber(),"Customer adhar should have matched");
        assertEquals("voter", response.getCustomer().getVoterId(),"Customer voter should have matched");
        assertEquals("driving", response.getCustomer().getDrivingLicense(),"Customer driving should have matched");
        assertEquals("pan", response.getCustomer().getPanNumber(),"Customer pan should have matched");
        assertEquals(25, response.getCustomer().getAge(),"Customer age should have matched");
        assertEquals(LocalDate.of(1997, 12, 01), response.getCustomer().getDateOfBirth(),
                "Customer dob should have matched");
        assertEquals(60000L, response.getAccounts().getBalance(),"Customer balance should have matched");
        assertEquals(25000L, response.getAccounts().getTransferLimitPerDay(),"Customer transferLimit should have matched");
        assertEquals(750, response.getAccounts().getCreditScore(),"Customer credit score should have matched");
    }

    @Test
    public void addAccountTest() throws IOException {
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
        assertEquals(850, response.getAccounts().getCreditScore(),"Account CreditScore should have matched");
        assertEquals(90000L, response.getAccounts().getBalance(),"Account Balance should have matched");
        assertEquals(Accounts.Branch.CHENNAI, response.getAccounts().getHomeBranch(),"Account Branch should have matched");
    }

    @Test
    public void addAccountValidationForMaxPermissibleAccountTest() throws IOException {
        String branchCode = CodeRetrieverHelper.getBranchCode(Accounts.Branch.CHENNAI);
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        List<Accounts> accountsList = new ArrayList<>();
        for (int i = 0; i < MAX_PERMISSIBLE_ACCOUNTS; i++) {
            accountsList.add(new Accounts());
        }
        customer.setAccounts(accountsList);
        PutInputRequestDto putInputRequestDto = PutInputRequestDto.builder()
                .customerId(1L)
                .accountType(Accounts.AccountType.SAVINGS)
                .updateRequest(AccountsDto.UpdateRequest.ADD_ACCOUNT)
                .homeBranch(Accounts.Branch.CHENNAI)
                .build();
        assertThrows(AccountsException.class, () -> {
            accountsService.putRequestExecutor(putInputRequestDto);
        },"AccountsException should have been thrown");

    }

    @Test
    public void AddAccountFailedForInvalidCustomerIdTest() throws IOException {
        String branchCode = CodeRetrieverHelper.getBranchCode(Accounts.Branch.CHENNAI);
        when(accountsRepository.findByAccountNumber(anyLong())).thenReturn(Optional.of(accounts));
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer)).thenReturn(Optional.empty());

        PutInputRequestDto putInputRequestDto = PutInputRequestDto.builder()
                .customerId(1L)
                .accountType(Accounts.AccountType.SAVINGS)
                .updateRequest(AccountsDto.UpdateRequest.ADD_ACCOUNT)
                .homeBranch(Accounts.Branch.CHENNAI)
                .build();
        assertThrows(AccountsException.class,
                () -> {
                    accountsService.putRequestExecutor(putInputRequestDto);
                },"AccountsException should have been thrown");
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

        assertEquals(Accounts.Branch.BANGALORE, response.getAccounts().getHomeBranch(),"Accounts Branch should have matched");
        assertEquals(newBranchCode, response.getAccounts().getBranchCode(),"Account Branch COde should have matched");
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
        },"AccountsException should have been thrown");
    }

    @Test
    public void invalidCustomerIdFailedTest() throws IOException {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
        PutInputRequestDto putInputRequestDto = PutInputRequestDto.builder().customerId(1L).build();
        assertThrows(CustomerException.class,
                () -> {
                    accountsService.putRequestExecutor(putInputRequestDto);
                },"Customer Exception not being thrown");
    }

    @Test
    public void invalidAccountNumberFailedTest() throws IOException {
        when(accountsRepository.findByAccountNumber(anyLong())).thenReturn(Optional.empty());
        PutInputRequestDto putInputRequestDto = PutInputRequestDto.builder().accountNumber(47L).build();
        assertThrows(AccountsException.class,
                () -> {
                    accountsService.putRequestExecutor(putInputRequestDto);
                },"AccountsException should have been thrown");
    }

    @Test
    public void updateCustomerDataTest() throws IOException {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        Customer updatedCustomer= Customer.builder()
                .customerId(1L)
                .name("Updated Name")
                .email("updated@gmail.com")
                .phoneNumber("91-9345678912")
                .adharNumber("1234-5678-9034")
                .panNumber("GMDPD1234H")
                .voterId("vtdindeqpfc")
                .address("updated address")
                .drivingLicense("HR-0619441199191")
                .passportNumber("U6325787")
                .DateOfBirth(LocalDate.of(2000, 01, 02)).build();

        when(customerRepository.save(any())).thenReturn(updatedCustomer);
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(1, 2, sort);
        List<Accounts> accountsList = new ArrayList<>();
        for (int i = 0; i < 5; i++) accountsList.add(new Accounts());
        Page<Accounts> allPagedAccounts = new PageImpl<>(accountsList);
        when(accountsRepository.findAllByCustomer_CustomerId(anyLong(),any(Pageable.class))).thenReturn(Optional.of(allPagedAccounts));

        PutInputRequestDto request = PutInputRequestDto.builder()
                .customerId(1L)
                .updateRequest(AccountsDto.UpdateRequest.UPDATE_CUSTOMER_DETAILS)
                .name("Updated Name")
                .email("updated@gmail.com")
                .phoneNumber("91-9345678912")
                .adharNumber("1234-5678-9034")
                .panNumber("GMDPD1234H")
                .voterId("vtdindeqpfc")
                .address("updated address")
                .drivingLicense("HR-0619441199191")
                .passportNumber("U6325787")
                .dateOfBirthInYYYYMMDD(String.valueOf(LocalDate.of(2000, 01, 02)))
                .build();

        OutputDto response = accountsService.putRequestExecutor(request);
        assertNotNull(response.getCustomer(),"Customer should not be null");
        assertEquals(response.getCustomer().getEmail(),request.getEmail(),"Customer Email should have updated");
        assertEquals(response.getCustomer().getCustomerName(),request.getName(),"Customer Name should have updated");
        assertEquals(response.getCustomer().getPhoneNumber(),request.getPhoneNumber(),"Customer Phone Number should have updated");
        assertEquals(response.getCustomer().getAdharNumber(),request.getAdharNumber(),"Customer Adhar Number should have updated");
        assertEquals(response.getCustomer().getPanNumber(),request.getPanNumber(),"Customer Pan Number should have updated");
        assertEquals(response.getCustomer().getVoterId(),request.getVoterId(),"Customer Voter Id should have updated");
        assertEquals(response.getCustomer().getAddress(),request.getAddress(),"Customer Address should have updated");
        assertEquals(response.getCustomer().getDrivingLicense(),request.getDrivingLicense(),"Customer DrivingLicense should have updated");
    }

    @Test
    public void uploadProfileImageTest() throws IOException{
        UUID imageId = UUID.randomUUID();
        mockStatic(UUID.class);
        when(UUID.randomUUID()).thenReturn(imageId);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        Customer customerWithUploadedImage=Customer.builder()
                .customerId(1L)
                .imageName(imageId.toString())
                .build();

        when(customerRepository.save(any())).thenReturn(customerWithUploadedImage);


        MockMultipartFile imgfile =
                new MockMultipartFile("data", "uploadedfile.png", "text/plain", "some kml".getBytes());
        PutInputRequestDto request= PutInputRequestDto.builder()
                .customerId(1L)
                .updateRequest(AccountsDto.UpdateRequest.UPLOAD_CUSTOMER_IMAGE)
                .customerImage(imgfile)
                .build();

        OutputDto response=accountsService.putRequestExecutor(request);
        assertEquals(customerWithUploadedImage.getImageName()+".png",response.getCustomer().getImageName(),
                "Image should have been uploaded");
       verify(customerRepository,times(1)).save(any());
    }
}
