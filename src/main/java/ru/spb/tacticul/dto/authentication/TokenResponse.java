package ru.spb.tacticul.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TokenResponse(

        @NotBlank(message = "Токен не должен быть пустым")
        String token) {
}
