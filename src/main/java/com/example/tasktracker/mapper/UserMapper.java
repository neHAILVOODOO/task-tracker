package com.example.tasktracker.mapper;

import com.example.tasktracker.model.dto.CreateUserDto;
import com.example.tasktracker.model.dto.UserPreviewDto;
import com.example.tasktracker.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapCreateUserDtoToUser(CreateUserDto createUserDto) {

        return User.builder()
                .name(createUserDto.getName())
                .email(createUserDto.getEmail())
                .password(createUserDto.getPassword())
                .build();

    }

    public UserPreviewDto mapUserToUserPreviewDto(User user) {

        return UserPreviewDto.builder()
                .name(user.getName())
                .age(user.getAge())
                .build();

    }

}
