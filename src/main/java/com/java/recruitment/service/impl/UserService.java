package com.java.recruitment.service.impl;

import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.IUserService;
import com.java.recruitment.service.model.user.Role;
import com.java.recruitment.service.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    @Override
    @Transactional
    public User create(final User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("Пользователь уже существует.");
        }
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            throw new IllegalStateException("Пароль и подтверждение пароля не совпадают.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = Set.of(Role.USER);
        user.setRoles(roles);
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public User update(final User user) {

        User existingUser = getById(user.getId());
        if (!ObjectUtils.isEmpty(user.getName())) {
            existingUser.setName(user.getName());
        }
        if (!ObjectUtils.isEmpty(user.getUsername())) {
            existingUser.setUsername(user.getUsername());
        }
        if (!ObjectUtils.isEmpty(user.getPassword())) {
            existingUser.setPassword(user.getPassword());
        }
        if (!ObjectUtils.isEmpty(user.getRoles())) {
            existingUser.setRoles(user.getRoles());
        }
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
}
