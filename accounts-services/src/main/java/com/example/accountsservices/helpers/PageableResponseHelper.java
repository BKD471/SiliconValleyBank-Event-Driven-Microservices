package com.example.accountsservices.helpers;

import com.example.accountsservices.dto.PageableResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.stream.Collectors;

public class PageableResponseHelper {

    //e -->entity
    //d--> dto
    public static <e,d>PageableResponseDto<d> getPageableResponse(Page<e> page,Class<d> type){
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
