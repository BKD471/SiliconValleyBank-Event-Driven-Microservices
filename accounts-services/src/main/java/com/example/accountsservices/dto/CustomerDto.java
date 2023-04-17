package com.example.accountsservices.dto;

import com.example.accountsservices.model.Accounts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto extends BaseDto{
    private Long customerId;
    private String customerName;
    private List<Accounts> accounts;
 }
