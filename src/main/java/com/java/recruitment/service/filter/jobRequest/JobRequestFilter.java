package com.java.recruitment.service.filter.jobRequest;

import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.service.filter.JoinType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobRequestFilter {

    private List<CriteriaModel> criteriaModel;

    private JoinType joinType;

}
