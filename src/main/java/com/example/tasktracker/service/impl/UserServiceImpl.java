package com.example.tasktracker.service.impl;

import com.example.tasktracker.exception.UserAlreadyExistsException;
import com.example.tasktracker.mapper.UserMapper;
import com.example.tasktracker.model.dto.CreateUserDto;
import com.example.tasktracker.model.entity.User;
import com.example.tasktracker.repo.UserRepo;
import com.example.tasktracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(CreateUserDto createUserDto) {
        User user = userMapper.mapCreateUserDtoToUser(createUserDto);

        if (userRepo.findUserByEmail(createUserDto.getEmail()) != null) {
            throw new UserAlreadyExistsException("Пользователь c таким email уже существует");
        }

        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));

        userRepo.save(user);
    }


}
