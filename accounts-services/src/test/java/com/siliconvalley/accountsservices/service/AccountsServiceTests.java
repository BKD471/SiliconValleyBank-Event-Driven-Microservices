package com.siliconvalley.accountsservices.service;

import com.siliconvalley.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.BadApiRequestException;
import com.siliconvalley.accountsservices.exception.CustomerException;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.helpers.CodeRetrieverHelper;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.Customer;
import com.siliconvalley.accountsservices.model.Role;
import com.siliconvalley.accountsservices.repository.IAccountsRepository;
import com.siliconvalley.accountsservices.repository.ICustomerRepository;
import com.siliconvalley.accountsservices.repository.IRoleRepository;
import org.junit.jupiter.api.Assertions;
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
    private IImageService fileService;

    @MockBean
    private IRoleRepository roleRepositoryMock;

    @MockBean
    private ICustomerRepository customerRepositoryMock;

    @MockBean
    private IAccountsRepository accountsRepositoryMock;

    private Customer customer;
    private Accounts accounts;

    private Role normalRole;

    @Value("${customer.profile.images.path}")
    private String IMAGE_PATH;

    @Value("${normal.role.id}")
    private String NORMAL_ROLE_ID;

    @BeforeEach
    public  void init() {
        String branchCode = CodeRetrieverHelper.getBranchCode(AllConstantHelpers.Branch.KOLKATA);
        accounts = Accounts.builder()
                .accountNumber("1L")
                .accountType(AllConstantHelpers.AccountType.SAVINGS)
                .accountStatus(AllConstantHelpers.AccountStatus.OPEN)
                .anyActiveLoans(false)
                .approvedLoanLimitBasedOnCreditScore(500000L)
                .balance(60000L)
                .branchCode(branchCode)
                .totalOutStandingAmountPayableToBank(500000L)
                .transferLimitPerDay(25000L)
                .totLoanIssuedSoFar(450000L)
                .creditScore(750)
                .homeBranch(AllConstantHelpers.Branch.KOLKATA)
                .build();

        accounts.setCreatedDate(LocalDate.of(1990,12,01));

        normalRole= Role.builder().roleId(NORMAL_ROLE_ID).roleName("NORMAL").build();

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
                .roles(new HashSet<>(Collections.singleton(normalRole)))
                .accounts(Collections.singletonList(accounts))
                .build();
        accounts.setCustomer(customer);

    }


    @Test
    @DisplayName("Test the create accounts")
    public void createAccountTest() {
        when(roleRepositoryMock.findById(anyString()))
                .thenReturn(Optional.of(normalRole));
        when(customerRepositoryMock.findById(anyString()))
                .thenReturn(Optional.of(customer));
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));
        when(customerRepositoryMock.save(any()))
                .thenReturn(customer);

        String branchCode = CodeRetrieverHelper.getBranchCode(AllConstantHelpers.Branch.KOLKATA);
        PostInputRequestDto postInputRequestDto = PostInputRequestDto.builder()
                .updateRequest(AllConstantHelpers.UpdateRequest.CREATE_ACC)
                .name("phoenix")
                .email("phoenix@gmail.com")
                .password("pass")
                .phoneNumber("91-1234567890")
                .homeBranch(AllConstantHelpers.Branch.KOLKATA)
                .dateOfBirthInYYYYMMDD(String.valueOf(LocalDate.of(1997, 12, 01)))
                .adharNumber("adhar")
                .panNumber("pan")
                .voterId("voter")
                .address("address")
                .drivingLicense("driving")
                .passportNumber("passport")
                .accountType(AllConstantHelpers.AccountType.SAVINGS)
                .branchCode(branchCode)
                .transferLimitPerDay(25000)
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
        assertEquals(60000, response.getAccounts().getBalance(),"Customer balance should have matched");
        assertEquals(25000, response.getAccounts().getTransferLimitPerDay(),"Customer transferLimit should have matched");
        assertEquals(750, response.getAccounts().getCreditScore(),"Customer credit score should have matched");
    }

    @Test
    @DisplayName("Test add accounts")
    public void addAccountTest() throws IOException {
        String branchCode = CodeRetrieverHelper.getBranchCode(AllConstantHelpers.Branch.CHENNAI);
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));
        when(customerRepositoryMock.findById(anyString()))
                .thenReturn(Optional.of(customer));
        Accounts processedAccount = Accounts.builder()
                .accountNumber("2L")
                .accountType(AllConstantHelpers.AccountType.SAVINGS)
                .accountStatus(AllConstantHelpers.AccountStatus.OPEN)
                .anyActiveLoans(false)
                .approvedLoanLimitBasedOnCreditScore(900000L)
                .balance(90000L)
                .branchCode(branchCode)
                .totalOutStandingAmountPayableToBank(500000L)
                .transferLimitPerDay(85000L)
                .totLoanIssuedSoFar(550000L)
                .creditScore(850)
                .homeBranch(AllConstantHelpers.Branch.CHENNAI)
                .build();

        when(accountsRepositoryMock.save(any())).thenReturn(processedAccount);


        PutInputRequestDto putInputRequestDto = PutInputRequestDto.builder()
                .customerId("1L")
                .accountType(AllConstantHelpers.AccountType.SAVINGS)
                .updateRequest(AllConstantHelpers.UpdateRequest.ADD_ACCOUNT)
                .homeBranch(AllConstantHelpers.Branch.CHENNAI)
                .build();
        OutputDto response = accountsService.putRequestExecutor(putInputRequestDto);
        assertEquals(850, response.getAccounts().getCreditScore(),"Account CreditScore should have matched");
        assertEquals(90000L, response.getAccounts().getBalance(),"Account Balance should have matched");
        assertEquals(AllConstantHelpers.Branch.CHENNAI, response.getAccounts().getHomeBranch(),"Account Branch should have matched");
    }

    @Test
    @DisplayName("Adding accounts failed when no of accounts exceeds the permissible limit")
    public void addAccountValidationForMaxPermissibleAccountTest(){
        when(customerRepositoryMock.findById(anyString())).thenReturn(Optional.of(customer));

        List<Accounts> accountsList = new ArrayList<>();
        int MAX_PERMISSIBLE_ACCOUNTS = 5;
        for (int i = 0; i < MAX_PERMISSIBLE_ACCOUNTS; i++) {
            accountsList.add(new Accounts());
        }
        customer.setAccounts(accountsList);
        PutInputRequestDto putInputRequestDto = PutInputRequestDto.builder()
                .customerId("1L")
                .accountType(AllConstantHelpers.AccountType.SAVINGS)
                .updateRequest(AllConstantHelpers.UpdateRequest.ADD_ACCOUNT)
                .homeBranch(AllConstantHelpers.Branch.CHENNAI)
                .build();
        Assertions.assertThrows(AccountsException.class, () -> {
            accountsService.putRequestExecutor(putInputRequestDto);
        },"AccountsException should have been thrown");

    }

    @Test
    @DisplayName("Adding accounts failed for invalid Customer Id")
    public void AddAccountFailedForInvalidCustomerIdTest(){
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));
        when(customerRepositoryMock.findById(anyString())).thenReturn(Optional.of(customer))
                .thenReturn(Optional.empty());

        PutInputRequestDto putInputRequestDto = PutInputRequestDto.builder()
                .customerId("1L")
                .accountType(AllConstantHelpers.AccountType.SAVINGS)
                .updateRequest(AllConstantHelpers.UpdateRequest.ADD_ACCOUNT)
                .homeBranch(AllConstantHelpers.Branch.CHENNAI)
                .build();
        assertThrows(AccountsException.class,
                () -> {
                    accountsService.putRequestExecutor(putInputRequestDto);
                },"AccountsException should have been thrown");
    }

    @Test
    @DisplayName("Test update home branch")
    public void updateHomeBranchTest() throws IOException {
        String newBranchCode = CodeRetrieverHelper.getBranchCode(AllConstantHelpers.Branch.BANGALORE);
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));
        when(customerRepositoryMock.findById(anyString()))
                .thenReturn(Optional.of(customer));

        PutInputRequestDto putInputRequestDto = PutInputRequestDto.builder()
                .accountNumber("1L")
                .homeBranch(AllConstantHelpers.Branch.BANGALORE)
                .updateRequest(AllConstantHelpers.UpdateRequest.UPDATE_HOME_BRANCH).build();

        Accounts savedAccount = Accounts.builder()
                .accountNumber("1L")
                .homeBranch(AllConstantHelpers.Branch.BANGALORE)
                .branchCode(newBranchCode)
                .accountType(AllConstantHelpers.AccountType.CURRENT)
                .build();
        savedAccount.setCustomer(customer);
        when(accountsRepositoryMock.save(any())).thenReturn(savedAccount);
        OutputDto response = accountsService.putRequestExecutor(putInputRequestDto);

        assertEquals(AllConstantHelpers.Branch.BANGALORE, response.getAccounts().getHomeBranch(),"Accounts Branch should have matched");
        assertEquals(newBranchCode, response.getAccounts().getBranchCode(),"Account Branch COde should have matched");
    }

    @Test
    @DisplayName("Update home branch failed when there is already another account with same type ")
    public void updateHomeBranchFailedTest(){
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));
        when(customerRepositoryMock.findById(anyString()))
                .thenReturn(Optional.of(customer));

        PutInputRequestDto putInputRequestDto = PutInputRequestDto.builder()
                .accountNumber("1L")
                .homeBranch(AllConstantHelpers.Branch.KOLKATA)
                .updateRequest(AllConstantHelpers.UpdateRequest.UPDATE_HOME_BRANCH).build();

        assertThrows(AccountsException.class, () -> {
            accountsService.putRequestExecutor(putInputRequestDto);
        },"AccountsException should have been thrown");
    }

    @Test
    @DisplayName("Loading customer Failed for invalid customerId")
    public void invalidCustomerIdFailedTest() {
        when(customerRepositoryMock.findById(anyString()))
                .thenReturn(Optional.empty());
        PutInputRequestDto putInputRequestDto = PutInputRequestDto.builder()
                .customerId("1L").build();

        Assertions.assertThrows(CustomerException.class,
                () -> {
                    accountsService.putRequestExecutor(putInputRequestDto);
                },"Customer Exception not being thrown");
    }

    @Test
    @DisplayName("Loading account failed for invalid accountNumber")
    public void invalidAccountNumberFailedTest() {
        when(accountsRepositoryMock.findByAccountNumber(anyString())).
                thenReturn(Optional.empty());
        PutInputRequestDto putInputRequestDto = PutInputRequestDto.builder()
                .accountNumber("47L").build();
        assertThrows(AccountsException.class,
                () -> {
                    accountsService.putRequestExecutor(putInputRequestDto);
                },"AccountsException should have been thrown");
    }

    @Test
    @DisplayName("Test update customer details")
    public void updateCustomerDataTest() throws IOException {
        when(customerRepositoryMock.findById(anyString()))
                .thenReturn(Optional.of(customer));
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));
        Customer updatedCustomer= Customer.builder()
                .customerId("1L")
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

        when(customerRepositoryMock.save(any())).thenReturn(updatedCustomer);
        List<Accounts> accountsList = new ArrayList<>();
        for (int i = 0; i < 5; i++) accountsList.add(new Accounts());
        Page<Accounts> allPagedAccounts = new PageImpl<>(accountsList);
        when(accountsRepositoryMock.findAllByCustomer_CustomerId(anyString(),any(Pageable.class)))
                .thenReturn(Optional.of(allPagedAccounts));

        PutInputRequestDto request = PutInputRequestDto.builder()
                .customerId("1L")
                .updateRequest(AllConstantHelpers.UpdateRequest.UPDATE_CUSTOMER_DETAILS)
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
    @DisplayName("Test upload profile image")
    public void uploadProfileImageTest() throws IOException{
        UUID imageId = UUID.randomUUID();
        mockStatic(UUID.class);
        when(UUID.randomUUID()).thenReturn(imageId);

        when(customerRepositoryMock.findById(anyString())).thenReturn(Optional.of(customer));
        Customer customerWithUploadedImage=Customer.builder()
                .customerId("1L")
                .imageName(imageId.toString())
                .build();

        when(customerRepositoryMock.save(any())).thenReturn(customerWithUploadedImage);


        MockMultipartFile imgFile =
                new MockMultipartFile("data", "uploadedFile.png", "text/plain", "some kml".getBytes());
        PutInputRequestDto request= PutInputRequestDto.builder()
                .customerId("1L")
                .updateRequest(AllConstantHelpers.UpdateRequest.UPLOAD_CUSTOMER_IMAGE)
                .customerImage(imgFile)
                .build();

        OutputDto response=accountsService.putRequestExecutor(request);
        assertEquals(customerWithUploadedImage.getImageName()+".png",response.getCustomer().getImageName(),
                "Image should have been uploaded");
       verify(customerRepositoryMock,times(1)).save(any());
    }

    @Test
    @DisplayName("Test block account")
    public void blockAccountTest() throws AccountsException, IOException {
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));
        Accounts blockedAccount=Accounts.builder()
                .accountNumber("1L")
                .accountStatus(AllConstantHelpers.AccountStatus.BLOCKED)
                .build();
        when(accountsRepositoryMock.save(any())).thenReturn(blockedAccount);

        PutInputRequestDto request= PutInputRequestDto.builder()
                .accountNumber("1L")
                .updateRequest(AllConstantHelpers.UpdateRequest.BLOCK_ACC)
                .build();

        accountsService.putRequestExecutor(request);
        verify(accountsRepositoryMock,times(1)).save(accounts);
    }

    @Test
    @DisplayName("Failed blocking account test")
    public void blockAccountFailedTest() throws AccountsException {
        Accounts blockedAccounts= accounts;
        blockedAccounts.setAccountStatus(AllConstantHelpers.AccountStatus.BLOCKED);
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(blockedAccounts));

        PutInputRequestDto request= PutInputRequestDto.builder()
                .accountNumber("1L")
                .updateRequest(AllConstantHelpers.UpdateRequest.BLOCK_ACC)
                .build();

        assertThrows(AccountsException.class,()->{accountsService.putRequestExecutor(request);});
    }

    @Test
    @DisplayName("Test close account")
    public void closeAccountTest() throws AccountsException, IOException {
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));
        Accounts closedAccount=Accounts.builder()
                .accountNumber("1L")
                .accountStatus(AllConstantHelpers.AccountStatus.CLOSED)
                .build();
        when(accountsRepositoryMock.save(any())).thenReturn(closedAccount);

        PutInputRequestDto request= PutInputRequestDto.builder()
                .accountNumber("1L")
                .updateRequest(AllConstantHelpers.UpdateRequest.CLOSE_ACC)
                .build();

        accountsService.putRequestExecutor(request);
        verify(accountsRepositoryMock,times(1)).save(accounts);
    }

    @Test
    @DisplayName("Test the reopening of closed account")
    public void reOpenClosedAccountTest() throws AccountsException, IOException {

        Accounts openedAccount=Accounts.builder()
                .accountNumber("1L")
                .accountStatus(AllConstantHelpers.AccountStatus.OPEN)
                .build();
        Accounts closedAccount=Accounts.builder()
                .accountNumber("1L")
                .accountStatus(AllConstantHelpers.AccountStatus.CLOSED)
                .build();
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(closedAccount));
        when(accountsRepositoryMock.save(any())).thenReturn(openedAccount);

        PutInputRequestDto request= PutInputRequestDto.builder()
                .accountNumber("1L")
                .updateRequest(AllConstantHelpers.UpdateRequest.RE_OPEN_ACC)
                .accountStatus(AllConstantHelpers.AccountStatus.CLOSED)
                .build();

        accountsService.putRequestExecutor(request);
        verify(accountsRepositoryMock,times(1)).save(closedAccount);
    }

    @Test
    @DisplayName("Test delete account")
    public void deleteAccountTest() throws AccountsException{
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        DeleteInputRequestDto request= DeleteInputRequestDto.builder()
                .accountNumber("1L")
                .updateRequest(AllConstantHelpers.UpdateRequest.DELETE_ACC)
                .accountStatus(AllConstantHelpers.AccountStatus.OPEN)
                .build();

        accountsService.deleteRequestExecutor(request);
        verify(accountsRepositoryMock,times(1)).deleteByAccountNumber("1L");
    }


    @Test
    @DisplayName("Test fetching the account information")
    public  void getAccountInfoTest() throws AccountsException,IOException{
        when(accountsRepositoryMock.findByAccountNumber(anyString())).thenReturn(Optional.of(accounts));
        when(roleRepositoryMock.findById(anyString())).thenReturn(Optional.of(normalRole));

        GetInputRequestDto request=GetInputRequestDto.builder()
                .accountNumber("1L")
                .updateRequest(AllConstantHelpers.UpdateRequest.GET_ACC_INFO)
                .build();

        OutputDto response=accountsService.getRequestExecutor(request);
        assertNotNull(response.getAccounts(),"Accounts should nt be null");
        assertEquals(accounts.getAccountNumber(),response.getAccounts().getAccountNumber(),
                "Account NUmber should also be equal");
    }

    @Test
    @DisplayName("Create Account Failed coz Another account with different customer has same credentials")
    public void createAccountFailedTest() {
        when(customerRepositoryMock.findById(anyString())).thenReturn(Optional.of(customer));
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));
        when(customerRepositoryMock.save(any())).thenReturn(customer);

        String branchCode = CodeRetrieverHelper.getBranchCode(AllConstantHelpers.Branch.KOLKATA);
        PostInputRequestDto postInputRequestDto = PostInputRequestDto.builder()
                .updateRequest(AllConstantHelpers.UpdateRequest.CREATE_ACC)
                .adharNumber("adhar")
                .build();

        Customer customerWithDuplicateCredentials=Customer.builder()
                .adharNumber("adhar")
                .build();
        Accounts accountsWithDuplicatedCredentials=Accounts.builder()
                .accountNumber("3L")
                .customer(customerWithDuplicateCredentials)
                .build();
        List<Accounts> duplicateAccountThatWillCauseException=Collections
                .singletonList(accountsWithDuplicatedCredentials);
        when(accountsRepositoryMock.findAll()).thenReturn(duplicateAccountThatWillCauseException);
        assertThrows(AccountsException.class,()->{
             accountsService.accountSetUp(postInputRequestDto);
        });
    }

    @Test
    @DisplayName("Test the increment of transfer limit per day")
    public void increaseTransferLimitPerDayTest() throws IOException {
        when(accountsRepositoryMock.findByAccountNumber(anyString())).thenReturn(Optional.of(accounts));
        PutInputRequestDto request= PutInputRequestDto.builder()
                .accountNumber("1L")
                .updateRequest(AllConstantHelpers.UpdateRequest.INC_TRANSFER_LIMIT)
                .transferLimitPerDay(125000L)
                .build();

        Accounts savedAccount=Accounts.builder()
                .accountNumber("1L")
                .transferLimitPerDay(125000L)
                .build();
        when(accountsRepositoryMock.save(any())).thenReturn(savedAccount);
        OutputDto response=accountsService.putRequestExecutor(request);
        assertEquals(125000L,response.getAccounts()
                .getTransferLimitPerDay(),"Transfer limit should have updated");
    }

    @Test
    @DisplayName("Transfer Limit failed for accounts less than six months old")
    public void increaseTransferLimitPerDayFailedTest() {
        accounts.setCreatedDate(LocalDate.now());
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));
        PutInputRequestDto request= PutInputRequestDto.builder()
                .accountNumber("1L")
                .updateRequest(AllConstantHelpers.UpdateRequest.INC_TRANSFER_LIMIT)
                .transferLimitPerDay(125000L)
                .build();

        assertThrows(AccountsException.class,()->{
            accountsService.putRequestExecutor(request);
        },"Should have thrown accounts Exception");
    }

    @Test
    @DisplayName("Delete all accounts by customer")
    public void deleteAllAccountsByCustomerTest(){
        when(customerRepositoryMock.findById(anyString())).thenReturn(Optional.of(customer));
        DeleteInputRequestDto request=DeleteInputRequestDto.builder()
                .updateRequest(AllConstantHelpers.UpdateRequest.DELETE_ALL_ACC)
                .customerId("1L")
                .build();

        accountsService.deleteRequestExecutor(request);
        verify(accountsRepositoryMock,times(1))
                .deleteAllByCustomer_CustomerId(anyString());
    }

    @Test
    @DisplayName("Delete all accounts by customer Failed")
    public void deleteAllAccountsByCustomerFailedTest(){
        when(customerRepositoryMock.findById(anyString()))
                .thenReturn(Optional.empty());
        DeleteInputRequestDto request=DeleteInputRequestDto.builder()
                .updateRequest(AllConstantHelpers.UpdateRequest.DELETE_ALL_ACC)
                .customerId("1L")
                .build();

        assertThrows(AccountsException.class,()->{
            accountsService.deleteRequestExecutor(request);
        });
    }

    @Test
    @DisplayName("Get all accounts")
    public void getAllAccTest() throws AccountsException,IOException{
        when(customerRepositoryMock.findById(anyString())).thenReturn(Optional.of(customer));
        List<Accounts> accountsList = new ArrayList<>();
        for (int i = 0; i < 5; i++) accountsList.add(new Accounts());
        Page<Accounts> allPagedAccounts = new PageImpl<>(accountsList);
        when(accountsRepositoryMock.findAllByCustomer_CustomerId(anyString(),any(Pageable.class)))
                .thenReturn(Optional.of(allPagedAccounts));

        GetInputRequestDto request= GetInputRequestDto.builder()
                .customerId("1L")
                .updateRequest(AllConstantHelpers.UpdateRequest.GET_ALL_ACC)
                .build();

        accountsService.getRequestExecutor(request);
        verify(accountsRepositoryMock,times(1))
                .findAllByCustomer_CustomerId(anyString(),any(Pageable.class));
    }

    @Test
    @DisplayName("Get all accounts Failed for customer with no accounts")
    public void getAllAccTestFailedForCustomerWithNoAccounts() throws AccountsException{
        when(customerRepositoryMock.findById(anyString())).thenReturn(Optional.of(customer));
        when(accountsRepositoryMock.findAllByCustomer_CustomerId(anyString(),any(Pageable.class)))
                .thenReturn(Optional.empty());

        GetInputRequestDto request= GetInputRequestDto.builder()
                .customerId("1L")
                .updateRequest(AllConstantHelpers.UpdateRequest.GET_ALL_ACC)
                .build();

       assertThrows(AccountsException.class,()->{
           accountsService.getRequestExecutor(request);
       });
    }

    @Test
    @DisplayName("Invalid request type for get")
    public  void invalidGetRequestType() throws AccountsException {
        GetInputRequestDto request= GetInputRequestDto.builder()
                .updateRequest(AllConstantHelpers.UpdateRequest.ADD_ACCOUNT).build();
        assertThrows(AccountsException.class,()->{
            accountsService.getRequestExecutor(request);
        });
    }

    @Test
    @DisplayName("Invalid request type for put")
    public  void invalidPutRequestType(){
        PutInputRequestDto request= PutInputRequestDto.builder()
                .updateRequest(AllConstantHelpers.UpdateRequest.GET_ACC_INFO).build();
        assertThrows(AccountsException.class,()->{
            accountsService.putRequestExecutor(request);
        });
    }

    @Test
    @DisplayName("Invalid request type for post")
    public  void invalidPostRequestType(){
        PostInputRequestDto request= PostInputRequestDto.builder()
                .updateRequest(AllConstantHelpers.UpdateRequest.DELETE_ACC).build();
        assertThrows(AccountsException.class,()->{
            accountsService.postRequestExecutor(request);
        });
    }

    @Test
    @DisplayName("Invalid request type for delete")
    public  void invalidDeleteRequestType(){
        DeleteInputRequestDto request= DeleteInputRequestDto.builder()
                .updateRequest(AllConstantHelpers.UpdateRequest.UPDATE_CREDIT_SCORE).build();
        assertThrows(AccountsException.class,()->{
            accountsService.deleteRequestExecutor(request);
        });
    }

    @Test
    @DisplayName("Null update Request type for get")
    public  void nullUpdateGetRequestFailedTest(){
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        GetInputRequestDto request=GetInputRequestDto.builder()
                .accountNumber("1L")
                .updateRequest(null)
                .build();

        assertThrows(AccountsException.class,()->{
            accountsService.getRequestExecutor(request);
        });
    }

    @Test
    @DisplayName("Null update Request type for put")
    public  void nullUpdatePutRequestFailedTest(){
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        PutInputRequestDto request=PutInputRequestDto.builder()
                .accountNumber("1L")
                .updateRequest(null)
                .build();

        assertThrows(AccountsException.class,()->{
            accountsService.putRequestExecutor(request);
        });
    }

    @Test
    @DisplayName("Null update Request type for post")
    public  void nullUpdatePostRequestFailedTest(){
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));
        PostInputRequestDto request=PostInputRequestDto.builder()
                .accountNumber("1L")
                .updateRequest(null)
                .build();

        assertThrows(AccountsException.class,()->{
            accountsService.postRequestExecutor(request);
        });
    }

    @Test
    @DisplayName("Null update Request type for delete")
    public  void nullUpdateDeleteRequestFailedTest(){
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        DeleteInputRequestDto request=DeleteInputRequestDto.builder()
                .accountNumber("1L")
                .updateRequest(null)
                .build();

        assertThrows(AccountsException.class,()->{
            accountsService.deleteRequestExecutor(request);
        });
    }

    @Test
    @DisplayName("Put request Failed for Invalid page SIze")
    public void invalidPageSizeForUpdateCustomerDataTest(){
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));
        PutInputRequestDto request=PutInputRequestDto.builder()
                .accountNumber("1L")
                .pageSize(-69)
                .build();

        assertThrows(BadApiRequestException.class,()->{
           accountsService.putRequestExecutor(request);
        });
    }

    @Test
    @DisplayName("request Failed for Invalid page filed")
    public void invalidPageFieldForGetALLACCTest(){
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));
        when(customerRepositoryMock.findById(anyString()))
                .thenReturn(Optional.of(customer));
        GetInputRequestDto request=GetInputRequestDto.builder()
                .accountNumber("1L")
                .customerId("1L")
                .sortBy("INVALID FIELD")
                .updateRequest(AllConstantHelpers.UpdateRequest.GET_ALL_ACC)
                .build();

        assertThrows(BadApiRequestException.class,()->{
            accountsService.getRequestExecutor(request);
        });
    }
}