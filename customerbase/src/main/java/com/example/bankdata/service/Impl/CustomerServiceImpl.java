package com.example.bankdata.service.Impl;

import com.example.bankdata.dto.CustomerDto;
import com.example.bankdata.mapper.CustomerMapper;
import com.example.bankdata.model.Customer;
import com.example.bankdata.repository.CustomerRepository;
import com.example.bankdata.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;

    CustomerServiceImpl(CustomerRepository customerRepository){
        this.customerRepository=customerRepository;
    }

    /**
     * @param customerDto
     * @paramType CustomerDto
     * @ReturnType CustomerDto
     */
    @Override
    public CustomerDto createCustmer(CustomerDto customerDto) {
        Customer customer= CustomerMapper.mapToCustomer(customerDto);
        Customer savedCustomer=customerRepository.save(customer);
        return CustomerMapper.mapToCustomerDto(savedCustomer);
    }

    /**
     * @param id
     * @paramType Long
     * @ReturnType CustomerDto
     */
    @Override
    public CustomerDto getCustmerById(Long id) {
        Optional<Customer> fetchedCustomer=customerRepository.findById(id);
        return CustomerMapper.mapToCustomerDto(fetchedCustomer.get());
    }
}
