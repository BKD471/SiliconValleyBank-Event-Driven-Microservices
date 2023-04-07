package com.example.accountsservices.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transactions extends AuditTransactions{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="trans_id")
    private Long transactionId;

    @Column(name="trans-amnt",nullable = false)
    private Long transactionAmount;

    @Column(name = "trans-acnt-num", nullable = false)
    private String transactedAccountNumber;

    @Column(name = "trans-type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public enum TransactionType {
        CREDIT, DEBIT
    }

    @Column(name = "desc_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private DescriptionType description;

    public enum DescriptionType{
           EMI,CREDIT_CARD_BILL,DONATION,RENT,SALARY,E_SHOPPING,BUSINESS,INVESTMENT,FAMILY,ELECTRICITY,OTHERS
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Accounts accounts;
}
