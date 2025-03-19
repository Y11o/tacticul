package ru.spb.tacticul.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.spb.tacticul.dto.EventDTO;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.mapper.EventMapper;
import ru.spb.tacticul.mapper.MediaMapper;
import ru.spb.tacticul.model.Event;
import ru.spb.tacticul.repository.EventRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private MediaMapper mediaMapper;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private EventDTO eventDTO;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setShortDescription("Short Description");
        event.setLongDescription("Long Description");
        event.setPosition("center");

        eventDTO = new EventDTO(1L, "Test Event", "Short Description",
                "Long Description", null, null, "center");
    }

    @Test
    void getAll_ShouldReturnListOfEventDTOs() {
        when(eventRepository.findAll()).thenReturn(List.of(event));
        when(eventMapper.eventToEventDTO(event)).thenReturn(eventDTO);

        List<EventDTO> result = eventService.getAll();

        assertEquals(1, result.size());
        assertEquals("Test Event", result.get(0).name());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void getById_ExistingId_ShouldReturnEventDTO() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventMapper.eventToEventDTO(event)).thenReturn(eventDTO);

        EventDTO result = eventService.getById(1L);

        assertEquals("Test Event", result.name());
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void getById_NonExistingId_ShouldThrowException() {
        when(eventRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventService.getById(2L));
    }

    @Test
    void create_ShouldReturnCreatedEventDTO() {
        when(eventMapper.eventDTOtoEvent(eventDTO)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.eventToEventDTO(event)).thenReturn(eventDTO);

        EventDTO result = eventService.create(eventDTO);

        assertEquals("Test Event", result.name());
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void update_ExistingId_ShouldUpdateAndReturnDTO() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any())).thenReturn(event);
        when(eventMapper.eventToEventDTO(any())).thenReturn(eventDTO);

        EventDTO result = eventService.update(1L, eventDTO);

        assertEquals("Test Event", result.name());
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void update_NonExistingId_ShouldThrowException() {
        when(eventRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventService.update(2L, eventDTO));
    }

    @Test
    void delete_ExistingId_ShouldDeleteSuccessfully() {
        when(eventRepository.existsById(1L)).thenReturn(true);
        doNothing().when(eventRepository).deleteById(1L);

        assertDoesNotThrow(() -> eventService.delete(1L));
        verify(eventRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_NonExistingId_ShouldThrowException() {
        when(eventRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> eventService.delete(2L));
    }
}
