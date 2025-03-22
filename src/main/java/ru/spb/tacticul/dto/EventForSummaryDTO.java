package ru.spb.tacticul.dto;


import lombok.Builder;

@Builder
public record EventForSummaryDTO(
        Long id,

        String name,

        String shortDescription,

        String longDescription,

        MediaForSummaryDTO logo,

        MediaDTO img
) {
}
