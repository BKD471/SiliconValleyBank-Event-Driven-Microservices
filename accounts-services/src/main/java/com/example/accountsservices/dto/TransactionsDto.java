package com.example.accountsservices.dto;


import com.example.accountsservices.model.Transactions;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionsDto extends AbstractParentDto {
    @NotNull(message = "Please provide an account Number")
    private Long accountNumber;
    private LocalDateTime transactionTimeStamp;
    private Long transactionId;

    @Min(value =100,message = "transaction Amount should not be less than 100")
    private Long transactionAmount;
    @NotBlank(message = "Please provide a transacted Account Number")
    private String transactedAccountNumber;
    private Transactions.TransactionType transactionType;
    @NotNull(message = "Please give the description")
    private Transactions.DescriptionType description;
}
