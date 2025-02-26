package ru.spb.tacticul.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GalleryDTO(

        String backgroundImg,

        List<AlbumDTO> albums

) {
}
