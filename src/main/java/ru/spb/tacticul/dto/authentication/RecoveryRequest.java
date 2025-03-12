package ru.spb.tacticul.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RecoveryRequest(

        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Некорректный email")
        String email) {
}
