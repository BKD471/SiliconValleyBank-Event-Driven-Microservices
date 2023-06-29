package com.example.cardservices.services.Impl;

import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.services.ICardsService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CardsServiceImpl implements ICardsService {

    /**
     * @param cardsDto
     * @return
     */
    @Override
    public CardsDto issueCard(CardsDto cardsDto) {
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