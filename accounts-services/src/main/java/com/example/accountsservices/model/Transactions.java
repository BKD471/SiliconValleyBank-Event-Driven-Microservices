package com.example.accountsservices.model;

import com.example.accountsservices.helpers.AllEnumConstantHelpers;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transactions extends AuditTransactions{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="trans_id")
    private long transactionId;

    @Column(name="trans-amnt",nullable = false)
    private long transactionAmount;

    @Column(name = "trans-acnt-num", nullable = false)
    private String transactedAccountNumber;

    @Column(name = "trans-type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AllEnumConstantHelpers.TransactionType transactionType;

    @Column(name = "desc_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private AllEnumConstantHelpers.DescriptionType description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Accounts accounts;
}
