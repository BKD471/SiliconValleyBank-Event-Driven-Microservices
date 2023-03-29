package com.example.loansservices.utils;

import com.example.loansservices.exception.TenureException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class RateOfInterestHelper {

    private static Map<Integer,Double> getHash(){
         Map<Integer,Double> Cash_Karo_hash_karo=new HashMap<>();
        Cash_Karo_hash_karo.put(5,5.5d);
        Cash_Karo_hash_karo.put(10,6.5d);
        Cash_Karo_hash_karo.put(15,8.2d);
        Cash_Karo_hash_karo.put(20,9.5d);
        Cash_Karo_hash_karo.put(35,12.5d);
        Cash_Karo_hash_karo.put(40,15.5d);
        return Cash_Karo_hash_karo;
    }
    public  static  Double getRateOfInterest(int tenure) throws TenureException {
        String methodName="getRateOfInterest() in RateOfInterestHelper";
        if(getHash().containsKey(tenure)) return getHash().get(tenure);
        throw new TenureException(String.format("Tenure %s is not available",tenure),methodName);
    }
}
