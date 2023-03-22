package com.example.cardservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardsDto {
    private Long cardId;
    private  Long customerId;
    private String cardNumber;
    private  String cardType;
    private Long totalLimit;
    private Long amountUsed;
    private Long availableAmount;

}
