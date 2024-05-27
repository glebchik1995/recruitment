package com.java.recruitment.service;

import com.java.recruitment.service.model.user.User;

public interface IUserService {

    User create(User user);

    User getByUsername(String username);

    User update(User user);

    User getById(Long id);

    void delete(Long id);

}
