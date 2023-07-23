package com.example.cardservices.dto;

import com.example.cardservices.model.Cards;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardsDto {
    private String cardNumber;
    private String customerId;
    private Cards.CARD_TYPE cardType;
    private Cards.CARD_NETWORK cardNetwork;

    //last transaction amount
    private BigDecimal lastTransactionAmount;

    //last billed summary
    private BigDecimal statementDue;
    private BigDecimal minimumDue;
    private LocalDate dueDate;

    //last payment details
    private BigDecimal amountPaid;
    private LocalDate lastPaidDate;
    private BigDecimal currentOutStanding;
    private BigDecimal unBilledOutstanding;
    private BigDecimal availableLimit;
    private BigDecimal sanctionedCreditLimit;
    private LocalDate lastRefreshedCreditLimit;
    private int creditScore;
    private int rewardPoints;
    private BigDecimal netIncomePA;
    private LocalDate billGenerationDate;
}
