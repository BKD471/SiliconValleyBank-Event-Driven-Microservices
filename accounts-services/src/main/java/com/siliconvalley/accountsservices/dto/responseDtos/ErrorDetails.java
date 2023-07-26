package com.siliconvalley.accountsservices.dto.responseDtos;

import java.time.LocalTime;

public record ErrorDetails(LocalTime timeStamp,String message,String details) {
}
