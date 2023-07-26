package com.siliconvalley.loansservices.aspect;

import com.siliconvalley.loansservices.dto.Dto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Aspect
@Component
@Slf4j
public class LoggerAspect {

    @Around(value = "execution(com.siliconvalley.loansservices.dto.Dto  com.example.loansservices.service.*.*(..))")
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
        return  (Dto)result;
    }

    @Around(value = "execution(java.util.List com.example.loansservices.service.*.*(..))")
    public List<Dto> logForMethodsWithReturnTypeList(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("<---------------------------------->"+joinPoint.
                getSignature().
                toString() + " method executions starts---------------------------------------------------------------->");
        log.info("<------------------------Preparing the list---------------------------------------------------------->");
        Instant start = Instant.now();
        Object result = joinPoint.proceed();
        Instant end = Instant.now();
        long timeElapsedInMs = Duration.between(start, end).toMillis();
        log.info(String.format("<-----------------Time elapsed to execute %s in Ms is %s------------------------------------->", joinPoint.getSignature().toString(), timeElapsedInMs));
        log.info("<--------------------------------->"+joinPoint.getSignature().toString() + "method execution stops------------------------------------>");
        return (List<Dto>) result;
    }
    @AfterThrowing(value = "execution(* com.example.loansservices.service.*.*(..))", throwing = "e")
    public void logException(JoinPoint joinPoint, Exception e) throws Exception {
        log.error("<-----------------------------------------" + e.getMessage() + " from ------------------------------->"
                + joinPoint.getSignature().toString());
        log.error("DANGER!!!!");
        throw new Exception(e.getMessage());
    }
}
