package com.siliconvalley.accountsservices.repository;

import com.siliconvalley.accountsservices.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionsRepository extends JpaRepository<Transactions,String> {
    void deleteByTransactionId(final String transactionsId);
}
