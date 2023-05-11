package com.example.accountsservices.helpers;

import com.example.accountsservices.model.Transactions;

import java.util.Comparator;

public class SortDateComparator implements Comparator<Transactions> {
    /**
     * @param a the first object to be compared.
     * @param b the second object to be compared.
     * @return
     */
    @Override
    public int compare(Transactions a, Transactions b) {
        if(a.getTransactionTimeStamp().isBefore(b.getTransactionTimeStamp())) return -1;
        else if(a.getTransactionTimeStamp().isAfter(b.getTransactionTimeStamp())) return 1;
        return  0;
    }
}
