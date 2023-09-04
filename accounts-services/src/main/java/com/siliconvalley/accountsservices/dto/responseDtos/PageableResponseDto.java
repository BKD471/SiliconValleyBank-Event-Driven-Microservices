package com.siliconvalley.accountsservices.dto.responseDtos;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageableResponseDto<T> implements Serializable {
    @Serial
    private static final long serialVersionUID=1234567891234567881L;
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalPages;
    private long totalElements;
    private boolean lastPage;
}
