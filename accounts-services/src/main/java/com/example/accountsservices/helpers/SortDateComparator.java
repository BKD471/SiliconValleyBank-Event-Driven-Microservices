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
        return (a.getTransactionTimeStamp().isBefore(b.getTransactionTimeStamp()))? -1:
                (a.getTransactionTimeStamp().isAfter(b.getTransactionTimeStamp()))? 1:0;
    }
}
