package ru.spb.tacticul.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.spb.tacticul.dto.MediaDTO;
import ru.spb.tacticul.dto.SocialMediaDTO;
import ru.spb.tacticul.model.Media;
import ru.spb.tacticul.model.SocialMedia;

public class SocialMediaMapperTest {

    private final SocialMediaMapper socialMediaMapper = Mappers.getMapper(SocialMediaMapper.class);

    @Test
    void testSocialMediaToSocialMediaDTO() {
        Media media = Media.builder().id(1L).fileName("logo.png").build();
        SocialMedia socialMedia = SocialMedia.builder()
                .id(1L)
                .name("Test Social Media")
                .description("Test Description")
                .logo(media)
                .url("http://example.com")
                .build();

        SocialMediaDTO socialMediaDTO = socialMediaMapper.socialMediaToSocialMediaDTO(socialMedia);

        assertNotNull(socialMediaDTO);
        assertEquals(socialMedia.getId(), socialMediaDTO.id());
        assertEquals(socialMedia.getName(), socialMediaDTO.name());
        assertEquals(socialMedia.getDescription(), socialMediaDTO.description());
        assertNotNull(socialMediaDTO.logo());
        assertEquals(socialMedia.getUrl(), socialMediaDTO.url());
    }

    @Test
    void testSocialMediaDTOToSocialMedia() {
        MediaDTO mediaDTO = new MediaDTO(1L, "logo.png");
        SocialMediaDTO socialMediaDTO = new SocialMediaDTO(1L, "Test Social Media", "Test Description", mediaDTO, "http://example.com");

        SocialMedia socialMedia = socialMediaMapper.socialMediaDTOToSocialMedia(socialMediaDTO);

        assertNotNull(socialMedia);
        assertEquals(socialMediaDTO.id(), socialMedia.getId());
        assertEquals(socialMediaDTO.name(), socialMedia.getName());
        assertEquals(socialMediaDTO.description(), socialMedia.getDescription());
        assertNotNull(socialMedia.getLogo());
        assertEquals(socialMediaDTO.url(), socialMedia.getUrl());
    }
}