package com.java.recruitment.util;

import com.java.recruitment.repositoty.exception.DataAccessException;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class AccessChecker {

    public void checkAccess(Long id, Long otherId) {
        if (!Objects.equals(id, otherId)) {
            throw new DataAccessException("Отсутствует доступ к данному объекту");
        }
    }
}