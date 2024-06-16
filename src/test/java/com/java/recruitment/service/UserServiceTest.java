package com.java.recruitment.service;

import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.impl.UserService;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.web.dto.user.UserDTO;
import com.java.recruitment.web.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

//    @Test
//    void getById() {
//        Long id = 1L;
//        UserDTO user = new UserDTO();
//        user.setId(id);
//        when(userRepository.findById(id))
//                .thenReturn(Optional.of(user));
//        UserDTO testUser = userService.getById(id);
//        verify(userRepository).findById(id);
//        assertEquals(user, testUser);
//    }

    @Test
    void getByNotExistingId() {
        Long id = 1L;
        when(userRepository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class,
                () -> userService.getById(id));
        verify(userRepository).findById(id);
    }

    @Test
    void getByUsername() {
        String username = "username@gmail.com";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));
        User testUser = userService.getByUsername(username);
        verify(userRepository).findByUsername(username);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByNotExistingUsername() {
        String username = "username@gmail.com";
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class,
                () -> userService.getByUsername(username));
        verify(userRepository).findByUsername(username);
    }

//    @Test
//    void update() {
//        Long id = 1L;
//        String password = "password";
//        User user = new User();
//        user.setId(id);
//        user.setPassword(password);
//        when(passwordEncoder.encode(password))
//                .thenReturn("encodedPassword");
//        when(userRepository.findById(user.getId()))
//                .thenReturn(Optional.of(user));
//        User updated = userService.update(user);
//        verify(passwordEncoder).encode(password);
//        verify(userRepository).save(user);
//        Assertions.assertEquals(user.getUsername(), updated.getUsername());
//        Assertions.assertEquals(user.getName(), updated.getName());
//    }

    @Test
    void testCreateUser_Success() {
        UserDTO userDTO = UserDTO.builder()
                .name("name")
                .username("username@mail.com")
                .password("password")
                .passwordConfirmation("password")
                .build();

        User user = User.builder()
                .name("name")
                .username("username@mail.com")
                .password("password")
                .passwordConfirmation("password")
                .build();

        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());
        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO createdUser = userService.create(userDTO);

        assertNotNull(createdUser);
        assertEquals("username", createdUser.getUsername());
        assertEquals("encodedPassword", createdUser.getPassword());
    }


//    @Test
//    void createWithExistingUsername() {
//        String username = "username@gmail.com";
//        String password = "password";
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(password);
//        user.setPasswordConfirmation(password);
//        when(userRepository.findByUsername(username))
//                .thenReturn(Optional.of(new User()));
//        when(passwordEncoder.encode(password))
//                .thenReturn("encodedPassword");
//        Assertions.assertThrows(IllegalStateException.class,
//                () -> userService.create(user));
//        verify(userRepository, never()).save(user);
//    }
//
//    @Test
//    void createWithDifferentPasswords() {
//        String username = "username@gmail.com";
//        String password = "password1";
//        String passwordConfirmation = "password2";
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(password);
//        user.setPasswordConfirmation(passwordConfirmation);
//        when(userRepository.findByUsername(username))
//                .thenReturn(Optional.empty());
//        Assertions.assertThrows(IllegalStateException.class,
//                () -> userService.create(user));
//        verify(userRepository, never()).save(user);
//    }
//
//    @Test
//    void isTaskOwnerWithFalse() {
//        Long userId = 1L;
//        Long taskId = 1L;
//        when(userRepository.isTaskOwner(userId, taskId))
//                .thenReturn(false);
//        boolean isOwner = userService.isTaskOwner(userId, taskId);
//        verify(userRepository).isTaskOwner(userId, taskId);
//        Assertions.assertFalse(isOwner);
//    }
//
//    @Test
//    void getTaskAuthor() {
//        Long taskId = 1L;
//        Long userId = 1L;
//        User user = new User();
//        user.setId(userId);
//        when(userRepository.findTaskAuthor(taskId))
//                .thenReturn(Optional.of(user));
//        User author = userService.getTaskAuthor(taskId);
//        verify(userRepository).findTaskAuthor(taskId);
//        Assertions.assertEquals(user, author);
//    }
//
//    @Test
//    void getNotExistingTaskAuthor() {
//        Long taskId = 1L;
//        when(userRepository.findTaskAuthor(taskId))
//                .thenReturn(Optional.empty());
//        Assertions.assertThrows(ResourceNotFoundException.class, () ->
//                userService.getTaskAuthor(taskId));
//        verify(userRepository).findTaskAuthor(taskId);
//    }

    @Test
    void delete() {
        Long id = 1L;
        userService.delete(id);
        verify(userRepository).deleteById(id);
    }


}
