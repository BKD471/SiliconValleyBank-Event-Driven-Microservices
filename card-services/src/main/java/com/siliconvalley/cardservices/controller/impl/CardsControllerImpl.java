package com.siliconvalley.cardservices.controller.impl;

import com.siliconvalley.cardservices.services.ICardsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardsControllerImpl {
    private final ICardsService cardsService;

    CardsControllerImpl(ICardsService cardsService) {
        this.cardsService = cardsService;
    }
}
