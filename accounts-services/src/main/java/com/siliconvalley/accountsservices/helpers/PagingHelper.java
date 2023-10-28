package com.siliconvalley.accountsservices.helpers;

import com.siliconvalley.accountsservices.dto.baseDtos.AccountsDto;
import com.siliconvalley.accountsservices.dto.baseDtos.BeneficiaryDto;
import com.siliconvalley.accountsservices.dto.baseDtos.CustomerDto;
import com.siliconvalley.accountsservices.dto.responseDtos.PageableResponseDto;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.Beneficiary;
import com.siliconvalley.accountsservices.model.Customer;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.*;

import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.DIRECTION;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.DIRECTION.asc;
import static com.siliconvalley.accountsservices.helpers.MapperHelper.*;

public class PagingHelper {
    public static final int DEFAULT_PAGE_SIZE = 5;
    public static final DIRECTION PAGE_SORT_DIRECTION_ASCENDING = asc;

    private static Set<String> getSetsOfAccountFieldNames(){
        final Set<String> setOfAccountFieldNames=new HashSet<>();
        //fetching the attribs of Accounts
        Field[] accFields = Accounts.class.getDeclaredFields();
        for (Field field : accFields) setOfAccountFieldNames.add(field.getName());

        setOfAccountFieldNames.remove("listOfBeneficiary");
        setOfAccountFieldNames.remove("listOfTransactions");
        setOfAccountFieldNames.remove("customer");
        return  setOfAccountFieldNames;
    }

    private static Set<String> getSetsOfBeneficiaryFieldNames(){
        final Set<String> setOfBeneficiaryFieldNames=new HashSet<>();
        //fetching the attribs of Beneficiary
        Field[] benFields= Beneficiary.class.getDeclaredFields();
        for(Field field:benFields) setOfBeneficiaryFieldNames.add(field.getName());
        setOfBeneficiaryFieldNames.remove("accounts");
        return setOfBeneficiaryFieldNames;
    }

    private static  Set<String> getSetsOfCustomerFieldsName(){
        final Set<String> setOfCustomerFieldNames=new HashSet<>();
        Field[] custFields= Customer.class.getDeclaredFields();
        for(Field field:custFields) setOfCustomerFieldNames.add(field.getName());
        setOfCustomerFieldNames.remove("accounts");
        setOfCustomerFieldNames.remove("roles");
        return setOfCustomerFieldNames;
    }

    public static Set<String> getAllPageableFieldsOfAcustomer(){
        return getSetsOfCustomerFieldsName();
    }
    public static Set<String> getAllPageableFieldsOfAccounts() {
        return getSetsOfAccountFieldNames();
    }
    public static Set<String> getAllPageableFieldsOfBeneficiary(){return getSetsOfBeneficiaryFieldNames();}

    public static <e,d> PageableResponseDto<d> getPageableResponse(Page<e> page, AllConstantHelpers.DestinationDtoType destinationDtoType){
        final List<e> entity=page.getContent();

        List<d> userDtoList=new ArrayList<>();

        switch (destinationDtoType){
            case AccountsDto -> {
                if((!entity.isEmpty() && entity.get(0) instanceof Accounts)){
                    userDtoList= (List<d>) entity.stream().map(e->mapToAccountsDto((Accounts) e)).toList();
                }
            }

            case CustomerDto -> {
                if((!entity.isEmpty() && entity.get(0) instanceof Customer)){
                    userDtoList= (List<d>) entity.stream().map(e->mapToCustomerDto((Customer) e)).toList();
                }
            }

            case BeneficiaryDto -> {
                if(!entity.isEmpty() && entity.get(0) instanceof Beneficiary){
                    userDtoList= (List<d>) entity.stream().map(e->mapToBeneficiaryDto((Beneficiary) e)).toList();
                }
            }
        }


        final PageableResponseDto<d> responseDto=new PageableResponseDto.Builder<d>()
                .content(userDtoList)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .lastPage(page.isLast())
                .build();

        return responseDto;
    }
}
