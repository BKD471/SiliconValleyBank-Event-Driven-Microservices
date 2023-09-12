package com.siliconvalley.loansservices.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Emi {
   @Id
   @Column(name = "emi_id",nullable = false,unique = true)
   private String emiId;

   @Column(name="ts")
   private LocalDateTime timeStamp;

   @Column(name = "e_amnt")
   private BigDecimal emiAmount;

   @Column(name = "os_amnt")
   private BigDecimal outStandingAMount;

   @Column(name = "amnt_paid")
   private BigDecimal amountPaid;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "loan_id",nullable = false)
   private Loans loans;
}
