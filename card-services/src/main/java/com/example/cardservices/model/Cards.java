package com.example.cardservices.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Cards extends  Audit{
    @Id
    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "card_type")
    @Enumerated(EnumType.STRING)
    private CARD_TYPE cardType;

    public enum CARD_TYPE{
        TRAVEL,FUEL,REWARD,SECURED,BUSINESS,STUDENT,PREMIUM,
        CASHBACK,LIFESTYLE,ENTERTAINMENT,SHOPPING
    }

    @Column(name = "cardPaymentSystem")
    @Enumerated(EnumType.STRING)
    private CARD_NETWORK cardNetwork;

    public enum CARD_NETWORK{
        VISA,MASTERCARD,RU_PAY
    }

    //net income
    @Column(name = "net_income_pa")
    private BigDecimal netIncomePA;

    //last blld summary
    @Column(name = "stmt_due")
    private BigDecimal statementDue;
    @Column(name = "mn_due")
    private BigDecimal minimumDue;
    @Column(name = "bill-gen_date")
    private LocalDate billGenerationDate;
    @Column(name = "due_dt")
    private LocalDate dueDate;

    //last transaction
    @Column(name = "last_trn_amnt")
    private BigDecimal lastTransactionAmount;

    //last pmt dtls
    @Column(name = "amnt_paid")
    private BigDecimal amountPaid;

    @Column(name = "lst_pd_dt")
    private LocalDate lastPaidDate;

    @Column(name = "curr_out_standing")
    private BigDecimal currentOutStanding;

    @Column(name = "un_bld_out_standing")
    private BigDecimal unBilledOutstanding;

    @Column(name = "avail_lmt")
    private BigDecimal availableLimit;

    @Column(name = "total_limit")
    private BigDecimal sanctionedCreditLimit;

    @Column(name="last_cred_limit_rfereshed")
    private BigDecimal lastRefreshedCreditLimit;

    @Column(name = "crdt_score")
    private int creditScore;

    @Column(name = "reward_pts")
    private int rewardPoints;
}
