package com.example.loansservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class ErrorDetails {
        private LocalDateTime timeStamp;
        private String message;
        private String details;
}
