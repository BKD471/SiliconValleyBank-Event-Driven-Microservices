package com.example.cardservices.controller;

import com.example.cardservices.dto.CardsDto;
import com.example.cardservices.services.Impl.CardsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
public class CardsController {
    private CardsServiceImpl cardsService;

    CardsController(CardsServiceImpl cardsService) {
        this.cardsService = cardsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<CardsDto>> getCardListByCustomerId
            (@PathVariable(name = "id") Long customerId) {
        List<CardsDto> listOfCards = cardsService.getAllCardsByCustomerId(customerId);
        return new ResponseEntity<>(listOfCards, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CardsDto> createCards(@RequestBody CardsDto cardsDto) {
        CardsDto createdCard = cardsService.createCards(cardsDto);
        return new ResponseEntity<>(createdCard, HttpStatus.CREATED);
    }

}
