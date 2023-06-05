package com.example.accountsservices.service.impl;


import com.example.accountsservices.dto.outputDtos.OutputDto;
import com.example.accountsservices.dto.TransactionsDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.TransactionException;
import com.example.accountsservices.helpers.MapperHelper;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Customer;
import com.example.accountsservices.model.Transactions;
import com.example.accountsservices.repository.AccountsRepository;
import com.example.accountsservices.repository.CustomerRepository;
import com.example.accountsservices.repository.TransactionsRepository;
import com.example.accountsservices.service.AbstractAccountsService;
import com.example.accountsservices.helpers.SortDateComparator;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static com.example.accountsservices.helpers.MapperHelper.*;

@Service
public class TransactionsServiceImpl extends AbstractAccountsService {
    private final Transactions.TransactionType CREDIT = Transactions.TransactionType.CREDIT;
    private final Transactions.TransactionType DEBIT = Transactions.TransactionType.DEBIT;
    private final TransactionsRepository transactionsRepository;
    private final AccountsRepository accountsRepository;

    TransactionsServiceImpl(TransactionsRepository transactionsRepository,
                            AccountsRepository accountsRepository,
                            CustomerRepository customerRepository) {
        super(accountsRepository,customerRepository);
        this.transactionsRepository = transactionsRepository;
        this.accountsRepository = accountsRepository;
    }

    private Transactions updateBalance(Accounts accounts, Transactions transactions, Long amount, Transactions.TransactionType transactionType) throws TransactionException {
        String methodName="updateBalance(Accounts,Transactions,Long,Transactions.TransactionType ) in TransactionsServiceImpl";

        Long previousBalance = accounts.getBalance();

        if (CREDIT.equals(transactionType)) {
            accounts.setBalance(previousBalance + amount);
            transactions.setTransactionType(CREDIT);
        }
        if (DEBIT.equals(transactionType)) {
            if (previousBalance >= amount) accounts.setBalance(previousBalance - amount);
            else throw new TransactionException(TransactionException.class,"Insufficient Balance",methodName);
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
        Long accountNumber = transactionsDto.getAccountNumber();
        Accounts fetchedAccount = fetchAccountByAccountNumber(accountNumber);

        //converting to entity object
        Transactions requestTransaction = mapToTransactions(transactionsDto);

        //get the money & update the balance
        Long amountToBeCredited = requestTransaction.getTransactionAmount();
        Transactions recentTransaction = updateBalance(fetchedAccount, requestTransaction,
                amountToBeCredited, transactionType);

        //some critical linkup before saving it to db
        List<Transactions> listOfTransactions=new ArrayList<>();
        listOfTransactions.add(recentTransaction);
        fetchedAccount.setListOfTransactions(listOfTransactions);
        recentTransaction.setAccounts(fetchedAccount);

        //save in DB & return
        Transactions savedTransactions = transactionsRepository.save(recentTransaction);
        return mapToTransactionsDto(savedTransactions);
    }

    //using switch expression to decide debit or credit type transactions
    //among all transactions of descriptions like
    // EMI,CREDIT_CARD_BILL,DONATION,RENT,E_SHOPPING,
    // BUSINESS,INVESTMENT,FAMILY,ELECTRICITY,OTHERS
    //are all debit type transactions
    //only SALARY is credit type, we only add money ,so it's simple need not to add too much complexity
    //so just call payOrDeposit method to process the transaction
    @Override
    public OutputDto transactionsExecutor(TransactionsDto transactionsDto) throws TransactionException, AccountsException {
        String methodName="transactionsExecutor(TransactionsDto) in TransactionsServiceImpl";

        Long accountNumber=transactionsDto.getAccountNumber();
        Accounts fetchedAccount=fetchAccountByAccountNumber(accountNumber);
        Customer fetchedCustomer=fetchedAccount.getCustomer();

        if(null==transactionsDto.getTransactionType()) throw new TransactionException(TransactionException.class,
                "Please provide transaction Type",methodName);
        switch (transactionsDto.getTransactionType()) {
            case CREDIT -> {
                TransactionsDto transactionDetails=payOrDepositMoney(transactionsDto, CREDIT);
                return OutputDto.builder()
                        .customer(mapToCustomerOutputDto(mapToCustomerDto(fetchedCustomer)))
                        .accounts(mapToAccountsOutputDto(mapToAccountsDto(fetchedAccount)))
                        .transactions(transactionDetails)
                        .defaultMessage(String.format("Recent Transactions Details for account:%s",accountNumber))
                        .build();
            }
            case DEBIT -> {
                TransactionsDto transactionDetails=payBills(transactionsDto);
                return OutputDto.builder()
                        .customer(mapToCustomerOutputDto(mapToCustomerDto(fetchedCustomer)))
                        .accounts(mapToAccountsOutputDto(mapToAccountsDto(fetchedAccount)))
                        .transactions(transactionDetails)
                        .defaultMessage(String.format("Recent Transaction details for account:%s",accountNumber))
                        .build();
            }
            default -> throw new TransactionException(TransactionException.class,
                    "Please Specify a valid transaction type",methodName);
        }
    }

    @Override
    public OutputDto getPastSixMonthsTransactionsForAnAccount(Long accountNumber) throws  AccountsException{
        //fetch account
        Accounts fetchedAccount=fetchAccountByAccountNumber(accountNumber);
        Customer loadedCustomer=fetchedAccount.getCustomer();

        //Calculate the  date six months before today's date
        LocalDateTime today=LocalDateTime.now();
        LocalDateTime pastSixMonthsDate=today.minusMonths(6);

        List<Transactions> listOfTransactions= new ArrayList<>(fetchedAccount.getListOfTransactions().
                stream().filter(transactions -> transactions.getTransactionTimeStamp()
                        .isAfter(pastSixMonthsDate)).toList());

        listOfTransactions.sort(new SortDateComparator());
        ArrayList<TransactionsDto> transactionsArrayList= listOfTransactions.stream()
                .map(MapperHelper::mapToTransactionsDto)
                .collect(Collectors.toCollection(ArrayList::new));

        return OutputDto.builder()
                .customer(mapToCustomerOutputDto(mapToCustomerDto(loadedCustomer)))
                .accounts(mapToAccountsOutputDto(mapToAccountsDto(fetchedAccount)))
                .transactionsList(transactionsArrayList)
                .defaultMessage(String.format("Last 6 months transaction details for account:%s",accountNumber))
                .build();
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

            default -> throw  new TransactionException(TransactionException.class,
                    "we do not support this types of transaction yet",methodName);
        }
        return transactionsDto;
    }

}
