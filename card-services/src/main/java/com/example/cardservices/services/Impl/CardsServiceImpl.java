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

    /**
     * @param cardsDto
     * @return
     */
    @Override
    public CardsDto createCards(CardsDto cardsDto) {
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
}