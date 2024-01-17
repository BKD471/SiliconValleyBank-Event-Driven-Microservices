package com.siliconvalley.accountsservices.repository;

import com.siliconvalley.accountsservices.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICustomerRepository extends CrudRepository<Customer,String> {
   Optional<Customer> findByEmail(final String email);
   Optional<Customer> findCustomerByEmail(final String email);

   Optional<Page<Customer>> findAll(Pageable pageable);
}
