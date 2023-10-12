package com.siliconvalley.accountsservices.dto.baseDtos;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;



public record BankStatementRequestDto(String startDate, String endDate,
                                      String accountNumber,
                                      AllConstantHelpers.FORMAT_TYPE downloadFormat){

    public static final class Builder{
        private String startDate;
        private String endDate;
        private String accountNumber;
        private AllConstantHelpers.FORMAT_TYPE downloadFormat;

        public Builder(){
        }

        public Builder startDate(String startDate){
            this.startDate=startDate;
            return this;
        }

        public Builder endDate(String endDate){
            this.endDate=endDate;
            return this;
        }

        public Builder accountNumber(String accountNumber){
            this.accountNumber=accountNumber;
            return this;
        }

        public Builder downloadFormat(AllConstantHelpers.FORMAT_TYPE downloadFormat){
            this.downloadFormat=downloadFormat;
            return this;
        }

        public BankStatementRequestDto build(){
            return new BankStatementRequestDto(startDate,endDate,
                    accountNumber,downloadFormat);
        }
    }
}

