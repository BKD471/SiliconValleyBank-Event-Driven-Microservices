package com.siliconvalley.loansservices.model;

import com.siliconvalley.loansservices.helpers.AllConstantsHelper;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Loans extends Audit{
    @Id
    @Column(name = "loan_num",unique = true,nullable = false)
    private String loanNumber;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name="end_dt")
    private LocalDate endDt;

    @Column(name = "loan_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private AllConstantsHelper.LoanType loanType;


    @Column(name = "total_loan")
    private BigDecimal totalLoan;

    @Column(name = "loan_tenure")
    private Integer loanTenureInYears;

    @Column(name = "amount_paid")
    private BigDecimal amountPaid;

    @Column(name="rate_of_interest")
    private Double Rate_Of_Interest;

    @Column(name = "emi_amnt")
    private BigDecimal emiAmount;

    @Column(name = "tot_inst")
    private Integer totalInstallmentsInNumber;
    @Column(name = "inst_patd")
    private Integer installmentsPaidInNumber;

    @Column(name = "inst_rem")
    private Integer installmentsRemainingInNumber;

    @Column(name = "outstanding_amount")
    private BigDecimal outstandingAmount;

    @Column(name="is_loan_active")
    private Boolean isLoanActive;

    @Column(name = "last_payment_date")
    private LocalDateTime lastPayMentBeingMade;

    @OneToMany(mappedBy = "loans",cascade = CascadeType.ALL)
    private Set<Emi> setOfEmis=new LinkedHashSet<>();
}
