package com.example.cardservices.services;

import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.helpers.AllEnumConstantHelpers;
import com.example.cardservices.model.Cards;

public interface IValidationService {
    void cardsValidator(CardsDto cardsDto, Cards cards, AllEnumConstantHelpers.CardsValidationType cardsValidationType);
}
