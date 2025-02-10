package ru.spb.tacticul.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.spb.tacticul.dto.SocialMediaDTO;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.mapper.MediaMapper;
import ru.spb.tacticul.mapper.SocialMediaMapper;
import ru.spb.tacticul.model.SocialMedia;
import ru.spb.tacticul.repository.SocialMediaRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SocialMediaServiceTest {

    @Mock
    private SocialMediaRepository socialMediaRepository;

    @Mock
    private SocialMediaMapper socialMediaMapper;

    @Mock
    private MediaMapper mediaMapper;

    @InjectMocks
    private SocialMediaService socialMediaService;

    private SocialMedia socialMedia;
    private SocialMediaDTO socialMediaDTO;

    @BeforeEach
    void setUp() {
        socialMedia = new SocialMedia();
        socialMedia.setId(1L);
        socialMedia.setName("Facebook");
        socialMedia.setDescription("Social network");

        socialMediaDTO = new SocialMediaDTO(1L, "Facebook", "Social network", null, "https://facebook.com");
    }

    @Test
    void getAll_ShouldReturnListOfSocialMediaDTOs() {
        when(socialMediaRepository.findAll()).thenReturn(List.of(socialMedia));
        when(socialMediaMapper.socialMediaToSocialMediaDTO(socialMedia)).thenReturn(socialMediaDTO);

        List<SocialMediaDTO> result = socialMediaService.getAll();

        assertEquals(1, result.size());
        assertEquals("Facebook", result.get(0).name());
        verify(socialMediaRepository, times(1)).findAll();
    }

    @Test
    void getById_ExistingId_ShouldReturnSocialMediaDTO() {
        when(socialMediaRepository.findById(1L)).thenReturn(Optional.of(socialMedia));
        when(socialMediaMapper.socialMediaToSocialMediaDTO(socialMedia)).thenReturn(socialMediaDTO);

        SocialMediaDTO result = socialMediaService.getById(1L);

        assertEquals("Facebook", result.name());
        verify(socialMediaRepository, times(1)).findById(1L);
    }

    @Test
    void getById_NonExistingId_ShouldThrowException() {
        when(socialMediaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> socialMediaService.getById(2L));
    }

    @Test
    void create_ShouldReturnCreatedSocialMediaDTO() {
        when(socialMediaMapper.socialMediaDTOToSocialMedia(socialMediaDTO)).thenReturn(socialMedia);
        when(socialMediaRepository.save(socialMedia)).thenReturn(socialMedia);
        when(socialMediaMapper.socialMediaToSocialMediaDTO(socialMedia)).thenReturn(socialMediaDTO);

        SocialMediaDTO result = socialMediaService.create(socialMediaDTO);

        assertEquals("Facebook", result.name());
        verify(socialMediaRepository, times(1)).save(socialMedia);
    }

    @Test
    void update_ExistingId_ShouldUpdateAndReturnDTO() {
        when(socialMediaRepository.findById(1L)).thenReturn(Optional.of(socialMedia));
        when(socialMediaRepository.save(any())).thenReturn(socialMedia);
        when(socialMediaMapper.socialMediaToSocialMediaDTO(any())).thenReturn(socialMediaDTO);

        SocialMediaDTO result = socialMediaService.update(1L, socialMediaDTO);

        assertEquals("Facebook", result.name());
        verify(socialMediaRepository, times(1)).save(socialMedia);
    }

    @Test
    void update_NonExistingId_ShouldThrowException() {
        when(socialMediaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> socialMediaService.update(2L, socialMediaDTO));
    }

    @Test
    void delete_ExistingId_ShouldDeleteSuccessfully() {
        when(socialMediaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(socialMediaRepository).deleteById(1L);

        assertDoesNotThrow(() -> socialMediaService.delete(1L));
        verify(socialMediaRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_NonExistingId_ShouldThrowException() {
        when(socialMediaRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> socialMediaService.delete(2L));
    }
}
