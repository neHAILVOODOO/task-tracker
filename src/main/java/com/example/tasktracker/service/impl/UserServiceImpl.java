package com.example.tasktracker.service.impl;

import com.example.tasktracker.exception.ConflictException;
import com.example.tasktracker.exception.NotFoundException;

import com.example.tasktracker.mapper.UserMapper;
import com.example.tasktracker.model.dto.user.CreateUserDto;
import com.example.tasktracker.model.dto.user.GetUserDto;
import com.example.tasktracker.model.dto.user.UpdateUserBioDto;
import com.example.tasktracker.model.dto.user.UpdateUserEmailDto;
import com.example.tasktracker.model.dto.user.UpdateUserPasswordDto;
import com.example.tasktracker.model.entity.User;
import com.example.tasktracker.repo.UserRepo;
import com.example.tasktracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public GetUserDto getUserById(long userId) {
       User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

       return userMapper.mapUserToGetUserDto(user);
    }

    @Override
    public void saveUser(CreateUserDto createUserDto) {
        User user = userMapper.mapCreateUserDtoToUser(createUserDto);

        if (userRepo.existsByEmail(createUserDto.getEmail())) {
            throw new ConflictException("Email уже занят");
        }

        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));

        userRepo.save(user);
    }

    @Override
    public void updateUserPassword(UpdateUserPasswordDto updateUserPasswordDto, long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (!passwordEncoder.matches(updateUserPasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new ConflictException("Неверный текущий пароль");
        }

        if (passwordEncoder.matches(updateUserPasswordDto.getNewPassword(), user.getPassword())) {
            throw new ConflictException("Новый пароль должен отличаться от текущего");
        }

        user.setPassword(passwordEncoder.encode(updateUserPasswordDto.getNewPassword()));
        userRepo.save(user);

    }

    @Override
    public void updateUserEmail(UpdateUserEmailDto updateUserEmailDto, long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (!passwordEncoder.matches(updateUserEmailDto.getCurrentPassword(), user.getPassword())) {
            throw new ConflictException("Неверный текущий пароль");
        }

        if (userRepo.existsByEmail(updateUserEmailDto.getNewEmail())) {
            throw new ConflictException("Email уже занят");
        }

        user.setEmail(updateUserEmailDto.getNewEmail());
        userRepo.save(user);

    }

    @Override
    public void updateUserBio(UpdateUserBioDto updateUserBioDto, long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (updateUserBioDto == null) {
            throw new ConflictException("Нет полей для изменения");
        }

        BeanUtils.copyProperties(updateUserBioDto, user);

        userRepo.save(user);
    }


}
