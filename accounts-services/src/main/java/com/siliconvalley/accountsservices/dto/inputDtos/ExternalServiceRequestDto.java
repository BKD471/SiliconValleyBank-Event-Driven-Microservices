package com.siliconvalley.accountsservices.dto.inputDtos;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.loansservices.helpers.AllConstantsHelper;

import java.math.BigDecimal;

public record ExternalServiceRequestDto(String customerId,String accountNumber,AllConstantsHelper.LoanType loanType, AllConstantsHelper.RequestType requestType,
                                        BigDecimal totalLoan, Integer loanTenureInYears,
                                        AllConstantHelpers.UpdateRequest updateRequest) {

    public static final class Builder{
        private String customerId;
        private String accountNumber;
        private AllConstantsHelper.LoanType loanType;
        private AllConstantsHelper.RequestType requestType;
        private BigDecimal totalLoan;
        private Integer loanTenureInYears;

        private AllConstantHelpers.UpdateRequest updateRequest;
        public Builder(){}

        public Builder customerId(String customerId){
            this.customerId=customerId;
            return this;
        }

        public Builder accountNumber(String accountNumber){
            this.accountNumber=accountNumber;
            return this;
        }


        public Builder loanType(AllConstantsHelper.LoanType loanType){
            this.loanType=loanType;
            return this;
        }

        public Builder requestType(AllConstantsHelper.RequestType requestType){
            this.requestType=requestType;
            return this;
        }

        public Builder totalLoan(BigDecimal totalLoan){
            this.totalLoan=totalLoan;
            return this;
        }

        public Builder loanTenureInYears(Integer loanTenureInYears){
            this.loanTenureInYears=loanTenureInYears;
            return this;
        }

        public Builder updateRequest(AllConstantHelpers.UpdateRequest updateRequest){
            this.updateRequest=updateRequest;
            return this;
        }

        public ExternalServiceRequestDto build(){
            return new ExternalServiceRequestDto(customerId,accountNumber,loanType,requestType,
                    totalLoan,loanTenureInYears,updateRequest);
        }
    }
}
