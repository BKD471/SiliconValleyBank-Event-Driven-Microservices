package com.siliconvalley.loansservices.dto;

import com.siliconvalley.loansservices.helpers.AllConstantsHelper;
import com.siliconvalley.loansservices.model.Loans;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutPutDto implements Dto{
    private LoansDto loansDto;
    private List<LoansDto> listOfLoans;
}
