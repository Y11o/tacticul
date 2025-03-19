package ru.spb.tacticul.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record InProgressDTO(

        Long id,

        @NotNull(message = "Доступность обязательна")
        Boolean isAvailable,

        @NotBlank(message = "Описание не должно быть пустым")
        String description) {
}