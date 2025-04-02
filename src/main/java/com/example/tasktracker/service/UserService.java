package com.example.tasktracker.service;

import com.example.tasktracker.model.dto.user.CreateUserDto;
import com.example.tasktracker.model.dto.user.GetUserDto;
import com.example.tasktracker.model.dto.user.UpdateUserBioDto;
import com.example.tasktracker.model.dto.user.UpdateUserEmailDto;
import com.example.tasktracker.model.dto.user.UpdateUserPasswordDto;

public interface UserService {

    GetUserDto getUserById(long userId);

    void saveUser(CreateUserDto userDto);

    void updateUserPassword(UpdateUserPasswordDto updateUserPasswordDto, long userId);

    void updateUserEmail(UpdateUserEmailDto updateUserEmailDto, long userId);

    void updateUserBio(UpdateUserBioDto updateUserBioDto, long userId);

}
