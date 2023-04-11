package com.example.accountsservices.dto;

import lombok.AllArgsConstructor;

import java.time.LocalTime;


@AllArgsConstructor
public class ErrorDetails {
    private LocalTime timeStamp;
    private String message;
    private String details;
}
