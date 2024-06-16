package com.java.recruitment.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("@within(com.java.recruitment.aspect.log.LogInfo)")
    public Object log(final ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();

        log.info(
                "Метод {} с параметрами {} будет выполнен",
                methodName,
                Arrays.asList(arguments)
        );

        Object returnedByMethod = joinPoint.proceed();

        log.info(
                "Метод {} выполнен и возвращен {}",
                methodName,
                returnedByMethod
        );

        return returnedByMethod;
    }
}
