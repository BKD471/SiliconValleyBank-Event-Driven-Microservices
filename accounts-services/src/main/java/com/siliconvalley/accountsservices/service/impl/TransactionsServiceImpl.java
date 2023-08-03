package com.siliconvalley.accountsservices.service.impl;


import com.siliconvalley.accountsservices.dto.outputDtos.OutputDto;
import com.siliconvalley.accountsservices.dto.baseDtos.TransactionsDto;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.TransactionException;
import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.helpers.MapperHelper;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.Customer;
import com.siliconvalley.accountsservices.model.Transactions;
import com.siliconvalley.accountsservices.repository.IAccountsRepository;
import com.siliconvalley.accountsservices.repository.ICustomerRepository;
import com.siliconvalley.accountsservices.repository.ITransactionsRepository;
import com.siliconvalley.accountsservices.service.AbstractService;
import com.siliconvalley.accountsservices.service.ITransactionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.*;
import static com.siliconvalley.accountsservices.helpers.MapperHelper.*;
import static java.util.Objects.isNull;

@Slf4j
@Service("transactionsServicePrimary")
public class TransactionsServiceImpl extends AbstractService implements ITransactionsService {

    private final ITransactionsRepository transactionsRepository;
    private final IAccountsRepository accountsRepository;

    TransactionsServiceImpl(final ITransactionsRepository transactionsRepository,
                            final IAccountsRepository accountsRepository,
                            final ICustomerRepository customerRepository) {
        super(accountsRepository,customerRepository);
        this.transactionsRepository = transactionsRepository;
        this.accountsRepository = accountsRepository;
    }

    private Transactions updateBalance(final Accounts accounts, final Transactions transactions, final Long amount, final AllConstantHelpers.TransactionType transactionType) throws TransactionException {
        log.debug("<--------------------updateBalance(Accounts, Transactions , Long , Transactions.TransactionType) TransactionsServiceImpl started ----------" +
                "--------------------------------------------------------------------------------------------------------->");
        final String methodName="updateBalance(Accounts,Transactions,Long,Transactions.TransactionType ) in TransactionsServiceImpl";
        final Long previousBalance = accounts.getBalance();

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
        log.debug("<---------updateBalance(Accounts , Transactions , Long , Transactions.TransactionType) TransactionsServiceImpl ended -----------------" +
                "-------------------------------------------------------------------------------------------------------------->");
        return transactions;
    }

    /**
     * @param transactionsDto
     * @returnType AccountsDto
     */

    private TransactionsDto payOrDepositMoney(final TransactionsDto transactionsDto, final AllConstantHelpers.TransactionType transactionType) throws AccountsException, TransactionException {
        log.debug("<-------------payOrDepositMoney(TransactionsDto, Transactions.TransactionType) TransactionsServiceImpl started -----------------------" +
                "-------------------------------------------------------------------------------------------------------------------------->");
        //fetch account
        final String accountNumber = transactionsDto.getAccountNumber();
        final Accounts fetchedAccount = fetchAccountByAccountNumber(accountNumber);

        //converting to entity object
        final Transactions requestTransaction = mapToTransactions(transactionsDto);

        //get the money & update the balance
        final Long amountToBeCredited = requestTransaction.getTransactionAmount();
        final Transactions recentTransaction = updateBalance(fetchedAccount, requestTransaction,
                amountToBeCredited, transactionType);

        //some critical linkup before saving it to db
        final List<Transactions> listOfTransactions=new ArrayList<>();
        listOfTransactions.add(recentTransaction);
        fetchedAccount.setListOfTransactions(listOfTransactions);
        recentTransaction.setAccounts(fetchedAccount);

        //save in DB & return
        final Transactions savedTransactions = transactionsRepository.save(recentTransaction);
        log.debug("<-------------payOrDepositMoney(TransactionsDto, Transactions.TransactionType) TransactionsServiceImpl ended ------------------------" +
                "------------------------------------------------------------------------------------------------------------------------>");
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
    public OutputDto transactionsExecutor(final TransactionsDto transactionsDto) throws TransactionException, AccountsException {
        final String methodName="transactionsExecutor(TransactionsDto) in TransactionsServiceImpl";

        final String accountNumber=transactionsDto.getAccountNumber();
        final Accounts fetchedAccount=fetchAccountByAccountNumber(accountNumber);
        final Customer fetchedCustomer=fetchedAccount.getCustomer();

        //set transactionId
        final String transactionId= UUID.randomUUID().toString();
        transactionsDto.setTransactionId(transactionId);

        if(isNull(transactionsDto.getTransactionType())) throw new TransactionException(TransactionException.class,
                "Please provide transaction Type",methodName);
        switch (transactionsDto.getTransactionType()) {
            case CREDIT -> {
                final TransactionsDto transactionDetails=payOrDepositMoney(transactionsDto, CREDIT);
                return OutputDto.builder()
                        .customer(mapToCustomerOutputDto(mapToCustomerDto(fetchedCustomer)))
                        .accounts(mapToAccountsOutputDto(mapToAccountsDto(fetchedAccount)))
                        .transactions(transactionDetails)
                        .defaultMessage(String.format("Recent Transactions Details for account:%s",accountNumber))
                        .build();
            }
            case DEBIT -> {
                final TransactionsDto transactionDetails=payBills(transactionsDto);
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
    public OutputDto getPastSixMonthsTransactionsForAnAccount(final String accountNumber) throws  AccountsException{
        //fetch account
        final Accounts fetchedAccount=fetchAccountByAccountNumber(accountNumber);
        final Customer loadedCustomer=fetchedAccount.getCustomer();

        //Calculate the  date six months before today's date
        final LocalDateTime today=LocalDateTime.now();
        final LocalDateTime pastSixMonthsDate=today.minusMonths(6);

        final List<Transactions> listOfTransactions= new ArrayList<>(fetchedAccount.getListOfTransactions().
                stream().filter(transactions -> transactions.getTransactionTimeStamp()
                        .isAfter(pastSixMonthsDate)).toList());

        listOfTransactions.sort((o1,o2)->(o1.getTransactionTimeStamp().isBefore(o2.getTransactionTimeStamp()))? -1:
                (o1.getTransactionTimeStamp().isAfter(o2.getTransactionTimeStamp()))? 1:0);

        final ArrayList<TransactionsDto> transactionsArrayList= listOfTransactions.stream()
                .map(MapperHelper::mapToTransactionsDto)
                .collect(Collectors.toCollection(ArrayList::new));

        return OutputDto.builder()
                .customer(mapToCustomerOutputDto(mapToCustomerDto(loadedCustomer)))
                .accounts(mapToAccountsOutputDto(mapToAccountsDto(fetchedAccount)))
                .transactionsList(transactionsArrayList)
                .defaultMessage(String.format("Last 6 months transaction details for account:%s",accountNumber))
                .build();
    }

    /**
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public OutputDto downloadTransactionStatmentAsCsv(LocalDate startDate, LocalDate endDate) {
        return null;
    }

    private TransactionsDto payBills(final TransactionsDto transactionsDto) throws TransactionException, AccountsException {
        log.debug("<---------payBills(TransactionsDto transactionsDto) started --------------------------------------------------------------------" +
                "--------------------------------------------------------------------------------->");
        final String methodName="payBills(TransactionDto) in TransactionsServiceImpl";

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
        log.debug("<--------------------payBills(TransactionsDto) ended -------------------" +
                "------------------------------------------------------------------------------------------------>");
        return transactionsDto;
    }
}
