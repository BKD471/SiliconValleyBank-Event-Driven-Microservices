package com.siliconvalley.loansservices.dto;

import com.siliconvalley.loansservices.helpers.AllConstantsHelper;
import com.siliconvalley.loansservices.model.Loans;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutPutDto implements Dto, Serializable {
    @Serial
    private static final long serialVersionUID=7894567891234567841L;
    private String defaultMessage;
    private LoansDto loansDto;
    private EmiDto emiDto;
    private Set<LoansDto> listOfLoans;
}
