package com.example.cardservices.services.Impl;

import com.example.cardservices.exception.BadApiRequestException;
import com.example.cardservices.exception.CardsException;
import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.helpers.AllEnumConstantHelpers;
import com.example.cardservices.model.Cards;
import com.example.cardservices.repository.ICardsRepository;
import com.example.cardservices.services.IValidationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("ValidationServicePrimary")
public class ValidationServiceImpl implements IValidationService {
   private final ICardsRepository cardsRepository;
   private final int MAX_PERMISSIBLE_CARDS=5;

   ValidationServiceImpl(ICardsRepository cardsRepository){
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
        Long customerId=cardsDto.getCustomerId();

        //doing all primary checks for obvious unhappy paths
        //if(null==cardsDto.getCardNumber()) throw new CardsException(CardsException.class,"Please specify non null cardNumber",methodName);
        if(null==customerId) throw new CardsException(CardsException.class,"Please specify non null customerId",methodName);

        switch (cardsValidationType){
            case ISSUE_CARD -> {
                Cards.CARD_TYPE cardType=cardsDto.getCardType();
                if(null==cardType) throw new CardsException(CardsException.class,"Please specify non null card type",methodName);

                Optional<List<Cards>> foundCardsList=cardsRepository.findAllByCustomerId(cardsDto.getCustomerId());
                if(foundCardsList.isPresent() && foundCardsList.get().size()>=MAX_PERMISSIBLE_CARDS) throw new CardsException(CardsException.class,"You can't have more than 5 cards",methodName);


                boolean anyMatch=foundCardsList.get().stream().anyMatch( card-> card.getCardType().equals(cardType));
                if(anyMatch) throw  new CardsException(CardsException.class,
                        "There is already one card with same type"+cardType+"linked to your account",methodName);

            }
            case GET_ALL_CARDS -> {
                boolean isCustomerExist=cardsRepository.findByCustomerIdExists(customerId);
                if(!isCustomerExist) throw new CardsException(BadApiRequestException.class
                        ,String.format("No such customers exists with this id %s",customerId),
                        methodName);
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
