package com.example.tasktracker.model.dto.user;

import com.example.tasktracker.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserDto {

    private long id;
    private String name;
    private String email;
    private String password;
    private int age;
    private UserRole role;

}
