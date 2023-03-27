package com.example.loansservices.utils;

import com.example.loansservices.exception.TenureException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class RateOfInterestHelper {

    private static Map<Integer,Double> getHash(){
         Map<Integer,Double> Cash_Karo_hash_karo=new HashMap<>();
        Cash_Karo_hash_karo.put(5,10.5D);
        Cash_Karo_hash_karo.put(10,15.2D);
        Cash_Karo_hash_karo.put(15,20.3D);
        Cash_Karo_hash_karo.put(20,32D);
        Cash_Karo_hash_karo.put(35,37D);
        Cash_Karo_hash_karo.put(40,45D);
        return Cash_Karo_hash_karo;
    }
    public  static  Double getRateOfInterest(int tenure) throws TenureException {
        String methodName="getRateOfInterest() in RateOfInterestHelper";
        if(getHash().containsKey(tenure)) return getHash().get(tenure);
        throw new TenureException(String.format("Tenure %s is not available",tenure),methodName);
    }
}
