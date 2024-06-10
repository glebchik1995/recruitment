package com.java.recruitment.web.mapper;

import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.user.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDTO> {
}
