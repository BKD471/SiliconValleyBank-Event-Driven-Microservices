package com.example.bankdata.mapper;

import com.example.bankdata.dto.CustomerDto;
import com.example.bankdata.model.Customer;
import jakarta.persistence.Column;

public class CustomerMapper {

    /**
     * @param customerDto
     * @paramType CustomerDto
     * @ReturnType Customer
     */
    public static Customer mapToCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setMobileNumber(customerDto.getMobileNumber());
        customer.setAdharNumber(customerDto.getAdharNumber());
        customer.setPanNumber(customerDto.getPanNumber());
        customer.setVoterId(customerDto.getVoterId());
        customer.setDrivingLicense(customerDto.getDrivingLicense());
        customer.setPassportNumber(customerDto.getPassportNumber());
        return customer;
    }

    /**
     * @param customer
     * @paramType Customer
     * @ReturnType CustomerDto
     */
    public static CustomerDto mapToCustomerDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(customer.getCustomerId());
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setMobileNumber(customer.getMobileNumber());
        customerDto.setAdharNumber(customer.getAdharNumber());
        customerDto.setPanNumber(customer.getPanNumber());
        customerDto.setVoterId(customer.getVoterId());
        customerDto.setDrivingLicense(customer.getDrivingLicense());
        customerDto.setPassportNumber(customer.getPassportNumber());
        return customerDto;
    }
}
