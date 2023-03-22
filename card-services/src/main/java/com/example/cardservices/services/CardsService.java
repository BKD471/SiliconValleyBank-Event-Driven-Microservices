package com.example.cardservices.services;

import com.example.cardservices.dto.CardsDto;
import java.util.*;
public interface CardsService {
    CardsDto createCards(CardsDto cardsDto);
    List<CardsDto> getAllCardsByCustomerId(Long customerId);
}
