package com.example.accountsservices.model;

import com.example.accountsservices.exception.CustomerException;
import com.example.accountsservices.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String methodName = "loadUserByUsername(String) in CustomerUserDetailsService";
        Optional<Customer> foundCustomer = customerRepository.findByEmail(username);
        if (foundCustomer.isEmpty())
            throw new CustomerException(CustomerException.class, String.format("No such customer with username:%s", username), methodName);
        return foundCustomer.get();
    }
}
