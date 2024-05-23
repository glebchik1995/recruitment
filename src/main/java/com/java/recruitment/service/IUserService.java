package com.java.recruitment.service;

import com.java.recruitment.service.model.user.User;

public interface IUserService {

    User save(User user);

    User create(User user);

    User update(User user);

    User getByUsername(String username);

    User getById(Long id);

    void delete(Long id);

    User getCurrentUser();
}
