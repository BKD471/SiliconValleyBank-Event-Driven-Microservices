package com.example.cardservices.services.Impl;

import com.example.cardservices.CardsException;
import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.helpers.AllEnumConstantHelpers;
import com.example.cardservices.model.Cards;
import com.example.cardservices.repository.CardsRepository;
import com.example.cardservices.services.IValidationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("ValidationServicePrimary")
public class ValidationServiceImpl implements IValidationService {
   private final CardsRepository cardsRepository;

   private final int MAX_PERMISSIBLE_CARDS=5;

   ValidationServiceImpl(CardsRepository cardsRepository){
       this.cardsRepository=cardsRepository;
   }

   //settle unhappy paths for cards
    /**
     * @param cardsDto
     * @param cards
     */
    @Override
    public void cardsValidator(CardsDto cardsDto, Cards cards, AllEnumConstantHelpers.CardsValidationType cardsValidationType) {
        String methodName="cardsValidator(CardsDto,Cards)";

        //doing all primary checks for obvious unhappy paths
        if(null==cardsDto.getCardType() || null==cardsDto.getCardNetwork())
            throw new CardsException(CardsException.class,"Please specify non null card type or card network",methodName);
        if(null==cardsDto.getCardNumber()) throw new CardsException(CardsException.class,"Please specify non null cardNumber",methodName);
        if(null==cardsDto.getCustomerId()) throw new CardsException(CardsException.class,"Please specify non null customerId",methodName);

        switch (cardsValidationType){
            case ISSUE_CARD -> {
                Optional<List<Cards>> foundCardsList=cardsRepository.findAllByCustomerId(cardsDto.getCustomerId());
                if(foundCardsList.isPresent() && foundCardsList.get().size()>=MAX_PERMISSIBLE_CARDS)
                    throw new CardsException(CardsException.class,"You can't have more than 5 cards",methodName);

                Cards.CARD_TYPE cardType=cardsDto.getCardType();
                boolean anyMatch=foundCardsList.get().stream().anyMatch( card-> card.getCardType().equals(cardType));
                if(anyMatch) throw  new CardsException(CardsException.class,
                        "There is already one card with same type"+cardType+"linked to your account",methodName);

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
