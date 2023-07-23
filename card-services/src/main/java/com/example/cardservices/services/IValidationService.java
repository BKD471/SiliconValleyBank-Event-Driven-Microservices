package com.example.cardservices.services;

import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.helpers.AllConstantHelpers;
import com.example.cardservices.model.Cards;

public interface IValidationService {
    void cardsValidator(final CardsDto cardsDto,final Cards cards,final AllConstantHelpers.CardsValidationType cardsValidationType);
}
