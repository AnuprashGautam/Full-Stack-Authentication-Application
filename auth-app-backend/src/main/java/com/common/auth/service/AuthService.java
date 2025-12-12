package com.common.auth.service;

import com.common.auth.dto.UserDto;

public interface AuthService {
    UserDto registerUser(UserDto userDto);
}
