package com.example.user_poc.controller;

import com.example.user_poc.dto.CreateUserDTO;
import com.example.user_poc.dto.UpdateUserDTO;
import com.example.user_poc.entity.User;
import com.example.user_poc.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPhoneNumber("1234567890");
        user.setDob("2000-01-01");
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPhoneNumber("1234567890");
        userDTO.setDob("2000-01-01");

        when(userService.createUser(any(CreateUserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"phoneNumber\":\"1234567890\",\"dob\":\"2000-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService).createUser(any(CreateUserDTO.class));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setEmail("new.email@example.com");
        updateUserDTO.setPhoneNumber("0987654321");

        when(userService.updateUser(eq(1L), any(UpdateUserDTO.class))).thenReturn(user);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"new.email@example.com\",\"phoneNumber\":\"0987654321\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("1234567890"));

        verify(userService).updateUser(eq(1L), any(UpdateUserDTO.class));
    }

    @Test
    void getAllUsers_ShouldReturnUserList() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));

        verify(userService).getAllUsers();
    }
}
