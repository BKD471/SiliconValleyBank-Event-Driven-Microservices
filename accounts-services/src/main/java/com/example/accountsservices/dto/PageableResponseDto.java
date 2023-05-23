package com.example.accountsservices.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageableResponseDto<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalPages;
    private long totalElements;
    private boolean lastPage;
}
