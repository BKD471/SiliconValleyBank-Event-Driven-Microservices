package com.siliconvalley.cardservices.services;

import com.siliconvalley.cardservices.dto.CardsDto;
import com.siliconvalley.cardservices.helpers.AllConstantHelpers;
import com.siliconvalley.cardservices.model.Cards;

public interface IValidationService {
    void cardsValidator(final CardsDto cardsDto, final Cards cards, final AllConstantHelpers.CardsValidationType cardsValidationType);
}
