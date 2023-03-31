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
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tran_id")
    private Long transactionId;

    private LocalTime timeStamp;
    private LocalDate date;

    private Long amount;

    @Column(name = "transacted-acnt", nullable = false)
    private String transactedAccount;

    @Column(name = "transact-type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public enum TransactionType {
        CREDIT, DEBIT
    }

    @Column(name = "desc_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private DescriptionType description;

    public enum DescriptionType{
           EMI,DONATION,RENT,EBILLPAYMENT,BUSINESS,INVESTMENT,FAMILY,OTHERS
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Accounts accounts;
}
