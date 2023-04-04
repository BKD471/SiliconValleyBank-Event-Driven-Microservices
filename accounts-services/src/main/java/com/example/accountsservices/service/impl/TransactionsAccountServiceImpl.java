package com.example.accountsservices.service.impl;


import com.example.accountsservices.dto.TransactionsDto;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.TransactionsRepository;
import com.example.accountsservices.service.AbstractAccountsService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionsAccountServiceImpl extends AbstractAccountsService {

    private final AccountsRepository accountsRepository;
    private final TransactionsRepository transactionsRepository;

    TransactionsAccountServiceImpl(TransactionsRepository transactionsRepository,AccountsRepository accountsRepository){
        this.transactionsRepository=transactionsRepository;
        this.accountsRepository=accountsRepository;
    }

    /**
     * @param customerId
     * @param accountNumberRecipient
     * @param accountNumberSender
     * @returnType AccountsDto
     */
    @Override
    public TransactionsDto creditMoney(Long customerId, Long accountNumberRecipient, Long accountNumberSender) {
        return null;
    }

    @Override
    public TransactionsDto debitMoney(Long customerId, Long accountNumberSource,
                                  Long accountNumberDestination) {
        return null;
    }

    @Override
    public List<TransactionsDto> getPastSixMonthsTransactionsForAnAccount(Long accountNumber) {
        return null;
    }

    @Override
    public void deleteTransactionDetails(Long transactionId){
        //
    }
}
