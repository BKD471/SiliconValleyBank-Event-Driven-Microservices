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


    @Column(name = "cust_balance")
    private Long balance;

    @Column(name = "acnt_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    public  enum  AccountType{
        SAVINGS,CURRENT
    }


    @Column(name="branch_code")
    private  Long branchCode;

    @Column( name = "branch_addr",nullable = false)
    private Branch homeBranch;

    public enum Branch{
        KOLKATA,CHENNAI,BANGALORE,HYDERABAD,DELHI,BHUBANESWAR,MUMBAI,BARODA,PATNA,KERALA
    }

    @Column(name = "cash_limit")
    private Long cashLimitPerDay;

    @Column(name = "credit_score")
    private int creditScore;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id",nullable = false)
    private Customer customer;
}
