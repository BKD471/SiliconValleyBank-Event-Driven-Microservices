package com.siliconvalley.accountsservices.model;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


import java.math.BigDecimal;
import java.util.Set;
import java.util.LinkedHashSet;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Accounts extends Audit {
    @Id
    @Column(nullable = false, unique = true, name = "accnt_num")
    private String accountNumber;

    @Column(name = "cust_balnc")
    private BigDecimal balance;

    @Column(name = "accnt_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private AllConstantHelpers.AccountType accountType;

    @Column(name="branch_code")
    private String branchCode;

    @Column( name = "branch_addr",nullable = false)
    @Enumerated(EnumType.STRING)
    private AllConstantHelpers.Branch homeBranch;

    @Column(name = "trnsfr_lmt_pr_d")
    private BigDecimal transferLimitPerDay;

    @Column(name = "crdt_scr")
    private int creditScore;

    @Column(name="apprvd_loan_bso_crdt_scr")
    private BigDecimal approvedLoanLimitBasedOnCreditScore;

    @Column(name="is_ln_actv")
    private Boolean anyActiveLoans;

    @Column(name="tot_loan_issued_sf")
    private BigDecimal totLoanIssuedSoFar;

    @Column(name="tot_out_amnt")
    private BigDecimal totalOutStandingAmountPayableToBank;

    @Column(name = "rate_Of_Interest")
    private Double rateOfInterest;

    @Column(name="acc_stts")
    @Enumerated(EnumType.STRING)
    private AllConstantHelpers.AccountStatus accountStatus;

    @OneToMany(mappedBy = "accounts",cascade = CascadeType.ALL)
    private Set<Beneficiary> listOfBeneficiary=new LinkedHashSet<>();

    @OneToMany(mappedBy = "accounts",cascade = CascadeType.MERGE)
    private Set<Transactions> listOfTransactions=new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id",nullable = false)
    private Customer customer;
}
