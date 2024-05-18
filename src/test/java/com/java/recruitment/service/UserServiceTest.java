package com.java.recruitment.service;

import com.java.recruitment.repositoty.UserDAO;
import com.java.recruitment.repositoty.exception.DataAlreadyExistException;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.impl.UserService;
import com.java.recruitment.service.model.user.Role;
import com.java.recruitment.service.model.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    @Test
    public void testSaveUser() {
        User user = new User("username", "email", "password", Role.INTERVIEW_SPECIALIST);
        when(userDAO.save(user)).thenReturn(user);

        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals(user, savedUser);
        verify(userDAO, times(1)).save(user);
    }

    @Test
    public void testCreateUser_Success() {
        User user = new User("username", "email", "password", Role.INTERVIEW_SPECIALIST);
        when(userDAO.existsByUsername(user.getUsername())).thenReturn(false);
        when(userDAO.existsByEmail(user.getEmail())).thenReturn(false);
        when(userDAO.save(user)).thenReturn(user);

        User createdUser = userService.create(user);

        assertNotNull(createdUser);
        assertEquals(user, createdUser);
        verify(userDAO, times(1)).save(user);
    }

    @Test
    public void testCreateUser_UserAlreadyExistsByUsername() {
        User user = new User("username", "email", "password", Role.INTERVIEW_SPECIALIST);
        when(userDAO.existsByUsername(user.getUsername())).thenReturn(true);

        assertThrows(DataAlreadyExistException.class, () -> userService.create(user));
        verify(userDAO, never()).save(user);
    }

    @Test
    public void testCreateUser_UserAlreadyExistsByEmail() {
        User user = new User("username", "email", "password", Role.INTERVIEW_SPECIALIST);
        when(userDAO.existsByUsername(user.getUsername())).thenReturn(false);
        when(userDAO.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(DataAlreadyExistException.class, () -> userService.create(user));
        verify(userDAO, never()).save(user);
    }

    @Test
    public void testGetByUsername_UserFound() {
        User user = new User("username", "email", "password", Role.INTERVIEW_SPECIALIST);
        when(userDAO.findByUsername("username")).thenReturn(Optional.of(user));

        User foundUser = userService.getByUsername("username");

        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }

    @Test
    public void testGetByUsername_UserNotFound() {
        when(userDAO.findByUsername("username")).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> userService.getByUsername("username"));
    }

    @Test
    public void testGetCurrentUser() {
        User user = new User("username", "email", "password", Role.INTERVIEW_SPECIALIST);
        when(userDAO.findByUsername("username")).thenReturn(Optional.of(user));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        User currentUser = userService.getCurrentUser();

        assertNotNull(currentUser);
        assertEquals(user, currentUser);
    }
}
