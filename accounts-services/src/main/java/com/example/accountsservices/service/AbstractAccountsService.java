package com.example.accountsservices.service;

import com.example.accountsservices.dto.AccountsDto;
import com.example.accountsservices.dto.BeneficiaryDto;
import com.example.accountsservices.dto.TransactionsDto;
import com.example.accountsservices.exception.AccountsException;
import com.example.accountsservices.exception.BeneficiaryException;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.repository.AccountsRepository;

import java.util.List;
import java.util.Optional;

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
    private  static final String REQUEST_TO_BLOCK="block";


    protected AbstractAccountsService(AccountsRepository accountsRepository){
        this.accountsRepository=accountsRepository;
    }


    public AccountsDto createAccounts(AccountsDto accountsDto) {
        return null;
    }
    public AccountsDto getAccountInfoByCustomerIdAndAccountNumber(Long customerId, Long accountNumber) throws AccountsException {return null;}
    public List<AccountsDto> getAllAccountsByCustomerId(Long customerId) throws AccountsException {return null;}
    public AccountsDto updateAccountByCustomerIdAndAccountNumber(Long customerId, Long accountNumber, AccountsDto accountsDto) throws AccountsException {return null;}
    public void deleteAccount(Long accountNumber) throws AccountsException {/*dummy implementations*/}
    public  void blockAccount(Long accountNumber) throws  AccountsException{/*dummy implementation*/}
    public  void deleteAllAccountsByCustomer(Long customerId) throws  AccountsException{/*dummy implementations*/}
    public BeneficiaryDto addBeneficiary(Long customerId, Long accountNumber, BeneficiaryDto beneficiaryDto) throws AccountsException {return null;}
    public BeneficiaryDto updateBeneficiaryDetailsOfanAccount(Long accountNumber, BeneficiaryDto beneficiaryDto) throws AccountsException, BeneficiaryException {return null;}
    public List<BeneficiaryDto> getAllBeneficiariesOfAnAccountByAccountNumber(Long accountNumber) throws AccountsException{return null;}
    public void deleteBeneficiariesForAnAccount(Long accountNumber,Long beneficiaryId) throws  AccountsException , BeneficiaryException{/*dummy implementation*/}
    public  void  deleteAllBeneficiaries(Long accountNumber) throws AccountsException {/*dummy*/}
    public TransactionsDto depositMoney(Long accountNumber, Long accountNumberSender, Long amount) {return null;}
    public TransactionsDto transferMoneyToOtherAccounts(Long accountNumber,Long accountNumberDestination,Long amount) {return null;}
    public List<TransactionsDto> getPastSixMonthsTransactionsForAnAccount( Long accountNumber) {return null;}
    public TransactionsDto payBills(Long accountNumber,TransactionsDto transactionsDto){return null;}
    protected Accounts fetchAccountByAccountNumber(Long accountNumber, String ...request) throws AccountsException {
        Optional<Accounts> fetchedAccounts = Optional.ofNullable(accountsRepository.findByAccountNumber(accountNumber));
        if (fetchedAccounts.isEmpty())
            throw new AccountsException(String.format("No such accounts exist with id %s", accountNumber));

        boolean checkAccountIsBlocked=STATUS_BLOCKED.equals(fetchedAccounts.get().getAccountStatus());
        if(request.length>0 && request[0].equalsIgnoreCase(REQUEST_TO_BLOCK) && checkAccountIsBlocked) throw new AccountsException(String.format("Account of id %s is already blocked",accountNumber));
        else if(checkAccountIsBlocked) throw new AccountsException(String.format("Account of id %s is in %s status",accountNumber,STATUS_BLOCKED));
        return fetchedAccounts.get();
    }
}
