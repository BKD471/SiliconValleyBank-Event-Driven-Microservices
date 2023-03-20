package com.example.bankdata.service;

import com.example.bankdata.dto.CustomerDto;
import com.example.bankdata.model.Customer;

public interface CustomerService {
    CustomerDto createCustmer(CustomerDto customerDto);
    CustomerDto getCustmerById(Long id);
}
