package ru.spb.tacticul.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.spb.tacticul.dto.InProgressDTO;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.mapper.InProgressMapper;
import ru.spb.tacticul.model.InProgress;
import ru.spb.tacticul.repository.InProgressRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class InProgressServiceTest {

    @Mock
    private InProgressRepository inProgressRepository;

    @Mock
    private InProgressMapper inProgressMapper;

    @InjectMocks
    private InProgressService inProgressService;

    private InProgress inProgress;
    private InProgressDTO inProgressDTO;

    @BeforeEach
    void setUp() {
        inProgress = new InProgress();
        inProgress.setId(1L);
        inProgress.setIsAvailable(true);
        inProgress.setDescription("Test Description");

        inProgressDTO = new InProgressDTO(1L, true, "Test Description");
    }

    @Test
    @Disabled
    void getAll_ShouldReturnListOfInProgressDTOs() {
        when(inProgressRepository.findAll()).thenReturn(List.of(inProgress));
        when(inProgressMapper.inProgressToInProgressDTO(inProgress)).thenReturn(inProgressDTO);

        List<InProgressDTO> result = inProgressService.getAll();

        assertEquals(1, result.size());
        assertEquals("Test Description", result.get(0).description());
        verify(inProgressRepository, times(1)).findAll();
    }

    @Test
    @Disabled
    void getById_ExistingId_ShouldReturnInProgressDTO() {
        when(inProgressRepository.findById(1L)).thenReturn(Optional.of(inProgress));
        when(inProgressMapper.inProgressToInProgressDTO(inProgress)).thenReturn(inProgressDTO);

        InProgressDTO result = inProgressService.getById(1L);

        assertEquals("Test Description", result.description());
        verify(inProgressRepository, times(1)).findById(1L);
    }

    @Test
    @Disabled
    void getById_NonExistingId_ShouldThrowException() {
        when(inProgressRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inProgressService.getById(2L));
    }

    @Test
    @Disabled
    void create_ShouldReturnCreatedInProgressDTO() {
        when(inProgressMapper.inProgressDTOToInProgress(inProgressDTO)).thenReturn(inProgress);
        when(inProgressRepository.save(inProgress)).thenReturn(inProgress);
        when(inProgressMapper.inProgressToInProgressDTO(inProgress)).thenReturn(inProgressDTO);

        InProgressDTO result = inProgressService.create(inProgressDTO);

        assertEquals("Test Description", result.description());
        verify(inProgressRepository, times(1)).save(inProgress);
    }

    @Test
    @Disabled
    void update_ExistingId_ShouldUpdateAndReturnDTO() {
        when(inProgressRepository.findById(1L)).thenReturn(Optional.of(inProgress));
        when(inProgressRepository.save(any())).thenReturn(inProgress);
        when(inProgressMapper.inProgressToInProgressDTO(any())).thenReturn(inProgressDTO);

        InProgressDTO result = inProgressService.update(1L, inProgressDTO);

        assertEquals("Test Description", result.description());
        verify(inProgressRepository, times(1)).save(inProgress);
    }

    @Test
    @Disabled
    void update_NonExistingId_ShouldThrowException() {
        when(inProgressRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inProgressService.update(2L, inProgressDTO));
    }

    @Test
    @Disabled
    void delete_ExistingId_ShouldDeleteSuccessfully() {
        when(inProgressRepository.existsById(1L)).thenReturn(true);
        doNothing().when(inProgressRepository).deleteById(1L);

        assertDoesNotThrow(() -> inProgressService.delete(1L));
        verify(inProgressRepository, times(1)).deleteById(1L);
    }

    @Test
    @Disabled
    void delete_NonExistingId_ShouldThrowException() {
        when(inProgressRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> inProgressService.delete(2L));
    }
}

