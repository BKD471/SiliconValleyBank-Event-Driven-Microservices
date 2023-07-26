package com.siliconvalley.cardservices.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Audit {
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected LocalDateTime issuedDate;

    @CreatedBy
    protected  String authorizedBy;
    protected LocalDateTime expiredDate;
}
