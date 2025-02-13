package ru.spb.tacticul.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.spb.tacticul.dto.AlbumDTO;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.mapper.AlbumMapper;
import ru.spb.tacticul.mapper.MediaMapper;
import ru.spb.tacticul.model.Album;
import ru.spb.tacticul.repository.AlbumRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceTest {
    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private AlbumMapper albumMapper;

    @Mock
    private MediaMapper mediaMapper;

    @InjectMocks
    private AlbumService albumService;

    private Album album;
    private AlbumDTO albumDTO;

    @BeforeEach
    void setUp() {
        album = new Album();
        album.setId(1L);
        album.setName("Test Album");
        album.setDescription("Test Description");

        albumDTO = new AlbumDTO(1L, "Test Album", "Test Description", null, null, "https://album.com");
    }

    @Test
    void getAll_ShouldReturnListOfAlbumDTOs() {
        when(albumRepository.findAll()).thenReturn(List.of(album));
        when(albumMapper.albumToAlbumDTO(album)).thenReturn(albumDTO);

        List<AlbumDTO> result = albumService.getAll();

        assertEquals(1, result.size());
        assertEquals("Test Album", result.get(0).name());
        verify(albumRepository, times(1)).findAll();
    }

    @Test
    void getById_ExistingId_ShouldReturnAlbumDTO() {
        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
        when(albumMapper.albumToAlbumDTO(album)).thenReturn(albumDTO);

        AlbumDTO result = albumService.getById(1L);

        assertEquals("Test Album", result.name());
        verify(albumRepository, times(1)).findById(1L);
    }

    @Test
    void getById_NonExistingId_ShouldThrowException() {
        when(albumRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> albumService.getById(2L));
    }

    @Test
    void create_ShouldReturnCreatedAlbumDTO() {
        when(albumMapper.albumDTOtoAlbum(albumDTO)).thenReturn(album);
        when(albumRepository.save(album)).thenReturn(album);
        when(albumMapper.albumToAlbumDTO(album)).thenReturn(albumDTO);

        AlbumDTO result = albumService.create(albumDTO);

        assertEquals("Test Album", result.name());
        verify(albumRepository, times(1)).save(album);
    }

    @Test
    void update_ExistingId_ShouldUpdateAndReturnDTO() {
        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
        when(albumRepository.save(any())).thenReturn(album);
        when(albumMapper.albumToAlbumDTO(any())).thenReturn(albumDTO);

        AlbumDTO result = albumService.update(1L, albumDTO);

        assertEquals("Test Album", result.name());
        verify(albumRepository, times(1)).save(album);
    }

    @Test
    void update_NonExistingId_ShouldThrowException() {
        when(albumRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> albumService.update(2L, albumDTO));
    }

    @Test
    void delete_ExistingId_ShouldDeleteSuccessfully() {
        when(albumRepository.existsById(1L)).thenReturn(true);
        doNothing().when(albumRepository).deleteById(1L);

        assertDoesNotThrow(() -> albumService.delete(1L));
        verify(albumRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_NonExistingId_ShouldThrowException() {
        when(albumRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> albumService.delete(2L));
    }
}
