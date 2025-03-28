package ru.spb.tacticul.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserDTO(

        Long id,

        @NotBlank(message = "Логин не может быть пустым")
        @Size(min = 3, max = 50, message = "Логин должен содержать от 3 до 50 символов")
        String login,

        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Некорректный формат email")
        String email,

        String password) {
}
