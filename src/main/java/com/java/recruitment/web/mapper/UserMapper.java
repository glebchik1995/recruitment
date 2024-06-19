package com.java.recruitment.web.mapper;

import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordConfirmation", ignore = true)
    @Mapping(target = "jobRequestsByHr", ignore = true)
    @Mapping(target = "jobRequestsByRecruiter", ignore = true)
    @Mapping(target = "vacancies", ignore = true)
    @Mapping(target = "sentMessages", ignore = true)
    @Mapping(target = "receivedMessages", ignore = true)
    @Mapping(target = "candidates", ignore = true)
    User toEntity(UserDTO userDTO);

    UserDTO toDto(User user);
}
