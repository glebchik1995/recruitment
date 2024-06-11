package com.java.recruitment.service;

import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.user.ShortUserDTO;
import com.java.recruitment.web.dto.user.UserDTO;

public interface IUserService {

    UserDTO create(UserDTO user);

    User getByUsername(String username);

    UserDTO updateWithRoleSimpleUser(ShortUserDTO user);

    UserDTO updateWithRoleAdmin(UserDTO user);

    UserDTO getById(Long id);

    void delete(Long id);

    boolean isJobRequestOwner(
            Long userId,
            Long taskId
    );
}
