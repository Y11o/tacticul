package ru.spb.tacticul.mapper;

import org.mapstruct.Mapper;
import ru.spb.tacticul.dto.InProgressDTO;
import ru.spb.tacticul.model.InProgress;

@Mapper(componentModel = "spring")
public interface InProgressMapper {

    InProgressDTO inProgressToInProgressDTO(InProgress inProgress);
    InProgress inProgressDTOToInProgress(InProgressDTO inProgressDTO);
}