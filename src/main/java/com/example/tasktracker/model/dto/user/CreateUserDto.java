package com.example.tasktracker.model.dto.user;

import com.example.tasktracker.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Email(message = "Должен быть валидный email")
    private String email;
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, message = "Минимальный размер пароля - 8 символов")
    private String password;
    @NotNull(message = "Выберите роль для пользователя")
    private UserRole userRole;

}
