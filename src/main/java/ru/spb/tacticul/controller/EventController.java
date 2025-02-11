package ru.spb.tacticul.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spb.tacticul.dto.EventDTO;
import ru.spb.tacticul.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
@Tag(name = "Событие", description = "API для управления событиями")
public class EventController {

    private final EventService eventService;

    @GetMapping
    @Operation(summary = "Получить все события")
    public ResponseEntity<List<EventDTO>> getAll() {
        return ResponseEntity.ok(eventService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить событие по ID")
    public ResponseEntity<EventDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Создать новое событие")
    public ResponseEntity<EventDTO> create(@Valid @RequestBody EventDTO eventDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(eventDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить событие по ID")
    public ResponseEntity<EventDTO> update(@PathVariable Long id, @Valid @RequestBody EventDTO eventDTO) {
        return ResponseEntity.ok(eventService.update(id, eventDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить событие по ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
