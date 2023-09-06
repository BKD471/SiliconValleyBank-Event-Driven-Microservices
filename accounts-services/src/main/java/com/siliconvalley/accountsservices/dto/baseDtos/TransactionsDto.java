package com.siliconvalley.accountsservices.dto.baseDtos;


import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.validator.NotNullEnum;
import com.siliconvalley.accountsservices.validator.ValidDescription;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.siliconvalley.accountsservices.helpers.RegexMatchersHelper.PATTERN_FOR_NOT_NULL_CHARS;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionsDto implements Serializable {
    @Serial
    private static final long serialVersionUID=1234567891234567896L;
    @NotEmpty(message = "Please provide an account Number")
    private String accountNumber;
    private LocalDateTime transactionTimeStamp;
    private String transactionId;

    @Min(value =100,message = "transaction Amount should not be less than 100")
    private BigDecimal transactionAmount;
    @NotEmpty( message = "Please provide a transacted Account Number")
    private String transactedAccountNumber;
    private AllConstantHelpers.TransactionType transactionType;
    @NotNullEnum(regexp = PATTERN_FOR_NOT_NULL_CHARS,message = "Field can;t be null")
    @ValidDescription
    private AllConstantHelpers.DescriptionType description;
}
