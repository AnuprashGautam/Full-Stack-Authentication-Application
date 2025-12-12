package com.common.auth.service.impl;

import com.common.auth.dto.UserDto;
import com.common.auth.service.AuthService;
import com.common.auth.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    @Override
    public UserDto registerUser(UserDto userDto) {

        // Pre user creation tasks: Email, password verification, setting the role of user.
        UserDto userDto1 = userService.createUser(userDto);

        return userDto1;
    }
}
