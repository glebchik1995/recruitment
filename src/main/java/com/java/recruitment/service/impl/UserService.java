package com.java.recruitment.service.impl;

import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.exception.DataAlreadyExistException;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.IUserService;
import com.java.recruitment.service.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }


    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    @Override
    @Transactional
    public User create(User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DataAlreadyExistException("Пользователь с таким именем уже существует");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DataAlreadyExistException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    @Override
    @Transactional
    public User update(final User user) {
        User existingUser = getById(user.getId());
        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        return userRepository.save(existingUser);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public User getByUsername(final String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
    }

    @Override
    public User getById(final Long id) {
        return userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
    }

    @Override
    @Transactional
    public void delete(final Long id) {
        userRepository.deleteById(id);
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
}
