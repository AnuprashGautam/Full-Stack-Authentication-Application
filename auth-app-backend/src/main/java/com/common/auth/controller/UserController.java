package com.common.auth.controller;

import com.common.auth.dto.UserDto;
import com.common.auth.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    // Api to create new user
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    // Api to fetch all users
    @GetMapping
    public ResponseEntity<Iterable<UserDto>> getAllUsers()
    {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Api to fetch single user using email id
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email)
    {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    // Api to delete a user by its id
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable  String userId)
    {
        userService.deleteUser(userId);
    }

    // Api to update a user
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable String userId)
    {
        return ResponseEntity.ok(userService.updateUser(userDto,userId));
    }

    // Api to get the user by its id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId)
    {
        return ResponseEntity.ok(userService.getUserById(userId));
    }
}
