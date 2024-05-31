package com.java.recruitment.service.filter;

import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.java.recruitment.service.filter.Operation.NOT_NULL;
import static com.java.recruitment.service.filter.Operation.NULL;

public class GenericSpecification<T> implements Specification<T> {
    private static final EnumSet<Operation> NULL_OPERATIONS = EnumSet.of(NULL, NOT_NULL);
    private static final Set<String> PRIMITIVE_NUMBERS = Set.of("byte", "short", "int", "long", "float", "double");
    private final List<CriteriaModel> criteriaModelList;
    private final Class<T> entityClass;

    public GenericSpecification(List<CriteriaModel> criteriaModelList, Class<T> entityClass) {
        this.criteriaModelList = criteriaModelList;
        this.entityClass = entityClass;
    }

    @Override
    public Predicate toPredicate(
            @NotNull Root<T> root,
            @NotNull CriteriaQuery<?> query,
            @NotNull CriteriaBuilder criteriaBuilder
    ) {
        List<Predicate> predicates = criteriaModelList.stream()
                .map(criteria -> createPredicate(criteria, root, criteriaBuilder))
                .toList();

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private Predicate createPredicate(CriteriaModel criteria, Root<T> root, CriteriaBuilder criteriaBuilder) {
        Operation operation = criteria.getOperation();
        String fieldName = criteria.getField();
        Path<Object> expression = root.get(fieldName);
        Object value = criteria.getValue();

        switch (operation) {
            case NULL -> {
                return criteriaBuilder.isNull(expression);
            }
            case NOT_NULL -> {
                return criteriaBuilder.isNotNull(expression);
            }
            case EQ -> {
                return criteriaBuilder.equal(expression, value);
            }
            case LIKE -> {
                if (isString(fieldName)) {
                    String likeString = "%" + value + "%";
                    return criteriaBuilder.like(expression.as(String.class), likeString);
                }
            }
            case GT -> {
                if (isNumber(fieldName)) {
                    return criteriaBuilder.gt(expression.as(BigDecimal.class), new BigDecimal(String.valueOf(value)));
                } else if (isDate(fieldName)) {
                    return criteriaBuilder.greaterThan(expression.as(LocalDateTime.class), toDate(value));
                }
            }
            case LT -> {
                if (isNumber(fieldName)) {
                    return criteriaBuilder.lt(expression.as(BigDecimal.class), new BigDecimal(String.valueOf(value)));
                } else if (isDate(fieldName)) {
                    return criteriaBuilder.lessThan(expression.as(LocalDateTime.class), toDate(value));
                }
            }
        }
        return null;
    }

    public Specification<T> mergeSpecifications(List<Specification> specifications, JoinType joinType) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            specifications.forEach(specification -> predicates.add(specification.toPredicate(root, query, cb)));

            if (joinType.equals(JoinType.AND)) {
                return cb.and(predicates.toArray(new Predicate[0]));
            } else {
                return cb.or(predicates.toArray(new Predicate[0]));
            }

        };
    }

    private LocalDateTime toDate(Object value) {
        return switch (value) {
            case LocalDateTime localDateTime -> localDateTime;
            case LocalDate date -> LocalDateTime.of(date, LocalTime.MIN);
            case Date date -> LocalDateTime.ofEpochSecond(date.getTime(), 0, ZoneOffset.UTC);
            case String s -> LocalDateTime.parse(s);
            case null, default -> throw new RuntimeException("Неподдерживаемое значение даты: " + value);
        };
    }

    private boolean isNumber(String fieldName) {
        Class<?> fieldType = getFieldType(fieldName);
        if (fieldType.isPrimitive()) {
            return PRIMITIVE_NUMBERS.contains(fieldType.getName());
        }
        return Number.class.isAssignableFrom(fieldType);
    }

    private boolean isString(String fieldName) {
        Class<?> fieldType = getFieldType(fieldName);
        return fieldType.isEnum() || String.class.equals(fieldType);
    }

    private boolean isDate(String fieldName) {
        Class<?> fieldType = getFieldType(fieldName);
        if (LocalDate.class.equals(fieldType) || LocalDateTime.class.equals(fieldType)) {
            return true;
        }
        return Date.class.isAssignableFrom(fieldType);
    }

    private Class<?> getFieldType(String fieldName) {
        try {
            Field field = entityClass.getDeclaredField(fieldName);
            return field.getType();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}