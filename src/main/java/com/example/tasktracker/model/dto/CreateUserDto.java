package com.example.tasktracker.model.dto;

import com.example.tasktracker.model.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Выберите роль для пользователя")
    private UserRole userRole;

}
