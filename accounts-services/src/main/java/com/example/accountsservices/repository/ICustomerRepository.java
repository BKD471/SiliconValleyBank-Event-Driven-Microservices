package com.example.accountsservices.repository;

import com.example.accountsservices.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer,String>{
   Optional<Customer> findByEmail(final String email);
}
