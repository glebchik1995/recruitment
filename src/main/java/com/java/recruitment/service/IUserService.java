package com.java.recruitment.service;

import com.java.recruitment.service.model.User;

public interface IUserService {

    User save(User user);

    User create(User user);

    User getByUsername(String username);

    User getCurrentUser();

    void getAdmin();
}
