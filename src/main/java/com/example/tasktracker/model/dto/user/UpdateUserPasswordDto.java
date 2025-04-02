package com.example.tasktracker.model.dto.user;

import jakarta.validation.constraints.NotBlank;
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
public class UpdateUserPasswordDto {
    @NotBlank(message = "Укажите текущий пароль")
    private String currentPassword;

    @NotBlank(message = "Укажите новый пароль")
    @Size(min = 8, message = "Минимальный размер пароля - 8 символов")
    private String newPassword;

    @NotBlank(message = "Повторите новый пароль")
    private String newPasswordConfirmation;
}
