package ru.spb.tacticul.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spb.tacticul.dto.EventDTO;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.mapper.EventMapper;
import ru.spb.tacticul.mapper.MediaMapper;
import ru.spb.tacticul.model.Event;
import ru.spb.tacticul.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final MediaMapper mediaMapper;

    public List<EventDTO> getAll() {
        log.info("Получение всех событий");
        return eventRepository.findAll().stream()
                .map(eventMapper::eventToEventDTO)
                .collect(Collectors.toList());
    }

    public EventDTO getById(Long id) {
        log.info("Поиск события по ID: {}", id);
        return eventRepository.findById(id)
                .map(eventMapper::eventToEventDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Событие", id));
    }

    @Transactional
    public EventDTO create(EventDTO eventDTO) {
        log.info("Создание нового события: {}", eventDTO.name());
        Event event = eventMapper.eventDTOtoEvent(eventDTO);
        event = eventRepository.save(event);
        log.info("Событие с ID {} успешно создано", event.getId());
        return eventMapper.eventToEventDTO(event);
    }

    @Transactional
    public EventDTO update(Long id, EventDTO eventDTO) {
        log.info("Обновление события с ID: {}", id);
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    if (eventDTO.name() != null) {
                        existingEvent.setName(eventDTO.name());
                    }
                    if (eventDTO.shortDescription() != null) {
                        existingEvent.setShortDescription(eventDTO.shortDescription());
                    }
                    if (eventDTO.longDescription() != null) {
                        existingEvent.setLongDescription(eventDTO.longDescription());
                    }
                    if (eventDTO.logo() != null) {
                        existingEvent.setLogo(mediaMapper.mediaDTOToMedia(eventDTO.logo()));
                    }
                    if (eventDTO.img() != null) {
                        existingEvent.setImg(mediaMapper.mediaDTOToMedia(eventDTO.img()));
                    }
                    if (eventDTO.position() != null) {
                        existingEvent.setPosition(eventDTO.position());
                    }

                    eventRepository.save(existingEvent);
                    log.info("Событие с ID {} успешно обновлено", id);
                    return eventMapper.eventToEventDTO(existingEvent);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Событие", id));
    }

    @Transactional
    public void delete(Long id) {
        log.info("Удаление события с ID: {}", id);
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Событие", id);
        }
        eventRepository.deleteById(id);
        log.info("Событие с ID {} успешно удалено", id);
    }
}
