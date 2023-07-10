package com.example.cardservices.services;

import com.example.cardservices.dto.CardsDto;

import java.time.LocalDate;
import java.util.*;

public interface ICardsService {
    CardsDto issueCard(CardsDto cardsDto);
    List<CardsDto> getAllCardsByCustomerId(String customerId);
    CardsDto payBills(CardsDto cardsDto);
    List<CardsDto> getPastTransactions(LocalDate startDate,LocalDate endDate);
    int generateCreditScore(String customerId);
    CardsDto requestForRevisedCreditLimitForACard(String cardNumber);
    CardsDto convertToFlexiPay(String cardNumber);
}
