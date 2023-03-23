package com.example.cardservices.mapper;

import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.model.Cards;

public class CardsMapper {

    /**
     * @param cardsDto
     * @paramType CardsDto
     * @ReturnType Cards
     */
    public static Cards mapToCards(CardsDto cardsDto){
        Cards cards=new Cards();
        cards.setCustomerId(cardsDto.getCustomerId());
        cards.setCardType(cardsDto.getCardType());
        cards.setCardNumber(cardsDto.getCardNumber());
        cards.setAmountUsed(cardsDto.getAmountUsed());
        cards.setAvailableAmount(cardsDto.getAvailableAmount());
        cards.setTotalLimit(cardsDto.getTotalLimit());
        return cards;
    }

    /**
     * @param cards
     * @paramType Cards
     * @ReturnType CardsDto
     */
    public static CardsDto mapToCardsDto(Cards cards){
        CardsDto cardsDto=new CardsDto();
        cardsDto.setCardId(cards.getCardId());
        cardsDto.setCustomerId(cards.getCustomerId());
        cardsDto.setCardType(cards.getCardType());
        cardsDto.setCardNumber(cards.getCardNumber());
        cardsDto.setAmountUsed(cards.getAmountUsed());
        cardsDto.setAvailableAmount(cards.getAvailableAmount());
        cardsDto.setTotalLimit(cards.getTotalLimit());
        return cardsDto;
    }
}
