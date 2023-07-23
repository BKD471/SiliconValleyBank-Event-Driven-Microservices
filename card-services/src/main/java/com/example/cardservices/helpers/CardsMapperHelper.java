package com.example.cardservices.helpers;

import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.model.Cards;

public class CardsMapperHelper {

   public static Cards mapToCards(final CardsDto cardsDto){
       return Cards.builder()
               .customerId(cardsDto.getCustomerId())
               .cardNumber(cardsDto.getCardNumber())
               .cardNetwork(cardsDto.getCardNetwork())
               .cardType(cardsDto.getCardType())
               .availableLimit(cardsDto.getAvailableLimit())
               .creditScore(cardsDto.getCreditScore())
               .dueDate(cardsDto.getDueDate())
               .currentOutStanding(cardsDto.getCurrentOutStanding())
               .minimumDue(cardsDto.getMinimumDue())
               .lastTransactionAmount(cardsDto.getLastTransactionAmount())
               .statementDue(cardsDto.getStatementDue())
               .lastPaidDate(cardsDto.getLastPaidDate())
               .lastRefreshedCreditLimit(cardsDto.getLastRefreshedCreditLimit())
               .netIncomePA(cardsDto.getNetIncomePA())
               .billGenerationDate(cardsDto.getBillGenerationDate())
               .sanctionedCreditLimit(cardsDto.getSanctionedCreditLimit())
               .rewardPoints(cardsDto.getRewardPoints())
               .amountPaid(cardsDto.getAmountPaid())
               .unBilledOutstanding(cardsDto.getUnBilledOutstanding())
               .build();
   }

    public static CardsDto mapToCardsDto(final Cards cards){
        return CardsDto.builder()
                .customerId(cards.getCustomerId())
                .cardNumber(cards.getCardNumber())
                .cardNetwork(cards.getCardNetwork())
                .cardType(cards.getCardType())
                .availableLimit(cards.getAvailableLimit())
                .creditScore(cards.getCreditScore())
                .dueDate(cards.getDueDate())
                .currentOutStanding(cards.getCurrentOutStanding())
                .minimumDue(cards.getMinimumDue())
                .statementDue(cards.getStatementDue())
                .lastPaidDate(cards.getLastPaidDate())
                .lastTransactionAmount(cards.getLastTransactionAmount())
                .lastRefreshedCreditLimit(cards.getLastRefreshedCreditLimit())
                .sanctionedCreditLimit(cards.getSanctionedCreditLimit())
                .netIncomePA(cards.getNetIncomePA())
                .billGenerationDate(cards.getBillGenerationDate())
                .rewardPoints(cards.getRewardPoints())
                .amountPaid(cards.getAmountPaid())
                .unBilledOutstanding(cards.getUnBilledOutstanding())
                .build();
    }
}
