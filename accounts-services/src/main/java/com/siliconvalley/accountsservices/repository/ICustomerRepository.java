package com.siliconvalley.accountsservices.repository;

import com.siliconvalley.accountsservices.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer,String>{
   Optional<Customer> findByEmail(final String email);
   Optional<Customer> findCustomerByEmail(String email);
}
