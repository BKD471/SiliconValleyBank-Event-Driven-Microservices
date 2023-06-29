package com.example.accountsservices.dto;


import com.example.accountsservices.model.Transactions;
import com.example.accountsservices.validator.NotNullEnum;
import com.example.accountsservices.validator.ValidDescription;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.time.LocalDateTime;

import static com.example.accountsservices.helpers.RegexMatchersHelper.PATTERN_FOR_NOT_NULL_CHARS;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionsDto {
    @Min(value =1, message = "Please provide an account Number")
    private Long accountNumber;
    private LocalDateTime transactionTimeStamp;
    private Long transactionId;

    @Min(value =100,message = "transaction Amount should not be less than 100")
    private Long transactionAmount;
    @Min(value =1, message = "Please provide a transacted Account Number")
    private String transactedAccountNumber;
    private Transactions.TransactionType transactionType;
    @NotNullEnum(regexp = PATTERN_FOR_NOT_NULL_CHARS,message = "Field can;t be null")
    @ValidDescription
    private Transactions.DescriptionType description;
}
