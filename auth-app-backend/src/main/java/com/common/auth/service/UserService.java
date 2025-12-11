package com.common.auth.service;

import com.common.auth.dto.UserDto;

import java.util.Iterator;
import java.util.UUID;

public interface UserService {
    // Create user
    UserDto createUser(UserDto userDto);

    // Get user by email
    UserDto getUserByEmail(String email);

    // Update user
    UserDto updateUser(UserDto userDto, String userId);

    // Get user by id
    UserDto getUserById(UUID id);

    // Get all users
    Iterable<UserDto> getAllUsers();
}
