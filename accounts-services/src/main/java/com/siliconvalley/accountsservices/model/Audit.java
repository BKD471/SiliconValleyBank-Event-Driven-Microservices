package com.siliconvalley.accountsservices.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;


@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Audit{
    @CreatedDate
    @Column(updatable = false,nullable = false)
    protected LocalDate createdDate;

    @CreatedBy
    protected String createdBy;

}
