package com.example.loansservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Getter
@AllArgsConstructor
public class ErrorDetails {
        private LocalTime timeStamp;
        private String message;
        private String details;
}
