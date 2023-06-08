package com.example.accountsservices.model;

import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Accounts extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "accnt_num")
    private Long accountNumber;


    @Column(name = "cust_balnc")
    private Long balance;

    @Column(name = "accnt_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    public  enum  AccountType{
        SAVINGS,CURRENT
    }


    @Column(name="branch_code")
    private String branchCode;

    @Column( name = "branch_addr",nullable = false)
    @Enumerated(EnumType.STRING)
    private Branch homeBranch;

    public enum Branch{
        KOLKATA,CHENNAI,BANGALORE,HYDERABAD,DELHI,BHUBANESWAR,MUMBAI,BARODA,PATNA,KERALA
    }

    @Column(name = "trnsfr_lmt_pr_d")
    private Long transferLimitPerDay;

    @Column(name = "crdt_scr")
    private int creditScore;

    @Column(name="apprvd_loan_bso_crdt_scr")
    private Long approvedLoanLimitBasedOnCreditScore;

    @Column(name="is_ln_actv")
    private Boolean anyActiveLoans;

    @Column(name="tot_loan_issued_sf")
    private Long totLoanIssuedSoFar;

    @Column(name="tot_out_amnt")
    private Long totalOutStandingAmountPayableToBank;

    @Column(name="acc_stts")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    public enum AccountStatus{
        OPEN,BLOCKED,CLOSED
    }


    @OneToMany(mappedBy = "accounts",cascade = CascadeType.ALL)
    private List<Beneficiary> listOfBeneficiary=new ArrayList<>();

    @OneToMany(mappedBy = "accounts",cascade = CascadeType.ALL)
    private List<Transactions> listOfTransactions=new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id",nullable = false)
    private Customer customer;

}
