package ru.spb.tacticul.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spb.tacticul.dto.ContactDTO;
import ru.spb.tacticul.service.ContactService;

import java.util.List;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
@Tag(name = "Контакты", description = "API для управления контактами")
public class ContactController {
    private final ContactService contactService;

    @GetMapping
    @Operation(summary = "Получить все контакты")
    public ResponseEntity<List<ContactDTO>> getAll() {
        return ResponseEntity.ok(contactService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить контакт по ID")
    public ResponseEntity<ContactDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Создать новый контакт")
    public ResponseEntity<ContactDTO> create(@Valid @RequestBody ContactDTO contactDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.create(contactDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить контакт по ID")
    public ResponseEntity<ContactDTO> update(@PathVariable Long id, @Valid @RequestBody ContactDTO contactDTO) {
        return ResponseEntity.ok(contactService.update(id, contactDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить контакт по ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}