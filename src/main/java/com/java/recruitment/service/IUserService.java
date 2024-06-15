package com.java.recruitment.service;

import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.user.EditUserDTO;
import com.java.recruitment.web.dto.user.UserDTO;

public interface IUserService {

    UserDTO create(UserDTO user);

    User getByUsername(String username);

    UserDTO editUser(EditUserDTO user);

    UserDTO getById(Long id);

    void delete(Long id);
}
