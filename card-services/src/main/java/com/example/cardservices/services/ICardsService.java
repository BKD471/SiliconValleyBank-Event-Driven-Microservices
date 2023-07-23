package com.example.cardservices.services;

import com.example.cardservices.dto.CardsDto;

import java.time.LocalDate;
import java.util.*;

public interface ICardsService {
    CardsDto issueCard(final CardsDto cardsDto);
    List<CardsDto> getAllCardsByCustomerId(final String customerId);
    CardsDto payBills(final CardsDto cardsDto);
    List<CardsDto> getPastTransactions(final LocalDate startDate,final LocalDate endDate);
    int generateCreditScore(final String customerId);
    CardsDto requestForRevisedCreditLimitForACard(final String cardNumber);
    CardsDto convertToFlexiPay(final String cardNumber);
}
