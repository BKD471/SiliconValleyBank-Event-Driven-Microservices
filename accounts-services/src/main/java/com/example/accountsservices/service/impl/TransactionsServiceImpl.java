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
import com.example.accountsservices.util.SortDateComparator;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionsServiceImpl extends AbstractAccountsService {
    private final Transactions.TransactionType CREDIT = Transactions.TransactionType.CREDIT;
    private final Transactions.TransactionType DEBIT = Transactions.TransactionType.DEBIT;
    private final TransactionsRepository transactionsRepository;
    private final AccountsRepository accountsRepository;

    TransactionsServiceImpl(TransactionsRepository transactionsRepository, AccountsRepository accountsRepository) {
        super(accountsRepository);
        this.transactionsRepository = transactionsRepository;
        this.accountsRepository = accountsRepository;
    }

    private Transactions updateBalance(Accounts accounts, Transactions transactions, Long amount, Transactions.TransactionType transactionType) throws TransactionException {
        String methodName="updateBalance(Accounts , Transactions , Long , Transactions.TransactionType ) in TransactionsServiceImpl";


        Long previousBalance = accounts.getBalance();

        if (CREDIT.equals(transactionType)) {
            accounts.setBalance(previousBalance + amount);
            transactions.setTransactionType(CREDIT);
        }
        if (DEBIT.equals(transactionType)) {
            if (previousBalance >= amount) accounts.setBalance(previousBalance - amount);
            else throw new TransactionException("Insufficient Balance",methodName);
            transactions.setTransactionType(DEBIT);
        }

        //update the latest balance to accounts db
        accountsRepository.save(accounts);
        return transactions;
    }

    /**
     * @param transactionsDto
     * @returnType AccountsDto
     */
    private TransactionsDto payOrDepositMoney(TransactionsDto transactionsDto, Transactions.TransactionType transactionType) throws AccountsException, TransactionException {
        //fetch account
        Long accountNumber = transactionsDto.getAccounts().getAccountNumber();
        Accounts fetchedAccount = fetchAccountByAccountNumber(accountNumber);

        //converting to entity object
        Transactions requestTransaction = Mapper.mapToTransactions(transactionsDto);

        //get the money & update the balance
        Long amountTobeDeposited = requestTransaction.getTransactionAmount();
        Transactions recentTransaction = updateBalance(fetchedAccount, requestTransaction, amountTobeDeposited, transactionType);

        //save in DB & return
        Transactions savedTransactions = transactionsRepository.save(recentTransaction);
        return Mapper.mapToTransactionsDto(savedTransactions);
    }

    //using switch expression to decide debit or credit type transactions
    //among all transactions of descriptions like
    // EMI,CREDIT_CARD_BILL,DONATION,RENT,E_SHOPPING,
    // BUSINESS,INVESTMENT,FAMILY,ELECTRICITY,OTHERS
    //are all debit type transactions
    //only SALARY is credit type, we only add money ,so it's simple need not to add too much complexity
    //so just call payOrDeposit method to process the transaction
    @Override
    public TransactionsDto transactionsExecutor(TransactionsDto transactionsDto) throws TransactionException, AccountsException {
        String methodName="transactionsExecutor(TransactionsDto) in TransactionsServiceImpl";

        switch (transactionsDto.getTransactionType()) {
            case CREDIT -> {
                return payOrDepositMoney(transactionsDto, CREDIT);
            }
            case DEBIT -> {
                return payBills(transactionsDto);
            }
            default -> throw new TransactionException("Please Specify a valid transaction type",methodName);
        }
    }

    @Override
    public List<TransactionsDto> getPastSixMonthsTransactionsForAnAccount(Long accountNumber) throws  AccountsException{
        //fetch account
        Accounts fetchedAccount=fetchAccountByAccountNumber(accountNumber);

        //Calculate the  date six months before today's date
        LocalDateTime today=LocalDateTime.now();
        LocalDateTime pastSixMonthsDate=today.minusMonths(6);

        List<Transactions> listOfTransactions= fetchedAccount.getListOfTransactions().
                stream().filter(transactions -> transactions.getTransactionTimeStamp()
                        .isAfter(pastSixMonthsDate)).toList();

        listOfTransactions.sort(new SortDateComparator());
        return listOfTransactions.stream().map(Mapper::mapToTransactionsDto).toList();
    }

    private TransactionsDto payBills(TransactionsDto transactionsDto) throws TransactionException, AccountsException {
         String methodName="payBills(TransactionDto) in TransactionsServiceImpl";
        switch (transactionsDto.getDescription()) {
            // this will be built along with loan microservices
            //we need to call Loan microservices apis
            case EMI -> {
                //....................
            }

            //Do this for now, later we will build a scheduler to auto generate bills
            //every 30 days to  mimic a NACH/autopay system and have the amount to be paid
            case RENT -> {
               return payOrDepositMoney(transactionsDto,DEBIT);
            }

            //this wiil be built along with cards microservices
            //we need to call Cards microservices apis
            case CREDIT_CARD_BILL_PAYMENT -> {
                //....................
            }

            //Do this for now, later we will build a scheduler to auto generate bills
            //every 30 days to  mimic a NACH/autopay system  and have the amount to be paid
            case ELECTRICITY -> {
                return payOrDepositMoney(transactionsDto, DEBIT);
            }

            //Do this for now, later we will build a scheduler to auto generate bills
            //every 30 days to mimic a NACH/autopay system and have the amount to be paid
            case FAMILY -> {
                return payOrDepositMoney(transactionsDto, DEBIT);
            }

            //Do this for now, later we will build a scheduler to auto generate bills
            //every 30 days to  mimic a NACH/autopay system and have the amount to be paid
            case INVESTMENT -> {
                return payOrDepositMoney(transactionsDto, DEBIT);
            }

            //once in a while expense
            case E_SHOPPING, DONATION, BUSINESS, OTHERS -> {
                return payOrDepositMoney(transactionsDto, DEBIT);
            }

            default -> throw  new TransactionException("we do not support this types of transaction",methodName);
        }
        return transactionsDto;
    }

}
