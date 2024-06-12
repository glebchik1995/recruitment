package com.java.recruitment.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionLoggingAspect {

    @AfterThrowing(
            pointcut = "execution(* com.java.recruitment..*.*(..))",
            throwing = "ex"
    )
    public void logException(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        log.error(
                "Исключение, выброшенное в методе {}: {}",
                methodName,
                ex.getMessage()
        );
    }
}
