package com.example.loansservices.aspect;

import com.example.loansservices.dto.Dto;
import com.example.loansservices.dto.LoansDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;

@Aspect
@Component
@Slf4j
public class LoggerAspect {
    @Around("execution(*  com.example.loansservices.service.*.*(..))")
    public Dto log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("<---------------------------------->"+joinPoint.
                getSignature().
                toString() + " method executions starts---------------------------------------------------------------->");
        Instant start = Instant.now();
        Object result = joinPoint.proceed();
        Instant end = Instant.now();
        long timeElapsedInMs = Duration.between(start, end).toMillis();
        log.info(String.format("<-----------------Time elapsed to execute %s in Ms is %s------------------------------------->", joinPoint.getSignature().toString(), timeElapsedInMs));
        log.info("<--------------------------------->"+joinPoint.getSignature().toString() + "method execution stops------------------------------------>");
        return (Dto) result;
    }

    @AfterThrowing(value = "execution(* com.example.loansservices.service.*.*(..))", throwing = "e")
    public void logException(JoinPoint joinPoint, Exception e) {
        log.debug(Level.SEVERE + "<------------------------------Exception is thrown due to-------------" + e.getMessage() + " from ------------------------------->"
                + joinPoint.getSignature().toString());
    }
}
