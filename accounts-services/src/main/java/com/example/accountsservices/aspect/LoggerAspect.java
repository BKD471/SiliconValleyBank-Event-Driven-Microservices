package com.example.accountsservices.aspect;

import com.example.accountsservices.dto.outputDtos.OutputDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;


@Aspect
@Component
@Slf4j
public class LoggerAspect {

    @Around(value = "execution(com.example.accountsservices.dto.outputDtos.OutputDto  com.example.accountsservices.service.*.*(..))")
    public OutputDto log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("<---------------------------------->"+joinPoint.
                getSignature().
                toString() + " method executions starts---------------------------------------------------------------->");
        Instant start = Instant.now();
        Object result = joinPoint.proceed();
        Instant end = Instant.now();
        long timeElapsedInMs = Duration.between(start, end).toMillis();
        log.info(String.format("<-----------------Time elapsed to execute %s in Ms is %s------------------------------------->", joinPoint.getSignature().toString(), timeElapsedInMs));
        log.info("<--------------------------------->"+joinPoint.getSignature().toString() + "method execution completed------------------------------------>");
        return  (OutputDto) result;
    }

    @AfterThrowing(value = "execution(* com.example.accountsservices.service.*.*(..))", throwing = "e")
    public void logException(JoinPoint joinPoint, Exception e) throws Exception {
        log.error("<-----------------------------------------" + e.getMessage() + " from ------------------------------->"
                + joinPoint.getSignature().toString());
        log.error("DANGER!!!!");
        throw new Exception(e.getMessage());
    }
}