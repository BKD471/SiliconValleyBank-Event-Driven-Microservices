package com.example.cardservices.helpers;

import com.example.cardservices.exception.TenureException;

import java.util.HashMap;
import java.util.Map;

public class RateOfInterestHelper {
    private static final Map<Integer,Double> RateOfInterestHash=new HashMap<>();

    static {
        RateOfInterestHash.put(5,5.5d);
        RateOfInterestHash.put(10,6.5d);
        RateOfInterestHash.put(15,8.2d);
        RateOfInterestHash.put(20,9.5d);
        RateOfInterestHash.put(35,12.5d);
        RateOfInterestHash.put(40,15.5d);
    }
    public  static  Double getRateOfInterest(int tenure) throws TenureException {
        String methodName="getRateOfInterest() in RateOfInterestHelper";
        if(RateOfInterestHash.containsKey(tenure)) return RateOfInterestHash.get(tenure);
        throw new TenureException(TenureException.class,"No tenure of %s is available"+tenure,methodName);
    }
}
