package com.example.cardservices.services.Impl;

import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.mapper.CardsMapper;
import com.example.cardservices.model.Cards;
import com.example.cardservices.repository.CardsRepository;
import com.example.cardservices.services.CardsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardsServiceImpl implements CardsService {

    private CardsRepository cardsRepository;

    CardsServiceImpl(CardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
    }

    /**
    *Method to set expiry date of 5 years from date of issue &
    */
    private Cards processCardInfo(Cards cards) {
        LocalDateTime date = cards.getCreatedDate();
        LocalDateTime expiredDate = date.plusYears(5);
        cards.setExpiredDate(expiredDate);
        Long remainingBalance=cards.getTotalLimit()-cards.getAmountUsed();
        cards.setAvailableAmount(remainingBalance);
        return cards;
    }

    /**
     * @param cardsDto
     * @paramType CardsDto
     * @ReturnType CardsDto
     */
    @Override
    public CardsDto createCards(CardsDto cardsDto) {
        Cards cards = CardsMapper.mapToCards(cardsDto);
        Cards savedCard = cardsRepository.save(cards);

        //Set expiry date of 5 years to card fom date of issue & calculate remaining balance
        Cards finalSavedCard = processCardInfo(savedCard);
        Cards finalCard = cardsRepository.save(finalSavedCard);
        return CardsMapper.mapToCardsDto(finalCard);
    }

    /**
     * @param customerId
     * @paramType Long
     * @ReturnType List<CardsDto>
     */
    @Override
    public List<CardsDto> getAllCardsByCustomerId(Long customerId) {
        List<Cards> savedCardsList =cardsRepository.findByCustomerId(customerId);
        List<CardsDto> savedCardDtoList = savedCardsList.stream().map(card -> CardsMapper.mapToCardsDto(card)).collect(Collectors.toList());
        return savedCardDtoList;
    }
}