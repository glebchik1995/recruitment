package com.java.recruitment.web.mapper.impl;

import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.user.UserDTO;
import com.java.recruitment.web.mapper.Mappable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDTO> {
}
