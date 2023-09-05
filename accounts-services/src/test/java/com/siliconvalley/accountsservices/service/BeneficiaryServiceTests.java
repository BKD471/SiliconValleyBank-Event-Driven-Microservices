package com.siliconvalley.accountsservices.service;

import com.siliconvalley.accountsservices.dto.inputDtos.DeleteInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.GetInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PostInputRequestDto;
import com.siliconvalley.accountsservices.dto.inputDtos.PutInputRequestDto;
import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.exception.BadApiRequestException;
import com.siliconvalley.accountsservices.exception.BeneficiaryException;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.helpers.CodeRetrieverHelper;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.Beneficiary;
import com.siliconvalley.accountsservices.model.Customer;
import com.siliconvalley.accountsservices.repository.IAccountsRepository;
import com.siliconvalley.accountsservices.repository.IBeneficiaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BeneficiaryServiceTests {
    @Autowired
    @Qualifier("beneficiaryServicePrimary")
    private IBeneficiaryService beneficiaryService;

    @MockBean
    IBeneficiaryRepository beneficiaryRepositoryMock;
    @MockBean
    IAccountsRepository accountsRepositoryMock;

    private Beneficiary beneficiary;
    private Accounts accounts;
    private Customer customer;
    private final int MAX_PERMISSIBLE_BENEFICIARIES=5;

    @BeforeEach
    public void init() {
        String branchCode = CodeRetrieverHelper.getBranchCode(AllConstantHelpers.Branch.KOLKATA);

        accounts = Accounts.builder()
                .accountNumber("1L")
                .accountType(AllConstantHelpers.AccountType.SAVINGS)
                .accountStatus(AllConstantHelpers.AccountStatus.OPEN)
                .anyActiveLoans(false)
                .approvedLoanLimitBasedOnCreditScore(BigDecimal.valueOf(500000L))
                .balance(BigDecimal.valueOf(60000L))
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

        beneficiary = Beneficiary.builder()
                .beneficiaryId("1L")
                .beneficiaryAccountNumber("1L")
                .beneficiaryName("ben 1")
                .beneficiaryEmail("ben1@gmail.com")
                .address("ben 123 street")
                .benAdharNumber("1234-5678-9999")
                .benDrivingLicense("driving-no-1")
                .benPassportNumber("passport-no-1")
                .benPhoneNumber("+91-123456789")
                .benPanNumber("GMDPD7592K")
                .benBank(AllConstantHelpers.BanksSupported.AXIS)
                .bankCode(CodeRetrieverHelper.getBankCode(AllConstantHelpers.BanksSupported.AXIS))
                .imageName("img1.png")
                .BenDate_Of_Birth(LocalDate.of(1997, 12, 01))
                .benVoterId("ben voter 1")
                .relation(SON)
                .accounts(accounts)
                .build();

        accounts.setCustomer(customer);
        accounts.setListOfBeneficiary(Collections.singletonList(beneficiary));
        customer.setAccounts(Collections.singletonList(accounts));
    }


    @Test
    public void addBeneficiaryTest() {
        String bankCOde = CodeRetrieverHelper.getBankCode(AllConstantHelpers.BanksSupported.AXIS);
        LocalDate dob = LocalDate.of(1997, 12, 01);
        int age = Period.between(dob, LocalDate.now()).getYears();


        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));
        when(accountsRepositoryMock.save(any())).thenReturn(accounts);

        PostInputRequestDto request = PostInputRequestDto.builder()
                .accountNumber("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.ADD_BEN)
                .name("ben 2")
                .email("ben2@gmail.com")
                .beneficiaryAccountNumber("3L")
                .bloodRelation(SON)
                .dateOfBirthInYYYYMMDD(String.valueOf(dob))
                .benBank(AllConstantHelpers.BanksSupported.AXIS)
                .adharNumber("1234-5678-8888")
                .address("ben 456 street")
                .phoneNumber("+91-987654321")
                .panNumber("GMDPD9876K")
                .passportNumber("passport-no-2")
                .voterId("ben voter 2")
                .drivingLicense("driving-no-2")
                .build();

        OutputDto response = beneficiaryService.postRequestBenExecutor(request);
        assertEquals(bankCOde, response.getBeneficiary().getBankCode());
        assertEquals(age, response.getBeneficiary().getBenAge());
        assertEquals(request.getEmail(), response.getBeneficiary().getBeneficiaryEmail());
    }

    @Test
    public void addBeneficiaryFailedForExceedingBeneficiaryLimitTest() {
        LocalDate dob = LocalDate.of(1997, 12, 01);

        List<Beneficiary> beneficiaryList=new ArrayList<>();
        for(int i=0;i<MAX_PERMISSIBLE_BENEFICIARIES;i++) beneficiaryList.add(new Beneficiary());
        accounts.setListOfBeneficiary(beneficiaryList);

        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));
        when(accountsRepositoryMock.save(any())).thenReturn(accounts);

        PostInputRequestDto request = PostInputRequestDto.builder()
                .accountNumber("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.ADD_BEN)
                .name("ben 2")
                .email("ben2@gmail.com")
                .beneficiaryAccountNumber("3L")
                .bloodRelation(SON)
                .dateOfBirthInYYYYMMDD(String.valueOf(dob))
                .benBank(AllConstantHelpers.BanksSupported.AXIS)
                .adharNumber("1234-5678-8888")
                .address("ben 456 street")
                .phoneNumber("+91-987654321")
                .panNumber("GMDPD9876K")
                .passportNumber("passport-no-2")
                .voterId("ben voter 2")
                .drivingLicense("driving-no-2")
                .build();

        assertThrows(BeneficiaryException.class,()->{
            beneficiaryService.postRequestBenExecutor(request);
        });
    }

    @Test
    public void addBeneficiaryFailedForConflictingAdharNumberTest() {
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        PostInputRequestDto request = PostInputRequestDto.builder()
                .accountNumber("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.ADD_BEN)
                .adharNumber("1234-5678-9999")
                .build();

        assertThrows(BeneficiaryException.class,()->{
            beneficiaryService.postRequestBenExecutor(request);
        });
    }

    @Test
    public void addBeneficiaryFailedForConflictingAdharNumberWhenSamePersonIsAddingHimselfAsBeneficiaryTest() {
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        PostInputRequestDto request = PostInputRequestDto.builder()
                .accountNumber("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.ADD_BEN)
                .email("phoenix@gmail.com")
                .adharNumber("1234-5678-9999")
                .build();

        assertThrows(BeneficiaryException.class,()->{
            beneficiaryService.postRequestBenExecutor(request);
        });
    }

    @Test
    public void addBeneficiaryFailedForConflictingRelationForFatherTest() {
        beneficiary.setRelation(FATHER);
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        PostInputRequestDto request = PostInputRequestDto.builder()
                .accountNumber("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.ADD_BEN)
                .bloodRelation(FATHER)
                .adharNumber("1234-5678-1234")
                .build();

        assertThrows(BeneficiaryException.class,()->{
            beneficiaryService.postRequestBenExecutor(request);
        });
    }

    @Test
    public void addBeneficiaryFailedForConflictingRelationForMotherTest() {
        beneficiary.setRelation(MOTHER);
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        PostInputRequestDto request = PostInputRequestDto.builder()
                .accountNumber("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.ADD_BEN)
                .bloodRelation(MOTHER)
                .adharNumber("1234-5678-1234")
                .build();

        assertThrows(BeneficiaryException.class,()->{
            beneficiaryService.postRequestBenExecutor(request);
        });
    }

    @Test
    public void addBeneficiaryFailedForConflictingRelationForSpouseTest() {
        beneficiary.setRelation(SPOUSE);
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        PostInputRequestDto request = PostInputRequestDto.builder()
                .accountNumber("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.ADD_BEN)
                .email("phoenix@gmail.com")
                .bloodRelation(SPOUSE)
                .adharNumber("1234-5678-9999")
                .build();

        assertThrows(BeneficiaryException.class,()->{
            beneficiaryService.postRequestBenExecutor(request);
        });
    }

    @Test
    public void getBeneficiaryByIdTest() {
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        GetInputRequestDto request = GetInputRequestDto.builder()
                .accountNumber("1L")
                .beneficiaryId("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.GET_BEN)
                .build();

        OutputDto response = beneficiaryService.getRequestBenExecutor(request);
        assertNotNull(response.getBeneficiary());
        assertEquals(beneficiary.getBeneficiaryEmail(),
                response.getBeneficiary().getBeneficiaryEmail());
        assertEquals(beneficiary.getBeneficiaryName(),
                response.getBeneficiary().getBeneficiaryName());
    }

    @Test
    public void getBeneficiaryByIdFailedForInvalidBenIdTest() {
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        GetInputRequestDto request = GetInputRequestDto.builder()
                .accountNumber("1L")
                .beneficiaryId("69L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.GET_BEN)
                .build();

        assertThrows(BeneficiaryException.class, () -> {
            beneficiaryService.getRequestBenExecutor(request);
        }, "Should have thrown Beneficiary Exceptions");
    }

    @Test
    public void noBeneficiaryAccountsForAnAccountTest() {
        String branchCode = CodeRetrieverHelper.getBranchCode(AllConstantHelpers.Branch.BANGALORE);
        List<Beneficiary> accountsList = new ArrayList<>();

        Accounts accountWithNoBeneficiary = Accounts.builder()
                .accountNumber("1L")
                .accountType(AllConstantHelpers.AccountType.SAVINGS)
                .accountStatus(AllConstantHelpers.AccountStatus.OPEN)
                .anyActiveLoans(false)
                .approvedLoanLimitBasedOnCreditScore(BigDecimal.valueOf(500000L))
                .balance(BigDecimal.valueOf(60000L))
                .branchCode(branchCode)
                .totalOutStandingAmountPayableToBank(BigDecimal.valueOf(500000L))
                .transferLimitPerDay(BigDecimal.valueOf(25000L))
                .totLoanIssuedSoFar(BigDecimal.valueOf(450000L))
                .listOfBeneficiary(accountsList)
                .creditScore(750)
                .homeBranch(AllConstantHelpers.Branch.BANGALORE).build();


        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accountWithNoBeneficiary));

        GetInputRequestDto request = GetInputRequestDto.builder()
                .accountNumber("1L")
                .beneficiaryId("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.GET_BEN)
                .build();

        assertThrows(BeneficiaryException.class, () -> {
            beneficiaryService.getRequestBenExecutor(request);
        });
    }

    @Test
    public void getAllBeneficiariesByAccountNumberTest() {

        List<Beneficiary> beneficiaryList = new ArrayList<>();
        for (int i = 0; i < 2; i++) beneficiaryList.add(new Beneficiary());
        Page<Beneficiary> allPagedBeneficiary = new PageImpl<>(beneficiaryList);
        when(beneficiaryRepositoryMock.findAllByAccounts_AccountNumber(anyString(), any(Pageable.class)))
                .thenReturn(Optional.of(allPagedBeneficiary));
        when(accountsRepositoryMock.findByAccountNumber(anyString())).thenReturn(Optional.of(accounts));

        GetInputRequestDto request = GetInputRequestDto.builder()
                .accountNumber("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.GET_ALL_BEN)
                .build();


        OutputDto response = beneficiaryService.getRequestBenExecutor(request);
        assertNotNull(response.getAccounts().getListOfBeneficiary());
        assertEquals(1, response.getAccounts().getListOfBeneficiary().size());
    }

    @Test
    public void getAllBeneficiariesByAccountNumberFailedFOrInvalidSortByField() {
        List<Beneficiary> beneficiaryList = new ArrayList<>();
        for (int i = 0; i < 2; i++) beneficiaryList.add(new Beneficiary());
        Page<Beneficiary> allPagedBeneficiary = new PageImpl<>(beneficiaryList);
        when(beneficiaryRepositoryMock.findAllByAccounts_AccountNumber(anyString(), any(Pageable.class)))
                .thenReturn(Optional.of(allPagedBeneficiary));
        when(accountsRepositoryMock.findByAccountNumber(anyString())).thenReturn(Optional.of(accounts));

        GetInputRequestDto request = GetInputRequestDto.builder()
                .accountNumber("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.GET_ALL_BEN)
                .sortBy("INVALID FIELD")
                .build();

        assertThrows(BadApiRequestException.class, ()->{
            beneficiaryService.getRequestBenExecutor(request);
        });
    }

    @Test
    public void getAllBeneficiariesByAccountNumberFailedFOrInvalidPageSize() {
        List<Beneficiary> beneficiaryList = new ArrayList<>();
        for (int i = 0; i < 2; i++) beneficiaryList.add(new Beneficiary());
        Page<Beneficiary> allPagedBeneficiary = new PageImpl<>(beneficiaryList);
        when(beneficiaryRepositoryMock.findAllByAccounts_AccountNumber(anyString(), any(Pageable.class)))
                .thenReturn(Optional.of(allPagedBeneficiary));
        when(accountsRepositoryMock.findByAccountNumber(anyString())).thenReturn(Optional.of(accounts));

        GetInputRequestDto request = GetInputRequestDto.builder()
                .accountNumber("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.GET_ALL_BEN)
                .sortBy("INVALID FIELD")
                .build();

        assertThrows(BadApiRequestException.class, ()->{
            beneficiaryService.getRequestBenExecutor(request);
        });
    }

    @Test
    public void noBeneficiariesPresentForAccountTest(){
            when(beneficiaryRepositoryMock.findAllByAccounts_AccountNumber(anyString(), any(Pageable.class)))
                    .thenReturn(Optional.empty());
            when(accountsRepositoryMock.findByAccountNumber(anyString())).thenReturn(Optional.of(accounts));

            GetInputRequestDto request = GetInputRequestDto.builder()
                    .accountNumber("1L")
                    .benRequest(AllConstantHelpers.BenUpdateRequest.GET_ALL_BEN)
                    .build();

            assertThrows(BeneficiaryException.class ,()->{
                beneficiaryService.getRequestBenExecutor(request);
            });
    }


    @Test
    public void updateBeneficiaryTest() {
        String bankCode = CodeRetrieverHelper.getBankCode(AllConstantHelpers.BanksSupported.ICICI);
        when(accountsRepositoryMock.findByAccountNumber(anyString())).thenReturn(Optional.of(accounts));
        Beneficiary processedAccount = Beneficiary.builder()
                .beneficiaryAccountNumber("3L")
                .beneficiaryId("1L")
                .beneficiaryName("Updated Beneficiary Name")
                .beneficiaryEmail("UpdatedEmail@gmail.com")
                .relation(DAUGHTER)
                .BenDate_Of_Birth(LocalDate.of(1997, 12, 02))
                .benBank(AllConstantHelpers.BanksSupported.ICICI)
                .benAdharNumber("9876-5432-1111")
                .benPanNumber("GMDPD6969M")
                .benPassportNumber("U6696969")
                .benPhoneNumber("+91-1234567809")
                .benVoterId("ZSG2069697")
                .benDrivingLicense("HR-0619026969697")
                .bankCode(bankCode)
                .build();

        when(beneficiaryRepositoryMock.save(any())).thenReturn(processedAccount);

        PutInputRequestDto request = PutInputRequestDto.builder()
                .accountNumber("1L")
                .beneficiaryId("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.UPDATE_BEN)
                .beneficiaryName("Updated Beneficiary Name")
                .beneficiaryAccountNumber("3L")
                .beneficiaryEmail("UpdatedEmail@gmail.com")
                .bloodRelation(DAUGHTER)
                .ben_date_of_birthInYYYYMMDD(String.valueOf(LocalDate.of(1997, 12, 02)))
                .benBank(AllConstantHelpers.BanksSupported.ICICI)
                .benAdharNumber("9876-5432-1111")
                .benPanNumber("GMDPD6969M")
                .benPassportNumber("U6696969")
                .benPhoneNumber("+91-1234567809")
                .benVoterId("ZSG2069697")
                .benDrivingLicense("HR-0619026969697")
                .build();

        OutputDto response = beneficiaryService.putRequestBenExecutor(request);
        assertEquals(request.getBeneficiaryId(), response.getBeneficiary().getBeneficiaryId());
        assertEquals(request.getBeneficiaryAccountNumber(),response.getBeneficiary().getBeneficiaryAccountNumber());
        assertEquals(request.getBeneficiaryEmail(), response.getBeneficiary().getBeneficiaryEmail());
        assertEquals(request.getBeneficiaryName(), response.getBeneficiary().getBeneficiaryName());
        assertEquals(request.getBloodRelation(), response.getBeneficiary().getRelation());
        assertEquals(request.getBen_date_of_birthInYYYYMMDD()
                , response.getBeneficiary().getBenDate_Of_Birth().toString());
        assertEquals(request.getBenBank(), response.getBeneficiary().getBenBank());
        assertEquals(request.getBenAdharNumber(), response.getBeneficiary().getBenAdharNumber());
        assertEquals(request.getBenDrivingLicense(), response.getBeneficiary().getBenDrivingLicense());
        assertEquals(request.getBenPanNumber(), response.getBeneficiary().getBenPanNumber());
        assertEquals(request.getBenPassportNumber(), response.getBeneficiary().getBenPassportNumber());
        assertEquals(request.getBenPhoneNumber(), response.getBeneficiary().getBenPhoneNumber());
        assertEquals(request.getBenVoterId(), response.getBeneficiary().getBenVoterId());
        assertEquals(request.getBenDrivingLicense(), response.getBeneficiary().getBenDrivingLicense());
        assertEquals(bankCode, response.getBeneficiary().getBankCode());
    }

    @Test
    public void updateBeneficiaryAccountFailedForInvalidIdTest() {
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        PutInputRequestDto request = PutInputRequestDto.builder()
                .accountNumber("1L")
                .beneficiaryId("69L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.UPDATE_BEN)
                .beneficiaryName("Updated Beneficiary Name")
                .beneficiaryAccountNumber("3L")
                .beneficiaryEmail("UpdatedEmail@gmail.com")
                .bloodRelation(DAUGHTER)
                .ben_date_of_birthInYYYYMMDD(String.valueOf(LocalDate.of(1997, 12, 02)))
                .benBank(AllConstantHelpers.BanksSupported.ICICI)
                .benAdharNumber("9876-5432-1111")
                .benPanNumber("GMDPD6969M")
                .benPassportNumber("U6696969")
                .benPhoneNumber("+91-1234567809")
                .benVoterId("ZSG2069697")
                .benDrivingLicense("HR-0619026969697")
                .build();

        assertThrows(BeneficiaryException.class,()->{
            beneficiaryService.putRequestBenExecutor(request);
        });
    }

    @Test
    public void deleteBeneficiariesForAnAccount() {
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        DeleteInputRequestDto request = DeleteInputRequestDto.builder()
                .accountNumber("1L")
                .beneficiaryId("1L").benRequest(AllConstantHelpers.BenUpdateRequest.DELETE_BEN)
                .build();

        beneficiaryService.deleteRequestBenExecutor(request);
        verify(beneficiaryRepositoryMock, times(1))
                .deleteByBeneficiaryId(anyString());
    }

    @Test
    public void deleteBeneficiariesForAnAccountFailedForInvalidBeneficiaryIdTest() {
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        DeleteInputRequestDto request = DeleteInputRequestDto.builder()
                .accountNumber("1L")
                .beneficiaryId(null).
                benRequest(AllConstantHelpers.BenUpdateRequest.DELETE_BEN)
                .build();

        assertThrows(BeneficiaryException.class,()->{
            beneficiaryService.deleteRequestBenExecutor(request);
        });
    }

    @Test
    public  void deleteAllBeneficiariesForAnAccount(){
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        DeleteInputRequestDto request= DeleteInputRequestDto.builder()
                .accountNumber("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.DELETE_ALL_BEN)
                .build();

        beneficiaryService.deleteRequestBenExecutor(request);
        verify(beneficiaryRepositoryMock,times(1))
                .deleteAllByAccounts_AccountNumber(anyString());
    }

    @Test
    public void invalidRequestForPostRequestFailedTest(){
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        PostInputRequestDto request= PostInputRequestDto.builder()
                .accountNumber("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.GET_ALL_BEN)
                .build();

        assertThrows(BeneficiaryException.class,()->{
            beneficiaryService.postRequestBenExecutor(request);
        });
    }

    @Test
    public void invalidRequestForGetRequestFailedTest(){
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        GetInputRequestDto request= GetInputRequestDto.builder()
                .accountNumber("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.ADD_BEN)
                .build();

        assertThrows(BeneficiaryException.class,()->{
            beneficiaryService.getRequestBenExecutor(request);
        });
    }

    @Test
    public void invalidRequestForPutRequestFailedTest(){
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        PutInputRequestDto request= PutInputRequestDto.builder()
                .accountNumber("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.DELETE_BEN)
                .build();

        assertThrows(BeneficiaryException.class,()->{
            beneficiaryService.putRequestBenExecutor(request);
        });
    }

    @Test
    public void invalidRequestForDeleteRequestFailedTest(){
        when(accountsRepositoryMock.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(accounts));

        DeleteInputRequestDto request= DeleteInputRequestDto.builder()
                .accountNumber("1L")
                .benRequest(AllConstantHelpers.BenUpdateRequest.ADD_BEN)
                .build();

        assertThrows(BeneficiaryException.class,()->{
            beneficiaryService.deleteRequestBenExecutor(request);
        });
    }
}
