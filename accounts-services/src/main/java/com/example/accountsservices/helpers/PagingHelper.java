package com.example.accountsservices.helpers;

import com.example.accountsservices.dto.responseDtos.PageableResponseDto;
import com.example.accountsservices.model.Accounts;
import com.example.accountsservices.model.Beneficiary;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.accountsservices.helpers.AllEnumConstantHelpers.DIRECTION;
import static com.example.accountsservices.helpers.AllEnumConstantHelpers.DIRECTION.asc;

public class PagingHelper {
    private static final Set<String> setOfAccountFieldNames = new HashSet<>();
    private static final Set<String> setOfBeneficiaryFieldNames = new HashSet<>();
    public static final int DEFAULT_PAGE_SIZE = 5;
    public static final DIRECTION PAGE_SORT_DIRECTION_ASCENDING = asc;

    static {
        //fetching the attribs of Accounts
        Field[] accFields = Accounts.class.getDeclaredFields();
        for (Field field : accFields) {
            setOfAccountFieldNames.add(field.getName());
        }
        setOfAccountFieldNames.remove("listOfBeneficiary");
        setOfAccountFieldNames.remove("listOfTransactions");
        setOfAccountFieldNames.remove("customer");

        //fetching the attribs of Beneficiary
        Field[] benFields=Beneficiary.class.getDeclaredFields();
        for(Field field:benFields){
            setOfBeneficiaryFieldNames.add(field.getName());
        }
        setOfBeneficiaryFieldNames.remove("accounts");
    }

    public static Set<String> getAllPageableFieldsOfAccounts() {
        return setOfAccountFieldNames;
    }
    public static Set<String> getAllPageableFieldsOfBeneficiary(){return setOfBeneficiaryFieldNames;}

    public static <e,d> PageableResponseDto<d> getPageableResponse(Page<e> page, Class<d> type){
        List<e> entity=page.getContent();
        List<d> userDtoList=entity.stream().map( Object->new ModelMapper().map(Object,type)).toList();

        PageableResponseDto<d> responseDto=new PageableResponseDto<>();
        responseDto.setContent(userDtoList);
        responseDto.setPageNumber(page.getNumber());
        responseDto.setPageSize(page.getSize());
        responseDto.setTotalPages(page.getTotalPages());
        responseDto.setTotalElements(page.getTotalElements());
        responseDto.setLastPage(page.isLast());
        return responseDto;
    }
}
