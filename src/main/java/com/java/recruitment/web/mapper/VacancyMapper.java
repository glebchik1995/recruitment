package com.java.recruitment.web.mapper;

import com.java.recruitment.service.model.vacancy.Vacancy;
import com.java.recruitment.web.dto.vacancy.RequestVacancyDTO;
import com.java.recruitment.web.dto.vacancy.ResponseVacancyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VacancyMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "interview_specialist_id", source = "interviewSpecialist.id")
    @Mapping(target = "requirement", source = "requirement")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "start_working_day", source = "start_working_day")
    @Mapping(target = "end_working_day", source = "end_working_day")
    @Mapping(target = "salary", source = "salary")
    @Mapping(target = "active", source = "active")
    Vacancy toEntity(RequestVacancyDTO dto);

    ResponseVacancyDTO toDTO(Vacancy vacancy);
}
