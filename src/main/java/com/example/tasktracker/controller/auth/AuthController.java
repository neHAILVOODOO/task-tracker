package com.example.tasktracker.controller.auth;

import com.example.tasktracker.model.dto.user.CreateUserDto;
import com.example.tasktracker.model.dto.user.UserLoginDto;
import com.example.tasktracker.service.AuthService;
import com.example.tasktracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        String token = authService.attemptLogin(userLoginDto.getEmail(), userLoginDto.getPassword());
        return ResponseEntity.ok(token);
    }


    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody CreateUserDto createUserDto) {
        userService.saveUser(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}