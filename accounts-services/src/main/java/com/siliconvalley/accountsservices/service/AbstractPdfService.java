package com.siliconvalley.accountsservices.service;

import com.siliconvalley.accountsservices.helpers.AllConstantHelpers;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.Transactions;
import com.siliconvalley.accountsservices.repository.IAccountsRepository;
import com.siliconvalley.accountsservices.repository.ICustomerRepository;
import net.sf.jasperreports.engine.JRException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractPdfService extends AbstractService implements IPdfService{


    protected AbstractPdfService(IAccountsRepository accountsRepository, ICustomerRepository customerRepository) {
        super(accountsRepository, customerRepository);
    }

    protected final Set<Transactions> prepareTransactionsSetBetweenDate(LocalDate startDate, LocalDate endDate, String accountNumber) {
        final Accounts accounts=fetchAccountByAccountNumber(accountNumber);
        LocalDateTime startDateTime=startDate.atTime(LocalTime.from(LocalDateTime.now()));
        LocalDateTime endDateTime=endDate.atTime(LocalTime.from(LocalDateTime.now()));

        Predicate<Transactions> conditionToFilterOutListOfTransactionBetweenGivenTimeInterval =
                (transactions)->transactions.getTransactionTimeStamp().isAfter(startDateTime)
                        && transactions.getTransactionTimeStamp().isBefore(endDateTime);

        return accounts.getListOfTransactions()
                .stream().filter(conditionToFilterOutListOfTransactionBetweenGivenTimeInterval).collect(Collectors.toSet());
    }
    /**
     * @param startDate
     * @param endDate
     * @param accountNumber
     * @return
     * @throws FileNotFoundException
     */
    @Override
    public ByteArrayInputStream generateBankStatement(LocalDate startDate, LocalDate endDate, String accountNumber) throws FileNotFoundException {
        return null;
    }

    /**
     * @param reportFormat
     * @param startDate
     * @param endDate
     * @param accountNumber
     * @throws FileNotFoundException
     * @throws JRException
     */
    @Override
    public void generateBankStatement(AllConstantHelpers.FORMAT_TYPE reportFormat, LocalDate startDate, LocalDate endDate, String accountNumber) throws FileNotFoundException, JRException {

    }
}
