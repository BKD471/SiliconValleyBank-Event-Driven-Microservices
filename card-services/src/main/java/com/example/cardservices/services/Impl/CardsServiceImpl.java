package com.example.cardservices.services.Impl;

import com.example.cardservices.CardsException;
import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.services.ICardsService;
import com.example.cardservices.services.IValidationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CardsServiceImpl implements ICardsService {
    private final IValidationService validationService;
    CardsServiceImpl(IValidationService validationService){
        this.validationService=validationService;
    }

    public  enum CardsValidationType{
        ISSUE_CARD,GENERATE_CREDIT_SCORE,REQUEST_FOR_REVISED_CREDIT_LIMIT,FLEXI_PAY
    }

    public CardsValidationType ISSUE_CARD=CardsValidationType.ISSUE_CARD;
    public CardsValidationType GENERATE_CREDIT_SCORE=CardsValidationType.GENERATE_CREDIT_SCORE;
    public CardsValidationType REQUEST_FOR_REVISED_CREDIT_LIMIT=CardsValidationType.REQUEST_FOR_REVISED_CREDIT_LIMIT;
    public CardsValidationType FLEXI_PAY=CardsValidationType.FLEXI_PAY;
    /**
     * @param cardsDto
     * @return
     */
    @Override
    public CardsDto issueCard(CardsDto cardsDto) {
        String methodName="issueCard(CardsDto) in CardsServiceImpl";
        validationService.cardsValidator(cardsDto,null,ISSUE_CARD);

        return null;
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