package com.java.recruitment.service.filter;

import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.java.recruitment.service.filter.Operation.IS_NOT_NULL;
import static com.java.recruitment.service.filter.Operation.IS_NULL;

public class GenericSpecification<T> implements Specification<T> {
    private static final Logger logger = LoggerFactory.getLogger(GenericSpecification.class);

    private static final EnumSet<Operation> NULL_OPERATIONS = EnumSet.of(IS_NULL, IS_NOT_NULL);
    private static final Set<String> PRIMITIVE_NUMBERS = Set.of("byte", "short", "int", "long", "float", "double");

    private final List<CriteriaModel> criteriaModelList;
    private final Class<T> entityClass;

    public GenericSpecification(List<CriteriaModel> criteriaModelList, Class<T> entityClass) {
        this.criteriaModelList = criteriaModelList;
        this.entityClass = entityClass;
        checkCriteria(criteriaModelList);
    }

    @Override
    public Predicate toPredicate(
            @NotNull Root<T> root,
            @NotNull CriteriaQuery<?> query,
            @NotNull CriteriaBuilder cb
    ) {
        List<Predicate> predicates = new ArrayList<>();
        for (CriteriaModel criteria : criteriaModelList) {
            predicates.add(createPredicate(criteria, root, cb));
        }

        if (predicates.isEmpty()) {
            return cb.conjunction();
        } else if (predicates.size() == 1) {
            return predicates.getFirst();
        } else {
            JoinType joinType = JoinType.AND;
            for (CriteriaModel criteria : criteriaModelList) {
                if (criteria.getJoinType() != null) {
                    joinType = criteria.getJoinType();
                    break;
                }
            }

            Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
            return joinType == JoinType.AND ? cb.and(predicateArray) : cb.or(predicateArray);
        }
    }

    private void checkCriteria(List<CriteriaModel> criteriaModel) {
        for (CriteriaModel model : criteriaModel) {
            if (model == null) {
                logger.error("CriteriaModel не должно быть нулевым.");
                throw new IllegalArgumentException("CriteriaModel не должно быть нулевым.");
            }
            if (StringUtils.isBlank(model.getField())) {
                logger.error("Поле не должно быть пустым.");
                throw new IllegalArgumentException("Поле не должно быть пустым.");
            }
            Operation operation = model.getOperation();
            if (operation == null) {
                logger.error("Операция не должна быть нулевой.");
                throw new IllegalArgumentException("Операция не должна быть нулевой.");
            }
            if (!NULL_OPERATIONS.contains(operation) && model.getValue() == null) {
                logger.error("Значение не должно быть нулевым");
                throw new IllegalArgumentException("Значение не должно быть нулевым");
            }
        }
    }

    private Predicate createPredicate(CriteriaModel criteria, Root<T> root, CriteriaBuilder cb) {
        Operation operation = criteria.getOperation();
        String fieldName = criteria.getField();
        Path<Object> expression = getExpression(root, fieldName);
        Object value = criteria.getValue();

        switch (operation) {
            case IS_NULL -> {
                return cb.isNull(expression);
            }
            case IS_NOT_NULL -> {
                return cb.isNotNull(expression);
            }
            case EQUALS -> {
                return cb.equal(expression, value);
            }
            case NOT_EQUALS -> {
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
                } else if (isDate(fieldName)) {
                    return cb.greaterThan(expression.as(LocalDateTime.class), toDate(value));
                }
            }
            case GREATER_THAN_OR_EQUALS -> {
                if (isNumber(fieldName)) {
                    return cb.ge(expression.as(BigDecimal.class), new BigDecimal(String.valueOf(value)));
                } else if (isDate(fieldName)) {
                    return cb.greaterThanOrEqualTo(expression.as(LocalDateTime.class), toDate(value));
                }
            }
            case LESS_THAN -> {
                if (isNumber(fieldName)) {
                    return cb.lt(expression.as(BigDecimal.class), new BigDecimal(String.valueOf(value)));
                } else if (isDate(fieldName)) {
                    return cb.lessThan(expression.as(LocalDateTime.class), toDate(value));
                }
            }
            case LESS_THAN_OR_EQUALS -> {
                if (isNumber(fieldName)) {
                    return cb.le(expression.as(BigDecimal.class), new BigDecimal(String.valueOf(value)));
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

    private Path<Object> getExpression(Root<T> root, String fieldName) {
        if (fieldName.contains(".")) {
            String[] fieldParts = fieldName.split("\\.");
            Join<Object, Object> join = root.join(fieldParts[0], jakarta.persistence.criteria.JoinType.INNER);
            return join.get(fieldParts[1]);
        } else {
            return root.get(fieldName);
        }
    }

    private LocalDateTime toDate(Object value) {
        return switch (value) {
            case LocalDateTime localDateTime -> localDateTime;
            case LocalDate date -> LocalDateTime.of(date, LocalTime.MIN);
            case Date date -> LocalDateTime.ofEpochSecond(date.getTime(), 0, ZoneOffset.UTC);
            case String s -> LocalDateTime.parse(s);
            case null, default -> {
                logger.error("Неподдерживаемое значение даты: {}", value);
                throw new RuntimeException("Неподдерживаемое значение даты: " + value);
            }
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
            Field field = getFieldByFieldName(fieldName);
            Class<?> fieldType = field.getType();
            if (fieldType.isAssignableFrom(Collection.class)) {
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                return (Class<?>) genericType.getActualTypeArguments()[0];
            } else {
                return fieldType;
            }
        } catch (NoSuchFieldException e) {
            logger.error("Ошибка получения типа поля: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Field getFieldByFieldName(String fieldName) throws NoSuchFieldException {
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