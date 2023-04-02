package com.example.accountsservices.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryException extends Exception{

   public BeneficiaryException(String msg){
       super(msg);
   }
}
