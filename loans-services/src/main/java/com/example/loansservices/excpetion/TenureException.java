package com.example.loansservices.excpetion;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public class TenureException extends  Exception{
   public TenureException(String msg){
       super(msg);
   }
}
