package com.example.accountsservices.dto.baseDtos;


import com.example.accountsservices.helpers.AllConstantHelpers;
import com.example.accountsservices.validator.NotNullEnum;
import com.example.accountsservices.validator.ValidDescription;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.time.LocalDateTime;

import static com.example.accountsservices.helpers.RegexMatchersHelper.PATTERN_FOR_NOT_NULL_CHARS;


@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class TransactionsDto {
    @Min(value =1, message = "Please provide an account Number")
    private final String accountNumber;
    private final LocalDateTime transactionTimeStamp;
    private final String transactionId;

    @Min(value =100,message = "transaction Amount should not be less than 100")
    private final Long transactionAmount;
    @Min(value =1, message = "Please provide a transacted Account Number")
    private final String transactedAccountNumber;
    private final AllConstantHelpers.TransactionType transactionType;
    @NotNullEnum(regexp = PATTERN_FOR_NOT_NULL_CHARS,message = "Field can;t be null")
    @ValidDescription
    private final AllConstantHelpers.DescriptionType description;
}
