package com.example.accountsservices.service.impl;


import com.example.accountsservices.dto.TransactionsDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.TransactionException;
import com.example.accountsservices.mapper.Mapper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Transactions;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.TransactionsRepository;
import com.example.accountsservices.service.AbstractAccountsService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionsAccountServiceImpl extends AbstractAccountsService {
    private static final Transactions.TransactionType CREDIT= Transactions.TransactionType.CREDIT;
    private  static final Transactions.TransactionType DEBIT= Transactions.TransactionType.DEBIT;

    private final TransactionsRepository transactionsRepository;
    private  final  AccountsRepository accountsRepository;

    TransactionsAccountServiceImpl(TransactionsRepository transactionsRepository,AccountsRepository accountsRepository){
        super(accountsRepository);
        this.transactionsRepository=transactionsRepository;
        this.accountsRepository=accountsRepository;
    }

    private Transactions updateBalance(Accounts accounts, Transactions transactions,Long amount, Transactions.TransactionType transactionType) throws  TransactionException{
        Long previousBalance=accounts.getBalance();

        if(CREDIT.equals(transactionType)) {
            accounts.setBalance(previousBalance+amount);
            transactions.setTransactionType(CREDIT);
        }
        if(DEBIT.equals(transactionType)){
            if(previousBalance>=amount ) accounts.setBalance(previousBalance-amount);
            else throw new TransactionException(".....");
            transactions.setTransactionType(DEBIT);
        }

        //update the latest balance to accounts db
        accountsRepository.save(accounts);
        return  transactions;
    }
    /**
     * @param transactionsDto
     * @returnType AccountsDto
     */

    private TransactionsDto depositMoney(TransactionsDto transactionsDto) throws AccountsException, TransactionException {
        //fetch account
        Long accountNumber=transactionsDto.getAccounts().getAccountNumber();
        Accounts fetchedAccount=fetchAccountByAccountNumber(accountNumber);

        //converting to entity object
        Transactions requestTransaction= Mapper.mapToTransactions(transactionsDto);

        //get the money & update the balance
        Long amountTobeDeposited=requestTransaction.getTransactionAmount();
        Transactions recentTransaction=updateBalance(fetchedAccount,requestTransaction,amountTobeDeposited,CREDIT);

        //save in DB & return
        Transactions savedTransaction=transactionsRepository.save(recentTransaction);
        return Mapper.mapToTransactionsDto(savedTransaction);
    }


    private TransactionsDto transferMoneyToOtherAccounts(TransactionsDto transactionsDto) throws AccountsException, TransactionException {
        //fetch account
        Long accountNumber=transactionsDto.getAccounts().getAccountNumber();
        Accounts fetchedAccount=fetchAccountByAccountNumber(accountNumber);

        //converting to entity object
        Transactions requestTransaction= Mapper.mapToTransactions(transactionsDto);

        //get the money & update the balance
        Long amountTobeDeposited=requestTransaction.getTransactionAmount();
        Transactions recentTransaction=updateBalance(fetchedAccount,requestTransaction,amountTobeDeposited,DEBIT);

        //save in DB & return
        Transactions savedTransactions=transactionsRepository.save(recentTransaction);
        return  Mapper.mapToTransactionsDto(savedTransactions);
    }

    public  TransactionsDto transactionsExecutor(TransactionsDto transactionsDto) throws TransactionException, AccountsException {
        switch (transactionsDto.getTransactionType()){
            case CREDIT -> {
                return depositMoney(transactionsDto);
            }
            case DEBIT -> {
                return transferMoneyToOtherAccounts(transactionsDto);
            }
            default -> {
                throw  new TransactionException("");
            }
        }
    }
    @Override
    public List<TransactionsDto> getPastSixMonthsTransactionsForAnAccount(Long accountNumber) {
        return null;
    }

    @Override
    public  TransactionsDto payBills(TransactionsDto transactionsDto){
        return null;
    }

}
