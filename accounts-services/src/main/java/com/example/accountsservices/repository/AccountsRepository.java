package com.example.accountsservices.repository;

import com.example.accountsservices.model.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts,Long> {
    Optional<Accounts> findByAccountNumber(Long accountNumber);
    Optional<Accounts> findByCustomer_Email(String email);
//    List<Accounts> findAllByCustomerId(Long customerId);
    //void deleteAllByCustomerId(Long customerId);
    void deleteByAccountNumber(Long accountNumber);

    Optional<List<Accounts>> findAllByCustomer_CustomerId(Long customerId);


//    @Modifying ///for update
//    @Query(value = "SELECT a from Accounts a WHERE a.id= :id AND a.name= :name",nativeQuery = false)//for jpql nativeQuery is false
//    Accounts getAccountByName(@Param("id") Long Acc_id, String name);

}
