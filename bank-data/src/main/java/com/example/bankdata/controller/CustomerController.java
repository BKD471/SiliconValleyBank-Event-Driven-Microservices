package com.example.bankdata.controller;

import com.example.bankdata.dto.CustomerDto;
import com.example.bankdata.service.Impl.CustomerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private CustomerServiceImpl customerService;
    CustomerController(CustomerServiceImpl customerService){
        this.customerService=customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto){
        CustomerDto createdCustomer=customerService.createCustmer(customerDto);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable(name="id") Long customerId){
        CustomerDto foundCustomer=customerService.getCustmerById(customerId);
        return new ResponseEntity<>(foundCustomer,HttpStatus.OK);
    }
}
