package com.siliconvalley.accountsservices.service;

import com.siliconvalley.accountsservices.model.Transactions;
import com.siliconvalley.accountsservices.repository.IAccountsRepository;
import com.siliconvalley.accountsservices.repository.ICustomerRepository;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.Customer;
import com.siliconvalley.accountsservices.exception.AccountsException;
import com.siliconvalley.accountsservices.exception.CustomerException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.REQUEST_TO_BLOCK;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.STATUS_BLOCKED;


public abstract class AbstractService {
    private  final IAccountsRepository accountsRepository;
    private  final ICustomerRepository customerRepository;

    protected AbstractService(IAccountsRepository accountsRepository,
                              ICustomerRepository customerRepository){
        this.accountsRepository=accountsRepository;
        this.customerRepository=customerRepository;
    }

    protected final Accounts fetchAccountByAccountNumber(final String accountNumber) throws AccountsException {
        final String methodName="fetchAccountByAccountNumber(Long,String vararg) in AbstractAccountsService";
        final Optional<Accounts> fetchedAccounts = accountsRepository.findByAccountNumber(accountNumber);
        if (fetchedAccounts.isEmpty())
            throw new AccountsException(AccountsException.class,String.format("No such accounts exist with id %s", accountNumber),methodName);

        final boolean checkAccountIsBlocked=STATUS_BLOCKED.equals(fetchedAccounts.get().getAccountStatus());
        if(checkAccountIsBlocked) throw new AccountsException(AccountsException.class,String.format("Account of id %s is in %s status",accountNumber,STATUS_BLOCKED),methodName);
        return fetchedAccounts.get();
    }

    protected final Customer fetchCustomerByCustomerNumber(final String customerId) throws CustomerException {
        final String methodName="fetchCustomerByCustomerNumber(Long)";
        final Optional<Customer> loadCustomer=customerRepository.findById(customerId);
        if(loadCustomer.isEmpty()) throw  new CustomerException(CustomerException.class,String.format("No such customer with id %s exist",customerId),
                methodName);
        return loadCustomer.get();
    }

    protected final List<Transactions> prepareTransactionsListBetweenDate(LocalDate startDate, LocalDate endDate, String accountNumber) {
        final Accounts accounts=fetchAccountByAccountNumber(accountNumber);
        LocalDateTime startDateTime=startDate.atTime(LocalTime.from(LocalDateTime.now()));
        LocalDateTime endDateTime=endDate.atTime(LocalTime.from(LocalDateTime.now()));

        Predicate<Transactions> conditionToFilterOutListOfTransactionBetweenGivenTimeInterval =
                (transactions)->transactions.getTransactionTimeStamp().isAfter(startDateTime)
                        && transactions.getTransactionTimeStamp().isBefore(endDateTime);

        return accounts.getListOfTransactions()
                .stream().filter(conditionToFilterOutListOfTransactionBetweenGivenTimeInterval).toList();
    }
}
