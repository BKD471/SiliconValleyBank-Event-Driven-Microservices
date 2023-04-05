package com.example.accountsservices.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Accounts extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "account_num")
    private Long accountNumber;

    @Column(name = "cust_id",nullable = false)
    private Long customerId;

    @Column(name="dob",nullable = false)
    private LocalDate DateOfBirth;

    @Column(name="cust_age")
    private  int age;

    @Column(name = "cust_balance")
    private Long balance;

    @Column(name = "acnt_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    public  enum  AccountType{
        SAVINGS,CURRENT
    }
    @Column( name = "branch_addr",nullable = false)
    private String branchAddress;

    @Column(name = "phone_num", nullable = false)
    private String phoneNumber;

    @Column(name = "adhar_num", nullable = false)
    private String adharNumber;

    @Column(name = "pan_num", nullable = false)
    private String panNumber;

    @Column(name = "voter_id")
    private String voterId;

    @Column(name = "driving_license")
    private String drivingLicense;

    @Column(name = "passport")
    private String passportNumber;

    @Column(name="acc_status")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    public enum AccountStatus{
        OPEN,BLOCKED
    }

    @OneToMany(mappedBy = "accounts",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Beneficiary> listOfBeneficiary=new ArrayList<>();

    @OneToMany(mappedBy = "accounts",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Transactions> listOfTransactions=new ArrayList<>();
}
