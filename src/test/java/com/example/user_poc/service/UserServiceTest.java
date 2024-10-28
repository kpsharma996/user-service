package com.example.user_poc.service;

import com.example.user_poc.dto.CreateUserDTO;
import com.example.user_poc.dto.UpdateUserDTO;
import com.example.user_poc.entity.User;
import com.example.user_poc.enums.Action;
import com.example.user_poc.enums.Entity;
import com.example.user_poc.helper.LoggingHelper;
import com.example.user_poc.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoggingHelper loggingHelper;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPhoneNumber("1234567890");
        user.setDob("2000-01-01");
    }

    @Test
    void createUser_ShouldSaveUserAndLogAction() {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPhoneNumber("1234567890");
        userDTO.setDob("2000-01-01");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals("John Doe", createdUser.getName());
        assertEquals("john.doe@example.com", createdUser.getEmail());
        assertEquals("1234567890", createdUser.getPhoneNumber());

        verify(loggingHelper).logUserAction(Action.CREATE.name(), Entity.USER.name(), null);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_ShouldUpdateUserAndLogAction() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setEmail("new.email@example.com");
        updateUserDTO.setPhoneNumber("0987654321");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(1L, updateUserDTO);

        assertNotNull(updatedUser);
        assertEquals("new.email@example.com", updatedUser.getEmail());
        assertEquals("0987654321", updatedUser.getPhoneNumber());

        ArgumentCaptor<String> attributesCaptor = ArgumentCaptor.forClass(String.class);
        verify(loggingHelper).logUserAction(eq(Action.UPDATE.name()), eq(Entity.USER.name()), attributesCaptor.capture());
        assertTrue(attributesCaptor.getValue().contains("phone number"));
        assertTrue(attributesCaptor.getValue().contains("email"));
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_UserNotFound_ShouldThrowException() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> userService.updateUser(1L, updateUserDTO));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(loggingHelper, never()).logUserAction(any(), any(), any());
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(user);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
        verify(userRepository).findAll();
    }
}