package com.example.accountsservices.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;


@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Audit{

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false,nullable = false)
    protected Date createdDate;

    @CreatedBy
    protected String CreatedBy;

}
