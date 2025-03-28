package com.example.tasktracker.service;

import com.example.tasktracker.model.dto.CreateUserDto;

public interface UserService {

    void saveUser(CreateUserDto userDto);

}
