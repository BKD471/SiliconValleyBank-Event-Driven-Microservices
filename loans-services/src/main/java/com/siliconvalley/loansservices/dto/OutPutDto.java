package com.siliconvalley.loansservices.dto;

import com.siliconvalley.loansservices.helpers.AllConstantsHelper;
import com.siliconvalley.loansservices.model.Loans;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutPutDto implements Dto{
    private LoansDto loansDto;
    private Set<LoansDto> listOfLoans;
}
