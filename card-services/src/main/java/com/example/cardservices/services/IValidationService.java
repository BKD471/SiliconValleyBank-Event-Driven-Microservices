package com.example.cardservices.services;

import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.helpers.AllConstantHelpers;
import com.example.cardservices.model.Cards;

public interface IValidationService {
    void cardsValidator(CardsDto cardsDto, Cards cards, AllConstantHelpers.CardsValidationType cardsValidationType);
}
