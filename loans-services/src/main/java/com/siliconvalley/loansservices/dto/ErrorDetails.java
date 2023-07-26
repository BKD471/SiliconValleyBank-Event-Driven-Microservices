package com.siliconvalley.loansservices.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDetails {
    private LocalTime timeStamp;
    private String message;
    private String details;
}
