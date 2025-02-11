package ru.spb.tacticul.mapper;

import org.mapstruct.Mapper;
import ru.spb.tacticul.dto.EventDTO;
import ru.spb.tacticul.model.Event;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventDTO eventToEventDTO(Event event);

    Event eventDTOtoEvent(EventDTO eventDTO);

}
