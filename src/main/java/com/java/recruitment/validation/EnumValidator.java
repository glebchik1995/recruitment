package com.java.recruitment.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumValidator implements ConstraintValidator<EnumValid, CharSequence> {

    private Set<String> acceptedValues;

    @Override
    public void initialize(EnumValid annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String normalizedValue = value.toString().toUpperCase(); // Приводим значение к верхнему регистру
        return acceptedValues.contains(normalizedValue);
    }
}
