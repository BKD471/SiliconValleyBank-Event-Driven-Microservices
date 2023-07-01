package com.example.cardservices.mapper;


import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.model.Cards;

public class CardsMapper {

   public static Cards mapToCards(CardsDto cardsDto){
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
               .statementDue(cardsDto.getStatementDue())
               .lastPaidDate(cardsDto.getLastPaidDate())
               .totalLimit(cardsDto.getTotalLimit())
               .rewardPoints(cardsDto.getRewardPoints())
               .amountPaid(cardsDto.getAmountPaid())
               .unBilledOutstanding(cardsDto.getUnBilledOutstanding())
               .build();
   }

    public static CardsDto mapToCardsDto(Cards cards){
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
                .totalLimit(cards.getTotalLimit())
                .rewardPoints(cards.getRewardPoints())
                .amountPaid(cards.getAmountPaid())
                .unBilledOutstanding(cards.getUnBilledOutstanding())
                .build();
    }
}
