package com.example.cardservices.services.Impl;

import com.example.cardservices.exception.CardsException;
import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.mapper.CardsMapper;
import com.example.cardservices.model.Cards;
import com.example.cardservices.repository.ICardsRepository;
import com.example.cardservices.services.ICardsService;
import com.example.cardservices.services.IValidationService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import static com.example.cardservices.helpers.AllEnumConstantHelpers.*;
import static com.example.cardservices.mapper.CardsMapper.mapToCards;
import static com.example.cardservices.mapper.CardsMapper.mapToCardsDto;

@Service("acardsServicePrimary")
public class CardsServiceImpl implements ICardsService {
    private final IValidationService validationService;
    private final ICardsRepository cardsRepository;
    CardsServiceImpl(IValidationService validationService,ICardsRepository cardsRepository){
        this.validationService=validationService;
        this.cardsRepository=cardsRepository;
    }

    /**
     * @param cardsDto
     * @return
     */
    @Override
    public CardsDto issueCard(CardsDto cardsDto) {
        //validating the unhappy path
        validationService.cardsValidator(cardsDto,null,ISSUE_CARD);
        Cards card= mapToCards(cardsDto);
        Cards savedCards=cardsRepository.save(card);
        return mapToCardsDto(savedCards);
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public List<CardsDto> getAllCardsByCustomerId(Long customerId) {
        String methodName="getAllCardsByCustomerId(Long) in CardsServiceImpl";
        CardsDto cardsDto=CardsDto.builder().customerId(customerId).build();
        validationService.cardsValidator(cardsDto,null,GET_ALL_CARDS);

        Optional<List<Cards>> listOfCards=cardsRepository.findAllByCustomerId(customerId);
        if(listOfCards.isEmpty()) throw new CardsException(CardsException.class,
                "No cards are linked to this account",
                methodName);
        return listOfCards.get().stream().map(CardsMapper::mapToCardsDto).toList();
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public int generateCreditScore(Long customerId) {
        Optional<List<Cards>> listOfAllCards=cardsRepository.findAllByCustomerId(customerId);
        validationService.cardsValidator(null,null,GENERATE_CREDIT_SCORE);

        //to be done
        //fetch all pending loans, based in this generate credit score
        return 0;
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public boolean requestForRevisedCreditLimit(Long customerId) {
        return false;
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public CardsDto convertEmiToFlexiPay(Long customerId) {
        return null;
    }
}