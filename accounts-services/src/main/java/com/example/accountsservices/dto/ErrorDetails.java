package com.example.accountsservices.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
    private LocalTime timeStamp;
    private String message;
    private String details;
}
