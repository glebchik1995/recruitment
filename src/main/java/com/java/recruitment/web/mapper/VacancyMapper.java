package com.java.recruitment.web.mapper;

import com.java.recruitment.service.model.vacancy.Vacancy;
import com.java.recruitment.web.dto.vacancy.RequestVacancyDTO;
import com.java.recruitment.web.dto.vacancy.ResponseVacancyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface VacancyMapper {

    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "recruiter", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "jobRequests", ignore = true)
    Vacancy toEntity(RequestVacancyDTO dto);

    ResponseVacancyDTO toDTO(Vacancy vacancy);
}
