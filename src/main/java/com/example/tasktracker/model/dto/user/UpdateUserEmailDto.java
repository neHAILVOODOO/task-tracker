package com.example.tasktracker.model.dto.user;

import jakarta.validation.constraints.Email;
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
public class UpdateUserEmailDto {

    @NotBlank(message = "Введите новый Email")
    @Email(message = "Формат Email неправильный")
    private String newEmail;

    @NotBlank(message = "Введите текущий пароль")
    private String currentPassword;

}
