package com.siliconvalley.cardservices.services.Impl;

import com.siliconvalley.cardservices.exception.BadApiRequestException;
import com.siliconvalley.cardservices.exception.CardsException;
import com.siliconvalley.cardservices.dto.CardsDto;
import com.siliconvalley.cardservices.helpers.AllConstantHelpers;
import com.siliconvalley.cardservices.model.Cards;
import com.siliconvalley.cardservices.repository.ICardsRepository;
import com.siliconvalley.cardservices.services.IValidationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

//settle unhappy paths for cards
@Service("ValidationServicePrimary")
public class ValidationServiceImpl implements IValidationService {
    private final ICardsRepository cardsRepository;
    ValidationServiceImpl(final ICardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
    }

    /**
     * @param cardsDto
     * @param cards
     */
    @Override
    public void cardsValidator(final CardsDto cardsDto, final Cards cards, final AllConstantHelpers.CardsValidationType cardsValidationType) {
        final String methodName = "cardsValidator(CardsDto,Cards)";
        final String customerId = cardsDto.getCustomerId();

        //doing all primary checks for obvious unhappy paths
        if (StringUtils.isEmpty(customerId))
            throw new CardsException(CardsException.class, "Please specify non null customerId", methodName);

        final int MAX_TIME_LIMIT_FOR_ANY_NEW_CREDIT_ACTIVITY_IN_MONTHS = 6;
        final StringBuffer location=new StringBuffer(500);
        switch (cardsValidationType) {
            case ISSUE_CARD -> {
                location.append("INSIDE ISSUE CARD");
                location.trimToSize();

                final Cards.CARD_TYPE cardType = cardsDto.getCardType();
                if (Objects.isNull(cardType))
                    throw new CardsException(CardsException.class,
                            "Please specify non null card type",
                            String.format("%s of %s",location,methodName));

                final Optional<List<Cards>> foundCardsList = cardsRepository.findAllByCustomerId(cardsDto.getCustomerId());
                final int MAX_PERMISSIBLE_CARDS = 5;
                if (foundCardsList.isPresent() && foundCardsList.get().size() >= MAX_PERMISSIBLE_CARDS)
                    throw new CardsException(CardsException.class,
                            "You can't have more than 5 cards",
                            String.format("%s of %s",location,methodName));


                final boolean anyMatch = foundCardsList.get().stream().anyMatch(card -> card.getCardType().equals(cardType));
                if (anyMatch) throw new CardsException(CardsException.class,
                        "There is already one card with same type" + cardType + "linked to your account",
                        String.format("%s of %s",location,methodName));

            }
            case GET_ALL_CARDS -> {
                location.append("INSIDE GET_ALL_CARDS");
                location.trimToSize();
                final boolean isCustomerExist = cardsRepository.findByCustomerIdExists(customerId);
                if (!isCustomerExist) throw new CardsException(BadApiRequestException.class
                        , String.format("No such customers exists with this id %s", customerId),
                        String.format("%s of %s",location,methodName));
            }
            case GENERATE_CREDIT_SCORE -> {
                location.append("INSIDE GENERATE_CREDIT_SCORE");
                location.trimToSize();
                final Optional<List<Cards>> foundCardsList = cardsRepository.findAllByCustomerId(customerId);
                if (foundCardsList.isEmpty()) throw new CardsException(CardsException.class
                        , "No cards available for this account,so can't generate credit score",
                        String.format("%s of %s",location,methodName));

                foundCardsList.get()
                        .sort((o1, o2) -> (o1.getIssuedDate().isBefore(o2.getIssuedDate())) ? -1
                                : (o1.getIssuedDate().isAfter(o2.getIssuedDate())) ? 1 : 0);

                final LocalDateTime now = LocalDateTime.now();
                final LocalDateTime oldestCardIssuedDate = foundCardsList.get().get(0).getIssuedDate();

                final double SECONDS_TO_MONTHS_CONVERTER_FRACTION = ((double) 1 / (86400 * 30 * 6));
                final double monthsOld = (Duration.between(oldestCardIssuedDate, now).getSeconds()) * SECONDS_TO_MONTHS_CONVERTER_FRACTION;

                if (monthsOld < MAX_TIME_LIMIT_FOR_ANY_NEW_CREDIT_ACTIVITY_IN_MONTHS)
                    throw new CardsException(CardsException.class,
                            "You oldest credit account should be at least six months old",
                            String.format("%s of %s",location,methodName));
            }
            case REQUEST_FOR_REVISED_CREDIT_LIMIT -> {
                location.append("INSIDE REQUEST_FOR_REVISED_CREDIT_LIMIT");
                location.trimToSize();

                final LocalDate lastRevisedCreditLtDate = cards.getLastRefreshedCreditLimit();
                final int months = Period.between(lastRevisedCreditLtDate, LocalDate.now()).getYears();
                final LocalDate nextCreditLimitRevisedDate = lastRevisedCreditLtDate.plusMonths(MAX_TIME_LIMIT_FOR_ANY_NEW_CREDIT_ACTIVITY_IN_MONTHS);
                if (months < MAX_TIME_LIMIT_FOR_ANY_NEW_CREDIT_ACTIVITY_IN_MONTHS)
                    throw new CardsException(CardsException.class,
                            String.format("You can't request for credit limit before %s", nextCreditLimitRevisedDate),
                            String.format("%s of %s",location,methodName));
            }
            case FLEXI_PAY -> {
                location.append("FLEXI_PAY");
                location.trimToSize();
                final BigDecimal lastTransaction = cards.getLastTransactionAmount();
                final BigDecimal creditLimit = cards.getSanctionedCreditLimit();

                final BigDecimal MIN_PERCENTAGE_FRACTION_TO_QUALIFY_FOR_FLEXI_PAY = BigDecimal.valueOf(0.02d);
                final BigDecimal threshHold =new BigDecimal(String.valueOf(creditLimit)).multiply(MIN_PERCENTAGE_FRACTION_TO_QUALIFY_FOR_FLEXI_PAY);
                if (lastTransaction.compareTo(threshHold)<0) throw new CardsException(CardsException.class,
                        String.format("Your transaction at least need to be minimum %s", threshHold),
                        String.format("%s of %s",location,methodName));
            }
            default -> throw new CardsException(CardsException.class, "Wrong request for validation", methodName);
        }
    }
}
