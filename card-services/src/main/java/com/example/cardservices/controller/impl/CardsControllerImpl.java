package com.example.cardservices.controller.impl;

import com.example.cardservices.services.ICardsService;
import com.example.cardservices.services.Impl.CardsServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cards")
public class CardsControllerImpl {
    private final ICardsService cardsService;

    CardsControllerImpl(ICardsService cardsService) {
        this.cardsService = cardsService;
    }
}
