package ru.spb.tacticul.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.spb.tacticul.dto.InProgressDTO;
import ru.spb.tacticul.model.InProgress;


public class InProgressMapperTest {

    private final InProgressMapper inProgressMapper = Mappers.getMapper(InProgressMapper.class);

    @Test
    void testInProgressToInProgressDTO() {
        InProgress inProgress = InProgress.builder()
                .id(1L)
                .isAvailable(true)
                .description("Test Description")
                .build();

        InProgressDTO inProgressDTO = inProgressMapper.inProgressToInProgressDTO(inProgress);

        assertNotNull(inProgressDTO);
        assertEquals(inProgress.getId(), inProgressDTO.id());
        assertEquals(inProgress.getIsAvailable(), inProgressDTO.isAvailable());
        assertEquals(inProgress.getDescription(), inProgressDTO.description());
    }

    @Test
    void testInProgressDTOToInProgress() {
        InProgressDTO inProgressDTO = new InProgressDTO(1L, true, "Test Description");

        InProgress inProgress = inProgressMapper.inProgressDTOToInProgress(inProgressDTO);

        assertNotNull(inProgress);
        assertEquals(inProgressDTO.id(), inProgress.getId());
        assertEquals(inProgressDTO.isAvailable(), inProgress.getIsAvailable());
        assertEquals(inProgressDTO.description(), inProgress.getDescription());
    }
}
