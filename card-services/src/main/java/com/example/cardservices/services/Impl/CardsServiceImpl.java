package com.example.cardservices.services.Impl;

import com.example.cardservices.CardsException;
import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.mapper.CardsMapper;
import com.example.cardservices.model.Cards;
import com.example.cardservices.repository.CardsRepository;
import com.example.cardservices.services.ICardsService;
import com.example.cardservices.services.IValidationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.example.cardservices.helpers.AllEnumConstantHelpers.ISSUE_CARD;
import static com.example.cardservices.mapper.CardsMapper.mapToCards;
import static com.example.cardservices.mapper.CardsMapper.mapToCardsDto;

@Service
public class CardsServiceImpl implements ICardsService {
    private final IValidationService validationService;
    private final CardsRepository cardsRepository;
    CardsServiceImpl(IValidationService validationService,CardsRepository cardsRepository){
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
        return null;
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public int generateCreditScore(Long customerId) {
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
    public CardsDto convertLoanToFlexiPay(Long customerId) {
        return null;
    }
}