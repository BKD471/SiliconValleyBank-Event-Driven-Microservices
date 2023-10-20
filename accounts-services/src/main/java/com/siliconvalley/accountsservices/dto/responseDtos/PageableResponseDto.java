package com.siliconvalley.accountsservices.dto.responseDtos;


import java.util.List;

public record PageableResponseDto<T>(List<T> content,int pageNumber,
                                     int pageSize,long totalPages,
                                     long totalElements,boolean lastPage){

    public PageableResponseDto<T> withContent(List<T> content){
        return new PageableResponseDto<T>(content,pageNumber(),pageSize(),totalPages(),totalElements(),lastPage());
    }
    public PageableResponseDto<T> withPageNumber(int pageNumber){
        return new PageableResponseDto<T>(content(),pageNumber,pageSize(),totalPages(),totalElements(),lastPage());
    }

    public PageableResponseDto<T> withPageSize(int pageSize){
        return new PageableResponseDto<T>(content(),pageNumber(),pageSize,totalPages(),totalElements(),lastPage());
    }

    public PageableResponseDto<T> withTotalPagses(long totalPagses){
        return new PageableResponseDto<T>(content(),pageNumber(),pageSize(),totalPages,totalElements(),lastPage());
    }

    public PageableResponseDto<T> withTotalElements(long totalElements){
        return new PageableResponseDto<T>(content(),pageNumber(),pageSize(),totalPages(),totalElements,lastPage());
    }
    public PageableResponseDto<T> withLastPage(boolean lastPage){
        return new PageableResponseDto<T>(content(),pageNumber(),pageSize(),totalPages(),totalElements(),lastPage);
    }
    public static final class Builder<T>{
        private List<T> content;
        private int pageNumber;
        private int pageSize;
        private long totalPages;
        private long totalElements;
        private boolean lastPage;

        public Builder(){}

        public Builder content(List<T> content){
            this.content=content;
            return this;
        }
        public Builder pageNumber(int pageNumber){
            this.pageNumber=pageNumber;
            return this;
        }
        public Builder pageSize(int pageSize){
            this.pageSize=pageSize;
            return this;
        }
        public Builder totalPages(long totalPages){
            this.totalPages=totalPages;
            return this;
        }
        public Builder lastPage(boolean lastPage){
            this.lastPage=lastPage;
            return this;
        }

        public Builder totalElements(long totalPages){
            this.totalPages=totalPages;
            return this;
        }

        public PageableResponseDto<T> build(){
            return new PageableResponseDto<>(content, pageNumber, pageSize, totalPages, totalElements, lastPage);
        }
    }
}

