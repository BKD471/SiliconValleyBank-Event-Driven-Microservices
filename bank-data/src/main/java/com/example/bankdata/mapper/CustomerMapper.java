package com.example.bankdata.mapper;

import com.example.bankdata.dto.CustomerDto;
import com.example.bankdata.model.Customer;
import org.springframework.context.annotation.Bean;

public class CustomerMapper {

    public static Customer mapToCustomer(CustomerDto customerDto){
        Customer customer=new Customer();
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setMobileNumber(customerDto.getMobileNumber());
        return customer;
    }

    public static CustomerDto mapToCustomerDto(Customer customer){
        CustomerDto customerDto=new CustomerDto();
        customerDto.setCustomerId(customer.getCustomerId());
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setMobileNumber(customer.getMobileNumber());
        return customerDto;
    }
}
