package com.java.recruitment.service.filter;

import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.java.recruitment.service.filter.Operation.IS_NOT_NULL;
import static com.java.recruitment.service.filter.Operation.IS_NULL;

public class GenericSpecification<T> implements Specification<T> {

    private static final EnumSet<Operation> NULL_OPERATIONS = EnumSet.of(IS_NULL, IS_NOT_NULL);
    private static final Set<String> PRIMITIVE_NUMBERS = Set.of("byte", "short", "int", "long", "float", "double");

    private final List<CriteriaModel> criteriaModelList;
    private final Class<T> entityClass;

    public GenericSpecification(
            final List<CriteriaModel> criteriaModelList,
            final Class<T> entityClass
    ) {
        this.criteriaModelList = criteriaModelList;
        this.entityClass = entityClass;
        checkCriteria(criteriaModelList);
    }

    @Override
    public Predicate toPredicate(
            @NotNull final Root<T> root,
            @NotNull final CriteriaQuery<?> query,
            @NotNull final CriteriaBuilder cb
    ) {
        List<Predicate> predicates = new ArrayList<>();
        for (CriteriaModel criteria : criteriaModelList) {
            predicates.add(createPredicate(criteria, root, cb));
        }

        if (predicates.isEmpty()) {
            return cb.conjunction();
        } else {
            Predicate finalPredicate = predicates.getFirst();
            for (int i = 1; i < predicates.size(); i++) {
                CriteriaModel criteria = criteriaModelList.get(i);
                Predicate currentPredicate = predicates.get(i);
                if (criteria.getJoinType() == JoinType.AND) {
                    finalPredicate = cb.and(finalPredicate, currentPredicate);
                } else {
                    finalPredicate = cb.or(finalPredicate, currentPredicate);
                }
            }
            return finalPredicate;
        }
    }

    private void checkCriteria(final List<CriteriaModel> criteriaModel) {
        for (CriteriaModel model : criteriaModel) {
            if (model == null) {
                throw new IllegalArgumentException("CriteriaModel не должно быть нулевым.");
            }
            if (StringUtils.isBlank(model.getField())) {
                throw new IllegalArgumentException("Поле не должно быть пустым.");
            }
            Operation operation = model.getOperation();
            if (operation == null) {
                throw new IllegalArgumentException("Операция не должна быть нулевой.");
            }
            if (!NULL_OPERATIONS.contains(operation) && model.getValue() == null) {
                throw new IllegalArgumentException("Значение не должно быть нулевым");
            }
        }
    }

    private Predicate createPredicate(
            final CriteriaModel criteria,
            final Root<T> root,
            final CriteriaBuilder cb
    ) {
        Operation operation = criteria.getOperation();
        String fieldName = criteria.getField();
        Path<Object> expression = getExpression(root, fieldName);
        Object value = criteria.getValue();

        switch (operation) {
            case IS_NULL -> {
                if (isDate(fieldName)) {
                    return cb.isNull(expression.as(LocalDateTime.class));
                } else {
                    return cb.isNull(expression);
                }
            }
            case IS_NOT_NULL -> {
                return cb.isNotNull(expression);
            }
            case EQUALS -> {
                if (isTime(fieldName)) {
                    return cb.equal(expression.as(LocalTime.class), LocalTime.parse(value.toString()));
                } else if (isDate(fieldName)) {
                    return cb.equal(expression.as(LocalDateTime.class), toDate(value));
                } else if (isBoolean(fieldName)) {
                    return cb.equal(expression, Boolean.parseBoolean(value.toString()));
                }
                return cb.equal(expression, value);
            }
            case NOT_EQUALS -> {
                if (isTime(fieldName)) {
                    return cb.notEqual(expression.as(LocalTime.class), LocalTime.parse(value.toString()));
                } else if (isDate(fieldName)) {
                    return cb.notEqual(expression.as(LocalDateTime.class), toDate(value));
                } else if (isBoolean(fieldName)) {
                    return cb.notEqual(expression, Boolean.parseBoolean(value.toString()));
                }
                return cb.notEqual(expression, value);
            }
            case LIKE -> {
                if (isString(fieldName)) {
                    String likeString = "%" + value + "%";
                    return cb.like(expression.as(String.class), likeString);
                }
            }
            case NOT_LIKE -> {
                if (isString(fieldName)) {
                    String likeString = "%" + value + "%";
                    return cb.notLike(expression.as(String.class), likeString);
                }
            }
            case GREATER_THAN -> {
                if (isNumber(fieldName)) {
                    return cb.gt(expression.as(BigDecimal.class), new BigDecimal(String.valueOf(value)));
                } else if (isTime(fieldName)) {
                    return cb.greaterThan(expression.as(LocalTime.class), toTime(value));
                } else if (isDate(fieldName)) {
                    return cb.greaterThan(expression.as(LocalDateTime.class), toDate(value));
                }
            }
            case GREATER_THAN_OR_EQUALS -> {
                if (isNumber(fieldName)) {
                    return cb.ge(expression.as(BigDecimal.class), new BigDecimal(String.valueOf(value)));
                } else if (isTime(fieldName)) {
                    return cb.greaterThanOrEqualTo(expression.as(LocalTime.class), toTime(value));
                } else if (isDate(fieldName)) {
                    return cb.greaterThanOrEqualTo(expression.as(LocalDateTime.class), toDate(value));
                }
            }
            case LESS_THAN -> {
                if (isNumber(fieldName)) {
                    return cb.lt(expression.as(BigDecimal.class), new BigDecimal(String.valueOf(value)));
                } else if (isTime(fieldName)) {
                    return cb.lessThan(expression.as(LocalTime.class), toTime(value));
                } else if (isDate(fieldName)) {
                    return cb.lessThan(expression.as(LocalDateTime.class), toDate(value));
                }
            }
            case LESS_THAN_OR_EQUALS -> {
                if (isNumber(fieldName)) {
                    return cb.le(expression.as(BigDecimal.class), new BigDecimal(String.valueOf(value)));
                } else if (isTime(fieldName)) {
                    return cb.lessThanOrEqualTo(expression.as(LocalTime.class), toTime(value));
                } else if (isDate(fieldName)) {
                    return cb.lessThanOrEqualTo(expression.as(LocalDateTime.class), toDate(value));
                }
            }
            case IN -> {
                return expression.in((List<?>) value);
            }
            case NOT_IN -> {
                return cb.not(expression.in((List<?>) value));
            }
        }
        return null;
    }

    private Path<Object> getExpression(final Root<T> root, final String fieldName) {
        if (fieldName.contains(".")) {
            String[] fieldParts = fieldName.split("\\.");
            Join<Object, Object> join = root.join(fieldParts[0], jakarta.persistence.criteria.JoinType.INNER);
            return join.get(fieldParts[1]);
        } else {
            return root.get(fieldName);
        }
    }

    private LocalDateTime toDate(final Object value) {
        return switch (value) {
            case LocalDateTime localDateTime -> localDateTime;
            case LocalDate localDate -> toLocalDateTime(localDate);
            case Date date -> toLocalDateTime(date);
            case String s -> parseStringToDate(s);
            case null, default -> throw new RuntimeException("Неподдерживаемое значение даты: " + value);
        };
    }

    private LocalTime toTime(final Object value) {
        return switch (value) {
            case LocalTime localTime -> toLocalTime(localTime);
            case String s -> parseStringToTime(s);
            case null, default -> throw new RuntimeException("Неподдерживаемое значение времени: " + value);
        };
    }

    private LocalDateTime toLocalDateTime(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.MIN);
    }

    private LocalTime toLocalTime(LocalTime localTime) {
        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        int sec = localTime.getSecond();
        return LocalTime.of(hour, minute, sec);
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private LocalTime parseStringToTime(String s) {
        try {
            return LocalTime.parse(s);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат времени: " + s);
        }
    }

        private LocalDateTime parseStringToDate(String s) {
        try {
            if (s.contains("T")) {
                return LocalDateTime.parse(s);
            } else {
                return LocalDate.parse(s).atStartOfDay();
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат даты: " + s);
        }
    }

    private boolean isDate(final String fieldName) {
        Class<?> fieldType = getFieldType(fieldName);
        if (LocalDate.class.equals(fieldType) || LocalDateTime.class.equals(fieldType)) {
            return true;
        }
        return Date.class.isAssignableFrom(fieldType);
    }

    private boolean isTime(String fieldName) {
        Class<?> fieldType = getFieldType(fieldName);
        if (LocalTime.class.equals(fieldType)) {
            return true;
        }
        return Time.class.isAssignableFrom(fieldType);
    }

    private boolean isNumber(final String fieldName) {
        Class<?> fieldType = getFieldType(fieldName);
        if (fieldType.isPrimitive()) {
            return PRIMITIVE_NUMBERS.contains(fieldType.getName());
        }
        return Number.class.isAssignableFrom(fieldType);
    }

    private boolean isString(final String fieldName) {
        Class<?> fieldType = getFieldType(fieldName);
        return fieldType.isEnum() || String.class.equals(fieldType);
    }

    private boolean isBoolean(final String fieldName) {
        Class<?> fieldType = getFieldType(fieldName);
        return fieldType.equals(Boolean.class) || fieldType.equals(boolean.class);
    }

    private Class<?> getFieldType(final String fieldName) {
        try {
            Field field = getFieldByFieldName(fieldName);
            Class<?> fieldType = field.getType();
            if (fieldType.isAssignableFrom(Collection.class)) {
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                return (Class<?>) genericType.getActualTypeArguments()[0];
            } else {
                return fieldType;
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private Field getFieldByFieldName(final String fieldName) throws NoSuchFieldException {
        if (fieldName.contains(".")) {
            String[] fieldParts = fieldName.split("\\.");
            Field field = entityClass.getDeclaredField(fieldParts[0]);
            if (Collection.class.isAssignableFrom(field.getType())) {
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                Class<?> collectionType = (Class<?>) genericType.getActualTypeArguments()[0];
                return collectionType.getDeclaredField(fieldParts[1]);
            } else {
                return field.getType().getDeclaredField(fieldParts[1]);
            }
        } else {
            return entityClass.getDeclaredField(fieldName);
        }
    }
}