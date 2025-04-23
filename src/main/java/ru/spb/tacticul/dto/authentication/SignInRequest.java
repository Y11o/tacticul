package ru.spb.tacticul.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SignInRequest(

        @NotBlank(message = "Поле не может быть пустым")
        String credentials,

        @NotBlank(message = "Пароль не может быть пустым")
        String password) {
}