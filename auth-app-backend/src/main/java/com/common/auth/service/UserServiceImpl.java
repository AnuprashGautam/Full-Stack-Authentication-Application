package com.common.auth.service;

import com.common.auth.dto.UserDto;
import com.common.auth.entity.Provider;
import com.common.auth.entity.User;
import com.common.auth.exception.ResourceNotFoundException;
import com.common.auth.helper.UserHelper;
import com.common.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {

        System.out.println("Create User Method was called:-");
        System.out.println(userDto);

        if(userDto.getEmail()==null || userDto.getEmail().isBlank())
        {
            throw new IllegalArgumentException("Email is required."+ userDto.getEmail());
        }

        if(userRepository.existsByEmail(userDto.getEmail()))
        {
            throw new IllegalArgumentException("User with this email already exists.");
        }

        User user = modelMapper.map(userDto, User.class);
        user.setProvider(userDto.getProvider()!=null ? userDto.getProvider() : Provider.LOCAL);
        // Assign the user role here
        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with given email id."));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, String userId) {
        UUID uuid = UserHelper.parseUUID(userId);
        User existingUser = userRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User with this id is not found."));

        if(userDto.getName()!=null) existingUser.setName(userDto.getName());
        if(userDto.getImage()!=null) existingUser.setImage(userDto.getImage());
        if(userDto.getProvider()!=null) existingUser.setProvider(userDto.getProvider());
        // TODO: Change password updation logic
        if(userDto.getPassword()!=null) existingUser.setPassword(userDto.getPassword());
        existingUser.setEnable(userDto.isEnable());
        existingUser.setUpdatedAt(Instant.now());
        User savedUser = userRepository.save(existingUser);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserById(String userId){
        UUID uuid = UserHelper.parseUUID(userId);
        User user = userRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User with this id not found."));

        return modelMapper.map(user,UserDto.class);
    }



    @Override
    @Transactional
    public Iterable<UserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    @Override
    public void deleteUser(String userId) {
        UUID uuid = UserHelper.parseUUID(userId);
        userRepository.findById(uuid).orElseThrow(()->new ResourceNotFoundException("User not found with this id."));
        userRepository.deleteById(uuid);
    }
}
