package com.java.recruitment.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.recruitment.service.filter.CriteriaModel;
import lombok.experimental.UtilityClass;
import org.apache.coyote.BadRequestException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class FilterParser {

    public List<CriteriaModel> parseCriteriaJson(String criteriaJson) throws BadRequestException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return Arrays.asList(objectMapper.readValue(criteriaJson, CriteriaModel[].class));
        } catch (IOException ex) {
            throw new BadRequestException("Не удалось проанализировать условия", ex);
        }
    }
}