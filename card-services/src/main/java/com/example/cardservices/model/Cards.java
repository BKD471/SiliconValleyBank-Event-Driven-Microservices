package com.example.cardservices.model;

import jakarta.persistence.*;
import lombok.*;

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
    private long netIncomePA;

    //last blld summary
    @Column(name = "stmt_due")
    private double statementDue;
    @Column(name = "mn_due")
    private double minimumDue;
    @Column(name = "bill-gen_date")
    private LocalDate billGenerationDate;
    @Column(name = "due_dt")
    private LocalDate dueDate;

    //last transaction
    @Column(name = "last_trn_amnt")
    private double lastTransactionAmount;

    //last pmt dtls
    @Column(name = "amnt_paid")
    private double amountPaid;

    @Column(name = "lst_pd_dt")
    private LocalDate lastPaidDate;

    @Column(name = "curr_out_standing")
    private double currentOutStanding;

    @Column(name = "un_bld_out_standing")
    private double unBilledOutstanding;

    @Column(name = "avail_lmt")
    private double availableLimit;

    @Column(name = "total_limit")
    private double sanctionedCreditLimit;

    @Column(name="last_cred_limit_rfereshed")
    private LocalDate lastRefreshedCreditLimit;

    @Column(name = "crdt_score")
    private int creditScore;

    @Column(name = "reward_pts")
    private int rewardPoints;
}
