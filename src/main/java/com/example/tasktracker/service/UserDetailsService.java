package com.example.tasktracker.service;

import com.example.tasktracker.model.User;
import com.example.tasktracker.repo.UserRepo;
import com.example.tasktracker.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findUserByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }

        return UserPrincipal.builder()
                .userId(user.getId())
                .login(user.getEmail())
                .authorities(Collections.emptyList())
                .password(user.getPassword())
                .build();
    }
}