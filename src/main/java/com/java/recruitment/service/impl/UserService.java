package com.java.recruitment.service.impl;

import com.java.recruitment.repositoty.UserDAO;
import com.java.recruitment.repositoty.exception.DataAlreadyExistException;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.IUserService;
import com.java.recruitment.service.model.user.Role;
import com.java.recruitment.service.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserDAO userDAO;

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    @Override
    public User save(User user) {
        return userDAO.save(user);
    }


    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    @Override
    public User create(User user) {

        if (userDAO.existsByUsername(user.getUsername())) {
            throw new DataAlreadyExistException("Пользователь с таким именем уже существует");
        }

        if (userDAO.existsByEmail(user.getEmail())) {
            throw new DataAlreadyExistException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    @Override
    public User getByUsername(String username) {
        return userDAO.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));

    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    @Override
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }


    /**
     * Выдача прав администратора текущему пользователю
     * <p>
     * Нужен для демонстрации
     */
    @Deprecated
    @Override
    public void getAdmin() {
        User user = getCurrentUser();
        user.setRole(Role.HR);
        save(user);
    }
}
