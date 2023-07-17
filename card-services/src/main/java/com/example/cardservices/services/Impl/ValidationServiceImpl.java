package com.example.cardservices.services.Impl;

import com.example.cardservices.exception.BadApiRequestException;
import com.example.cardservices.exception.CardsException;
import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.helpers.AllConstantHelpers;
import com.example.cardservices.model.Cards;
import com.example.cardservices.repository.ICardsRepository;
import com.example.cardservices.services.IValidationService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service("ValidationServicePrimary")
public class ValidationServiceImpl implements IValidationService {
    private final ICardsRepository cardsRepository;

    ValidationServiceImpl(ICardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
    }

    //settle unhappy paths for cards

    /**
     * @param cardsDto
     * @param cards
     */
    @Override
    public void cardsValidator(CardsDto cardsDto, Cards cards, AllConstantHelpers.CardsValidationType cardsValidationType) {
        String methodName = "cardsValidator(CardsDto,Cards)";
        String customerId = cardsDto.getCustomerId();

        //doing all primary checks for obvious unhappy paths
        //if(null==cardsDto.getCardNumber()) throw new CardsException(CardsException.class,"Please specify non null cardNumber",methodName);
        if (null == customerId)
            throw new CardsException(CardsException.class, "Please specify non null customerId", methodName);

        int MAX_TIME_LIMIT_FOR_ANY_NEW_CREDIT_ACTIVITY_IN_MONTHS = 6;
        switch (cardsValidationType) {
            case ISSUE_CARD -> {
                Cards.CARD_TYPE cardType = cardsDto.getCardType();
                if (null == cardType)
                    throw new CardsException(CardsException.class, "Please specify non null card type", methodName);

                Optional<List<Cards>> foundCardsList = cardsRepository.findAllByCustomerId(cardsDto.getCustomerId());
                int MAX_PERMISSIBLE_CARDS = 5;
                if (foundCardsList.isPresent() && foundCardsList.get().size() >= MAX_PERMISSIBLE_CARDS)
                    throw new CardsException(CardsException.class, "You can't have more than 5 cards", methodName);


                boolean anyMatch = foundCardsList.get().stream().anyMatch(card -> card.getCardType().equals(cardType));
                if (anyMatch) throw new CardsException(CardsException.class,
                        "There is already one card with same type" + cardType + "linked to your account", methodName);

            }
            case GET_ALL_CARDS -> {
                boolean isCustomerExist = cardsRepository.findByCustomerIdExists(customerId);
                if (!isCustomerExist) throw new CardsException(BadApiRequestException.class
                        , String.format("No such customers exists with this id %s", customerId),
                        methodName);
            }
            case GENERATE_CREDIT_SCORE -> {
                Optional<List<Cards>> foundCardsList = cardsRepository.findAllByCustomerId(customerId);
                if (foundCardsList.isEmpty()) throw new CardsException(CardsException.class
                        , "No cards available for this account,so can't generate credit score", methodName);

                foundCardsList.get()
                        .sort((o1, o2) -> (o1.getIssuedDate().isBefore(o2.getIssuedDate())) ? -1
                                : (o1.getIssuedDate().isAfter(o2.getIssuedDate())) ? 1 : 0);

                LocalDateTime now = LocalDateTime.now();
                LocalDateTime oldestCardIssuedDate = foundCardsList.get().get(0).getIssuedDate();

                double SECONDS_TO_MONTHS_CONVERTER_FRACTION = ((double) 1 / (86400 * 30 * 6));
                double monthsOld = (Duration.between(oldestCardIssuedDate, now).getSeconds()) * SECONDS_TO_MONTHS_CONVERTER_FRACTION;

                if (monthsOld < MAX_TIME_LIMIT_FOR_ANY_NEW_CREDIT_ACTIVITY_IN_MONTHS)
                    throw new CardsException(CardsException.class,
                            "You oldest credit account should be at least six months old", methodName);
            }
            case REQUEST_FOR_REVISED_CREDIT_LIMIT -> {
                LocalDate lastRevisedCreditLtDate = cards.getLastRefreshedCreditLimit();
                int months = Period.between(lastRevisedCreditLtDate, LocalDate.now()).getYears();
                LocalDate nextCreditLimitRevisedDate = lastRevisedCreditLtDate.plusMonths(MAX_TIME_LIMIT_FOR_ANY_NEW_CREDIT_ACTIVITY_IN_MONTHS);
                if (months < MAX_TIME_LIMIT_FOR_ANY_NEW_CREDIT_ACTIVITY_IN_MONTHS)
                    throw new CardsException(CardsException.class, String.format("You can't request for credit limit before %s", nextCreditLimitRevisedDate), methodName);
            }
            case FLEXI_PAY -> {
                double lastTransaction = cards.getLastTransactionAmount();
                double creditLimit = cards.getSanctionedCreditLimit();

                double MIN_PERCENTAGE_FRACTION_TO_QUALIFY_FOR_FLEXI_PAY = 0.02d;
                double threshHold = creditLimit * MIN_PERCENTAGE_FRACTION_TO_QUALIFY_FOR_FLEXI_PAY;
                if (lastTransaction < threshHold) throw new CardsException(CardsException.class,
                        String.format("Your transaction at least need to be minimum %s", threshHold), methodName);
            }
            default -> throw new CardsException(CardsException.class, "Wrong request for validation", methodName);
        }
    }
}
