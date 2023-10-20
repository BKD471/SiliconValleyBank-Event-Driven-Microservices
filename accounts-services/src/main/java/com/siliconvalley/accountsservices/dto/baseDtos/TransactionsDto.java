package com.siliconvalley.accountsservices.dto.baseDtos;


import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.validator.NotNullEnum;
import com.siliconvalley.accountsservices.validator.ValidDescription;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.siliconvalley.accountsservices.helpers.RegexMatchersHelper.PATTERN_FOR_NOT_NULL_CHARS;

public record TransactionsDto(@NotEmpty(message = "Please provide an account Number") String accountNumber,
                              LocalDateTime transactionTimeStamp,
                              String transactionId,
                              @Min(value =100,message = "transaction Amount should not be less than 100") BigDecimal transactionAmount,
                              @NotEmpty( message = "Please provide a transacted Account Number") String transactedAccountNumber,
                              AllConstantHelpers.TransactionType transactionType,
                              @NotNullEnum(regexp = PATTERN_FOR_NOT_NULL_CHARS,message = "Field can;t be null")
                              @ValidDescription
                              AllConstantHelpers.DescriptionType description,
                              AllConstantHelpers.FORMAT_TYPE downloadFormat){
    public static final class Builder{
        private String accountNumber;
        private LocalDateTime transactionTimeStamp;
        private String transactionId;
        private BigDecimal transactionAmount;
        private String transactedAccountNumber;
        private AllConstantHelpers.TransactionType transactionType;
        private AllConstantHelpers.DescriptionType description;
        private AllConstantHelpers.FORMAT_TYPE downloadFormat;

        public Builder(){}

        public Builder accountNumber(String accountNumber){
            this.accountNumber=accountNumber;
            return this;
        }

        public Builder transactionTimeStamp(LocalDateTime transactionTimeStamp){
            this.transactionTimeStamp=transactionTimeStamp;
            return this;
        }

        public Builder transactionId(String transactionId){
            this.transactionId=transactionId;
            return this;
        }
        public Builder transactionAmount(BigDecimal transactionAmount){
            this.transactionAmount=transactionAmount;
            return this;
        }

        public Builder transactedAccountNumber(String transactedAccountNumber){
            this.transactedAccountNumber=transactedAccountNumber;
            return this;
        }

        public Builder transactionType(AllConstantHelpers.TransactionType
                                               transactionType){
            this.transactionType=transactionType;
            return this;
        }


        public Builder description(AllConstantHelpers.DescriptionType description){
            this.description=description;
            return this;
        }

        public Builder downloadFormat(AllConstantHelpers.FORMAT_TYPE downloadFormat){
            this.downloadFormat=downloadFormat;
            return this;
        }

        public TransactionsDto build(){
            return new TransactionsDto(accountNumber,transactionTimeStamp,
                    transactionId,transactionAmount,transactedAccountNumber,transactionType,description,downloadFormat);
        }
    }
}


