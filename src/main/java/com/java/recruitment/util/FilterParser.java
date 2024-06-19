package com.java.recruitment.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.filter.JoinType;
import com.java.recruitment.service.model.user.User;
import lombok.experimental.UtilityClass;
import org.apache.coyote.BadRequestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.java.recruitment.service.filter.Operation.EQUALS;

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

    public List<CriteriaModel> buildCriteriaList(
            final User user,
            final String criteriaJson
    ) {
        List<CriteriaModel> criteriaList = new ArrayList<>();

        switch (user.getRole()) {
            case RECRUITER -> criteriaList.add(
                    CriteriaModel.builder()
                            .field("recruiter.id")
                            .operation(EQUALS)
                            .value(user.getId())
                            .joinType(JoinType.OR)
                            .build()
            );
            case HR -> criteriaList.add(
                    CriteriaModel.builder()
                            .field("hr.id")
                            .operation(EQUALS)
                            .value(user.getId())
                            .joinType(JoinType.OR)
                            .build()
            );
        }

        if (criteriaJson != null) {
            try {
                if (!criteriaJson.isEmpty()) {
                    criteriaList.getFirst().setJoinType(JoinType.AND);
                }
                criteriaList.addAll(FilterParser.parseCriteriaJson(criteriaJson));
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }

        return criteriaList;
    }
}