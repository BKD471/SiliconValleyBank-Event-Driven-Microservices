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
        super(accountsRepository);
        this.transactionsRepository=transactionsRepository;
        this.accountsRepository=accountsRepository;
    }

    /**
     * @param accountNumber
     * @param accountNumberSender
     * @param amount
     * @returnType AccountsDto
     */
    @Override
    public TransactionsDto depositMoney(Long accountNumber,  Long accountNumberSender,Long amount) {
        return null;
    }

    @Override
    public TransactionsDto transferMoneyToOtherAccounts(Long accountNumber,Long accountNumberDestination,Long amount) {
        return null;
    }

    @Override
    public List<TransactionsDto> getPastSixMonthsTransactionsForAnAccount(Long accountNumber) {
        return null;
    }

    @Override
    public  TransactionsDto payBills(Long accountNumber,TransactionsDto transactionsDto){
        return null;
    }

}
