package com.java.recruitment.validator.json;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CriteriaJsonValidator.class)
public @interface ValidCriteriaJson {
    String message() default "Invalid criteria JSON";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
