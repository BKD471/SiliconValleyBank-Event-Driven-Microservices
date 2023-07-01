package com.example.cardservices.dto;

import com.example.cardservices.model.Cards;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardsDto {
    private String cardNumber;
    private Long customerId;
    private Cards.CARD_TYPE cardType;
    private Cards.CARD_NETWORK cardNetwork;

    //last billed summary
    private double statementDue;
    private double minimumDue;
    private LocalDate dueDate;

    //last payment details
    private double amountPaid;
    private LocalDate lastPaidDate;
    private double currentOutStanding;
    private double unBilledOutstanding;
    private double availableLimit;
    private long totalLimit;
    private int creditScore;
    private int rewardPoints;
}
