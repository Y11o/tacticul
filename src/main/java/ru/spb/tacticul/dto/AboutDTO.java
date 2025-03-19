package ru.spb.tacticul.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AboutDTO(

        Long id,

        @NotBlank(message = "Название не должно быть пустым")
        @Size(min = 3, max = 255, message = "Название должно содержать от 3 до 255 символов")
        String name,

        @NotBlank(message = "Описание не должно быть пустым")
        String description) {
}
