package com.siliconvalley.accountsservices.dto.baseDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class BankStatementRequestDto {
    private String startDate;
    private String endDate;
    private String accountNumber;
}
