package ru.spb.tacticul.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record SummaryDTO(

        List<AboutDTO> about,

        List<EventDTO> events,

        GalleryDTO gallery,

        List<PartnerDTO> partners,

        List<SocialMediaDTO> socialmedias,

        List<ContactDTO> contacts
) {
}
