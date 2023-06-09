package com.example.accountsservices.repository;

import com.example.accountsservices.model.Accounts;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface AccountsRepository extends JpaRepository<Accounts, Long> {
    Optional<Accounts> findByAccountNumber(Long accountNumber);

    Optional<Accounts> findByCustomer_Email(String email);

    void deleteAllByCustomer_CustomerId(Long customerId);

    void deleteByAccountNumber(Long accountNumber);

    Optional<Page<Accounts>> findAllByCustomer_CustomerId(Long customerId, Pageable pageable);




//    @Modifying ///for update
//    @Query(value = "SELECT a from Accounts a WHERE a.id= :id AND a.name= :name",nativeQuery = false)//for jpql nativeQuery is false
//    Accounts getAccountByName(@Param("id") Long Acc_id, String name);

}
