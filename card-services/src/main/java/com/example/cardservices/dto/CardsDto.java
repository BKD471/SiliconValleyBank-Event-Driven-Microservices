package com.example.cardservices.dto;

import com.example.cardservices.model.Cards;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardsDto {
    private String cardNumber;
    private long customerId;
    private Cards.CARD_TYPE cardType;
    private Cards.CARD_NETWORK cardNetwork;

    //last blld summary
    private double statementDue;
    private double minimumDue;
    private LocalDate dueDate;

    //last pmt dtls
    private double amountPaid;
    private LocalDate lastPaidDate;
    private double currentOutStanding;
    private double unBilledOutstanding;
    private double availableLimit;
    private long totalLimit;
    private int creditScore;
    private int rewardPoints;
}
