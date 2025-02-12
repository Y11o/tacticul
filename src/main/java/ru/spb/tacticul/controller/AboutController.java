package ru.spb.tacticul.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.spb.tacticul.dto.AboutDTO;
import ru.spb.tacticul.service.AboutService;

import java.util.List;

@RestController
@RequestMapping("/api/about")
@RequiredArgsConstructor
@Tag(name = "О нас", description = "API для управления разделом 'О нас'")
public class AboutController {
    private final AboutService aboutService;

    @GetMapping
    @Operation(summary = "Получить все записи 'О нас'")
    public ResponseEntity<List<AboutDTO>> getAll() {
        return ResponseEntity.ok(aboutService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить запись 'О нас' по ID")
    public ResponseEntity<AboutDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(aboutService.getById(id));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Создать новую запись 'О нас'")
    public ResponseEntity<AboutDTO> create(@Valid @RequestBody AboutDTO aboutDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aboutService.create(aboutDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Обновить запись 'О нас' по ID")
    public ResponseEntity<AboutDTO> update(@PathVariable Long id, @Valid @RequestBody AboutDTO aboutDTO) {
        return ResponseEntity.ok(aboutService.update(id, aboutDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Удалить запись 'О нас' по ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        aboutService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


