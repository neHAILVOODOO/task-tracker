package com.example.tasktracker.model.dto;

import jakarta.validation.constraints.NotBlank;
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
public class CreateUserDto {

    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @NotBlank(message = "Email не может быть пустым")
    private String email;
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

}
