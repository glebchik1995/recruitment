package com.java.recruitment.util;

import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

/**
 * Этот класс представляет собой утилитарный помощник для работы с null-свойствами в Java объектах.
 * Он содержит методы для обнаружения и копирования ненулевых свойств между объектами.
 */
@UtilityClass
public class NullPropertyCopyHelper {
    /**
     * Получает имена свойств с null-значениями из указанного объекта.
     *
     * @param source объект для анализа
     * @return массив имен свойств с null-значениями
     */
    public String[] getNullPropertyNames(final Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * Копирует ненулевые свойства из исходного объекта в целевой объект.
     *
     * @param src исходный объект
     * @param target целевой объект
     */
    public void copyNonNullProperties(final Object src, final Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }
}
