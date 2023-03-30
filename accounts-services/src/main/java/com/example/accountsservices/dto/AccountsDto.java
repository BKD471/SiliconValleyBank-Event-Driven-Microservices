package com.example.accountsservices.dto;


import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountsDto {
    private Long accountNumber;
    private Long customerId;

    private Date DateOfBirth;
    private  int customerAge;
    private Accounts.AccountType accountType;
    private String branchAddress;
    private String phoneNumber;
    private String adharNumber;
    private String panNumber;
    private String voterId;
    private String drivingLicense;
    private String passportNumber;
    private List<Beneficiary> beneficiary;
}
