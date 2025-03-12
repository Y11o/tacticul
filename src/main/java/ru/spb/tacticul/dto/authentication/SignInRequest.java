package ru.spb.tacticul.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SignInRequest(

        @NotBlank(message = "Поле не может быть пустым")
        @Size(min = 3, max = 50, message = "Должно содержать от 3 до 50 символов")
        String credentials,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
        String password) {
}