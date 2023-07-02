package com.example.cardservices.helpers;

import com.example.cardservices.model.Cards;

import java.util.Comparator;

public class SortCardsByTime implements Comparator<Cards> {
    /**
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return
     */
    @Override
    public int compare(Cards o1, Cards o2) {
        if(o1.getIssuedDate().isBefore(o2.getIssuedDate())) return -1;
        else if(o1.getIssuedDate().isAfter(o2.getIssuedDate())) return 1;
        return 0;
    }
}
