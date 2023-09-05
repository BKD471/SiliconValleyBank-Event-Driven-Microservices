package com.siliconvalley.accountsservices.dto.baseDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class BankStatementRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID=1234567891234567892L;
    private String startDate;
    private String endDate;
    private String accountNumber;
    private FORMAT_TYPE downloadFormat;
    public enum FORMAT_TYPE{
        HTML,PDF,XML
    }

}
