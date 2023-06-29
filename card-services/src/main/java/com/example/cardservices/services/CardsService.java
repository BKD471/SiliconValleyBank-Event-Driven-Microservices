package com.example.cardservices.services;

import com.example.cardservices.dto.CardsDto;

import java.util.*;

public interface CardsService {
    CardsDto issueCard(CardsDto cardsDto);
    List<CardsDto> getAllCardsByCustomerId(Long customerId);
    int generateCreditScore(Long customerId);
    boolean requestForRevisedCreditLimit(Long customerId);
    CardsDto convertLoanToFlexiPay(Long customerId);
}
