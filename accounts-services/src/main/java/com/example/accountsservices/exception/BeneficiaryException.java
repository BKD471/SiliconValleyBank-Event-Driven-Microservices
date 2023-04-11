package com.example.accountsservices.exception;




public class BeneficiaryException extends Exception{

   private String reason;
   private String methodName;
   public BeneficiaryException(String reason,String methodName){
       super(String.format("BeneficiaryException has occurred in %s for %s",methodName,reason));
       this.reason=reason;
       this.methodName=methodName;
   }
}
