package com.java.recruitment.service;

import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.user.UpdateUserDTO;
import com.java.recruitment.web.dto.user.UserDTO;

public interface IUserService {

    UserDTO create(UserDTO user);

    User getByUsername(String username);

    UserDTO editUser(UpdateUserDTO user);

    UserDTO getById(Long id);

    void delete(Long id);
}
