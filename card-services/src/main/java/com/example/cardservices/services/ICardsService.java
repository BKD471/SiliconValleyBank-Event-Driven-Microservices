package com.example.cardservices.services;

import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.model.Cards;

import java.util.*;

public interface ICardsService {
    CardsDto issueCard(CardsDto cardsDto);
    List<CardsDto> getAllCardsByCustomerId(Long customerId);
    int generateCreditScore(Long customerId);
    Cards requestForRevisedCreditLimitForACard(String cardNumber);
    CardsDto convertEmiToFlexiPay(Long customerId);
}
