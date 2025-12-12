package com.common.auth.service;

import com.common.auth.dto.UserDto;

import java.util.UUID;

public interface UserService {
    // Create user
    UserDto createUser(UserDto userDto);

    // Get user by email
    UserDto getUserByEmail(String email);

    // Update user
    UserDto updateUser(UserDto userDto, String userId);

    // Get user by id
    UserDto getUserById(String userId);

    // Get all users
    Iterable<UserDto> getAllUsers();

    // Delete user
    void deleteUser(String userId);
}
