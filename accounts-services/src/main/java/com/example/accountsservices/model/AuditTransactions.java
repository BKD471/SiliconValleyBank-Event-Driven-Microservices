package com.example.accountsservices.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditTransactions {
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false,nullable = false)
    protected LocalTime transactionTimeStamp;

    @CreatedDate
    @Temporal(TemporalType.DATE)
    @Column(updatable = false,nullable = false)
    protected LocalDate transactionDate;
}
