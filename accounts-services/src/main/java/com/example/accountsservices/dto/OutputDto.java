package com.example.accountsservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutputDto {
    private CustomerOutPutDto customer;
    private AccountsOutPutDto accounts;
}
