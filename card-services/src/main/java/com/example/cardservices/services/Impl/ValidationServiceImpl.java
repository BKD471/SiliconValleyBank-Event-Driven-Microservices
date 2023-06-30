package com.example.cardservices.services.Impl;

import com.example.cardservices.CardsException;
import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.model.Cards;
import com.example.cardservices.services.IValidationService;
import org.springframework.stereotype.Service;

@Service("ValidationServicePrimary")
public class ValidationServiceImpl implements IValidationService {

   //settle unhappy paths for cards
    /**
     * @param cardsDto
     * @param cards
     */
    @Override
    public void cardsValidator(CardsDto cardsDto, Cards cards, CardsServiceImpl.CardsValidationType cardsValidationType) {
        String methodName="cardsValidator(CardsDto,Cards)";

        //doing all primary checks for obvious unhappy paths
        if(null==cardsDto.getCardType() || null==cardsDto.getCardNetwork())
            throw new CardsException(CardsException.class,"Please specify non null card type or card network",methodName);
        if(null==cardsDto.getCardNumber()) throw new CardsException(CardsException.class,"Please specify non null cardNumber",methodName);
        if(null==cardsDto.getCustomerId()) throw new CardsException(CardsException.class,"Please specify non null customerId",methodName);

        switch (cardsValidationType){
            case ISSUE_CARD -> {

            }
            case GENERATE_CREDIT_SCORE -> {

            }
            case REQUEST_FOR_REVISED_CREDIT_LIMIT -> {

            }
            case FLEXI_PAY -> {

            }
            default -> throw  new CardsException(CardsException.class,"Wrong request for validation",methodName);
        }
    }
}
