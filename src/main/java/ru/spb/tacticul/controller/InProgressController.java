package ru.spb.tacticul.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.spb.tacticul.dto.InProgressDTO;
import ru.spb.tacticul.service.InProgressService;

import java.util.List;

@RestController
@RequestMapping("/in-progress")
@RequiredArgsConstructor
@Tag(name = "В процессе", description = "API для управления сущностью InProgress")
public class InProgressController {
    private final InProgressService inProgressService;

    @GetMapping
    @Operation(summary = "Получить все записи")
    public ResponseEntity<List<InProgressDTO>> getAll() {
        return ResponseEntity.ok(inProgressService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить запись по ID")
    public ResponseEntity<InProgressDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(inProgressService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Создать новую запись")
    public ResponseEntity<InProgressDTO> create(@Valid @RequestBody InProgressDTO inProgressDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inProgressService.create(inProgressDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить запись по ID")
    public ResponseEntity<InProgressDTO> update(@PathVariable Long id, @Valid @RequestBody InProgressDTO inProgressDTO) {
        return ResponseEntity.ok(inProgressService.update(id, inProgressDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить запись по ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inProgressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}