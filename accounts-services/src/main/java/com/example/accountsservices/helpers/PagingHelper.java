package com.example.accountsservices.helpers;

import com.example.accountsservices.dto.PageableResponseDto;
import com.example.accountsservices.model.Accounts;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PagingHelper {

    private static final Set<String> setOfFieldNames = new HashSet<>();

    static {
        Accounts accountObj = new Accounts();
        Field[] fields = accountObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            setOfFieldNames.add(field.getName());
        }
        setOfFieldNames.remove("listOfBeneficiary");
        setOfFieldNames.remove("listOfTransactions");
        setOfFieldNames.remove("customer");
    }

    public static Set<String> getAllPageableFieldsOfAccounts() {
        return setOfFieldNames;
    }

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
