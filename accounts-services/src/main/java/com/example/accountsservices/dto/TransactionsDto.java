package com.example.accountsservices.dto;

import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Transactions;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionsDto extends AbstractParentDto {
    private LocalDateTime transactionTimeStamp;
    private Long transactionId;
    private Long transactionAmount;
    private String transactedAccountNumber;
    private Transactions.TransactionType transactionType;
    private Transactions.DescriptionType description;
    private Accounts accounts;
}
