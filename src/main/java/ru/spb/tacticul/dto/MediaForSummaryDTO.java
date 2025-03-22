package ru.spb.tacticul.dto;

import lombok.Builder;

@Builder
public record MediaForSummaryDTO(
        Long id,

        String url,

        String position
) {
}
