package com.example.user_poc.service;

import com.example.user_poc.dto.CreateUserDTO;
import com.example.user_poc.dto.UpdateUserDTO;
import com.example.user_poc.entity.User;
import com.example.user_poc.enums.Action;
import com.example.user_poc.enums.Entity;
import com.example.user_poc.helper.LoggingHelper;
import com.example.user_poc.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LoggingHelper loggingHelper;

    public UserService(UserRepository userRepository, LoggingHelper loggingHelper) {
        this.userRepository = userRepository;
        this.loggingHelper = loggingHelper;
    }

    public User createUser(CreateUserDTO userDTO) {
        loggingHelper.logUserAction(Action.CREATE.name(), Entity.USER.name(), null);
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setDob(userDTO.getDob());
        return userRepository.save(user);
    }

    public User updateUser(Long id, UpdateUserDTO updateUserDTO) {
        String attributes = null;
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if(updateUserDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(updateUserDTO.getPhoneNumber());
            attributes = "phone number";
        }

        if(updateUserDTO.getEmail() != null) {
            user.setEmail(updateUserDTO.getEmail());
            attributes += "email";
        }

        loggingHelper.logUserAction(Action.UPDATE.name(), Entity.USER.name(), attributes);

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }
}
