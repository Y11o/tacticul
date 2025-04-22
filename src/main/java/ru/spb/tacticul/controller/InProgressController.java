package ru.spb.tacticul.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spb.tacticul.dto.InProgressDTO;
import ru.spb.tacticul.service.InProgressService;

@RestController
@RequestMapping("/in-progress")
@RequiredArgsConstructor
@Tag(name = "В процессе", description = "API для управления сущностью InProgress")
public class InProgressController {
    private final InProgressService inProgressService;

    @GetMapping
    @Operation(summary = "Получить запись")
    public ResponseEntity<InProgressDTO> getById() {
        return ResponseEntity.ok(inProgressService.getAll());
    }

    @PostMapping
    @Operation(summary = "Создать новую запись")
    public ResponseEntity<InProgressDTO> create(@Valid @RequestBody InProgressDTO inProgressDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inProgressService.create(inProgressDTO));
    }

    @PutMapping
    @Operation(summary = "Обновить запись")
    public ResponseEntity<InProgressDTO> update(@Valid @RequestBody InProgressDTO inProgressDTO) {
        return ResponseEntity.ok(inProgressService.update(inProgressDTO));
    }

    @DeleteMapping
    @Operation(summary = "Удалить запись")
    public ResponseEntity<Void> delete() {
        inProgressService.delete();
        return ResponseEntity.noContent().build();
    }
}