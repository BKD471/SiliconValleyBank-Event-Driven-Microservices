package com.example.accountsservices.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditTransactions {
    @CreatedDate
    @Column(updatable = false,nullable = false)
    protected LocalDateTime transactionTimeStamp;
}
