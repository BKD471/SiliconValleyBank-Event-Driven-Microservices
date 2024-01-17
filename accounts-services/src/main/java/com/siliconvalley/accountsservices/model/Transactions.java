package com.siliconvalley.accountsservices.model;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transactions extends AuditTransactions{
    @Id
    @Column(name="trans_id")
    private String transactionId;

    @Column(name="trans-amnt",nullable = false)
    private BigDecimal transactionAmount;

    @Column(name = "trans-acnt-num", nullable = false)
    private String transactedAccountNumber;

    @Column(name = "trans-type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AllConstantHelpers.TransactionType transactionType;

    @Column(name = "desc_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private AllConstantHelpers.DescriptionType description;

    @Column(name="balance")
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Accounts accounts;
}
