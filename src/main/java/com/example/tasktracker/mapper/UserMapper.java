package com.example.tasktracker.mapper;

import com.example.tasktracker.model.dto.user.CreateUserDto;
import com.example.tasktracker.model.dto.user.GetUserDto;
import com.example.tasktracker.model.dto.user.UserPreviewDto;
import com.example.tasktracker.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapCreateUserDtoToUser(CreateUserDto createUserDto) {

        return User.builder()
                .name(createUserDto.getName())
                .email(createUserDto.getEmail())
                .password(createUserDto.getPassword())
                .role(createUserDto.getUserRole())
                .build();

    }

    public UserPreviewDto mapUserToUserPreviewDto(User user) {

        return UserPreviewDto.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .build();

    }

    public GetUserDto mapUserToGetUserDto(User user) {

        return GetUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .role(user.getRole())
                .build();

    }

}
