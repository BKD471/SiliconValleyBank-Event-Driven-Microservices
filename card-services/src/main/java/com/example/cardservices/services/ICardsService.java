package com.example.cardservices.services;

import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.model.Cards;

import java.util.*;

public interface ICardsService {
    CardsDto issueCard(CardsDto cardsDto);
    List<CardsDto> getAllCardsByCustomerId(String customerId);
    int generateCreditScore(String customerId);
    CardsDto requestForRevisedCreditLimitForACard(String cardNumber);
    CardsDto convertToFlexiPay(String cardNumber);
}
