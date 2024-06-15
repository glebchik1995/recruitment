package com.java.recruitment.service.impl;

import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.exception.DataAlreadyExistException;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.IUserService;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.util.NullPropertyCopyHelper;
import com.java.recruitment.web.dto.user.EditUserDTO;
import com.java.recruitment.web.dto.user.UserDTO;
import com.java.recruitment.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    @Override
    @Transactional
    public UserDTO create(final UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DataAlreadyExistException("Пользователь уже существует.");
        }
//        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
//            throw new IllegalStateException("Пароль и подтверждение пароля не совпадают.");
//        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(userDTO.getRole());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDTO editUser(final EditUserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
        NullPropertyCopyHelper.copyNonNullProperties(userDTO, user);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    @Override
    public User getByUsername(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
    }

    @Override
    public UserDTO getById(final Long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден")));
    }

    @Override
    @Transactional
    public void delete(final Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
        userRepository.deleteById(user.getId());
    }
}
