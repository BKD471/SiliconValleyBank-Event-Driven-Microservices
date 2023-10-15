package com.siliconvalley.accountsservices.helpers;

import com.siliconvalley.accountsservices.dto.responseDtos.PageableResponseDto;
import com.siliconvalley.accountsservices.model.Accounts;
import com.siliconvalley.accountsservices.model.Beneficiary;
import com.siliconvalley.accountsservices.model.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.DIRECTION;
import static com.siliconvalley.accountsservices.helpers.AllConstantHelpers.DIRECTION.asc;

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

    public static <e,d> PageableResponseDto<d> getPageableResponse(Page<e> page, Class<d> type){
        final List<e> entity=page.getContent();
        final List<d> userDtoList=entity.stream().map( Object->new ModelMapper().map(Object,type)).toList();

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
