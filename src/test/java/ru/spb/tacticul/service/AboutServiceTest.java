package ru.spb.tacticul.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.spb.tacticul.dto.AboutDTO;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.mapper.AboutMapper;
import ru.spb.tacticul.mapper.MediaMapper;
import ru.spb.tacticul.model.About;
import ru.spb.tacticul.repository.AboutRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AboutServiceTest {

    @Mock
    private AboutRepository aboutRepository;

    @Mock
    private AboutMapper aboutMapper;

    @Mock
    private MediaMapper mediaMapper;

    @InjectMocks
    private AboutService aboutService;

    @Test
    void getAll_ShouldReturnListOfAboutDTO() {
        List<About> aboutList = List.of(new About(1L, "Test Name", "Test Description", null));
        List<AboutDTO> aboutDTOList = List.of(new AboutDTO(1L, "Test Name", "Test Description", null));

        when(aboutRepository.findAll()).thenReturn(aboutList);
        when(aboutMapper.aboutToAboutDTO(any())).thenReturn(aboutDTOList.get(0));

        List<AboutDTO> result = aboutService.getAll();

        assertEquals(1, result.size());
        verify(aboutRepository).findAll();
    }

    @Test
    void getById_ShouldReturnAboutDTO() {
        About about = new About(1L, "Test Name", "Test Description", null);
        AboutDTO aboutDTO = new AboutDTO(1L,"Test Name", "Test Description", null);

        when(aboutRepository.findById(1L)).thenReturn(Optional.of(about));
        when(aboutMapper.aboutToAboutDTO(about)).thenReturn(aboutDTO);

        AboutDTO result = aboutService.getById(1L);

        assertEquals("Test Name", result.name());
        verify(aboutRepository).findById(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(aboutRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> aboutService.getById(1L));
        verify(aboutRepository).findById(1L);
    }

    @Test
    void create_ShouldSaveAndReturnAboutDTO() {
        AboutDTO aboutDTO = new AboutDTO(1L,"Test Name", "Test Description", null);
        About about = new About(1L, "Test Name", "Test Description", null);

        when(aboutMapper.aboutDTOToAbout(aboutDTO)).thenReturn(about);
        when(aboutRepository.save(about)).thenReturn(about);
        when(aboutMapper.aboutToAboutDTO(about)).thenReturn(aboutDTO);

        AboutDTO result = aboutService.create(aboutDTO);

        assertEquals("Test Name", result.name());
        verify(aboutRepository).save(about);
    }

    @Test
    void update_ShouldModifyAndReturnUpdatedAboutDTO() {
        About about = new About(1L, "Old Name", "Old Description", null);
        AboutDTO updatedDTO = new AboutDTO(1L,"New Name", "New Description", null);

        when(aboutRepository.findById(1L)).thenReturn(Optional.of(about));
        when(aboutRepository.save(any(About.class))).thenReturn(about);
        when(aboutMapper.aboutToAboutDTO(any(About.class))).thenReturn(updatedDTO);

        AboutDTO result = aboutService.update(1L, updatedDTO);

        assertEquals("New Name", result.name());
        verify(aboutRepository).save(about);
    }

    @Test
    void delete_ShouldRemoveAboutEntity() {
        when(aboutRepository.existsById(1L)).thenReturn(true);

        aboutService.delete(1L);

        verify(aboutRepository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenNotFound() {
        when(aboutRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> aboutService.delete(1L));
        verify(aboutRepository).existsById(1L);
    }
}
