package com.java.recruitment.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumCaseValidator implements ConstraintValidator<CheckEnumCase, String> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(CheckEnumCase constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            Enum.valueOf(enumClass.asSubclass(Enum.class), value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}