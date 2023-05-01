package com.example.accountsservices.service;

import com.example.accountsservices.dto.*;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.exception.CustomerException;
import com.example.accountsservices.exception.TransactionException;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.repository.AccountsRepository;

import java.util.List;
import java.util.Optional;
import static com.example.accountsservices.service.impl.AccountsServiceImpl.REQUEST_TO_BLOCK;

//DESIGN NOTE
//Obeying I of SOLID ,to not pollute a single interface with too much methods
// by splitting it up.
//we want to have a single parent of all service classes so need a class to provide
// dummy implementation of all abstract methods of all interface
// so that we can spilt up service logic into separate concerning classes
// and thus obeying S of SOLID also

//since there is no need of instantiating the AbstractAccountsService ,
// it's just to provide dummy implementation ,so make it abstract
// it can be used for loose coupling or having some logic that will be used by all three service classes

public abstract class AbstractAccountsService implements IAccountsService, ITransactionsService, IBeneficiaryService {

    private  final  AccountsRepository accountsRepository;
    private  static final Accounts.AccountStatus STATUS_BLOCKED= Accounts.AccountStatus.BLOCKED;



    protected AbstractAccountsService(AccountsRepository accountsRepository){
        this.accountsRepository=accountsRepository;
    }
    public OutputDto postRequestExecutor(InputDto inputDto) throws AccountsException{ return null;}
    public OutputDto  putRequestExecutor(InputDto inputDto) throws AccountsException{ return null;}
    public OutputDto getRequestExecutor(InputDto inputDto) throws AccountsException, CustomerException { return null;}
    public OutputDto deleteRequestExecutor(InputDto inputDto) throws AccountsException{ return null;}

    //ben
    public BeneficiaryDto addBeneficiary(Long accountNumber, BeneficiaryDto beneficiaryDto) throws AccountsException {return null;}
    public BeneficiaryDto updateBeneficiaryDetailsOfanAccount(Long accountNumber, BeneficiaryDto beneficiaryDto) throws AccountsException, BeneficiaryException {return null;}
    public List<BeneficiaryDto> getAllBeneficiariesOfAnAccountByAccountNumber(Long accountNumber) throws AccountsException{return null;}
    public void deleteBeneficiariesForAnAccount(Long accountNumber,Long beneficiaryId) throws  AccountsException , BeneficiaryException{/*dummy implementation*/}
    public  void  deleteAllBeneficiaries(Long accountNumber) throws AccountsException {/*dummy*/}

    //transactions
    public TransactionsDto transactionsExecutor(TransactionsDto transactionsDto) throws  TransactionException , AccountsException { return null;}
    public List<TransactionsDto> getPastSixMonthsTransactionsForAnAccount( Long accountNumber) throws AccountsException {return null;}
    protected Accounts fetchAccountByAccountNumber(Long accountNumber, String ...request) throws AccountsException {
        String methodName="fetchAccountByAccountNumber() in AbstractAccountsService";
        Optional<Accounts> fetchedAccounts = Optional.ofNullable(accountsRepository.findByAccountNumber(accountNumber));
        if (fetchedAccounts.isEmpty())
            throw new AccountsException(AccountsException.class,String.format("No such accounts exist with id %s", accountNumber),methodName);

        boolean checkAccountIsBlocked=STATUS_BLOCKED.equals(fetchedAccounts.get().getAccountStatus());
        if(request.length>0 && request[0].equalsIgnoreCase(REQUEST_TO_BLOCK) && checkAccountIsBlocked) throw new AccountsException(AccountsException.class,String.format("Account of id %s is already blocked",accountNumber),methodName);
        else if(checkAccountIsBlocked) throw new AccountsException(AccountsException.class,String.format("Account of id %s is in %s status",accountNumber,STATUS_BLOCKED),methodName);
        return fetchedAccounts.get();
    }
}
