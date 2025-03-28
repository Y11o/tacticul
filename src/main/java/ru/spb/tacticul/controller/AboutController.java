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
import ru.spb.tacticul.dto.AboutDTO;
import ru.spb.tacticul.service.AboutService;

import java.util.List;

@RestController
@RequestMapping("/about")
@RequiredArgsConstructor
@Tag(name = "О нас", description = "API для управления разделом 'О нас'")
public class AboutController {
    private final AboutService aboutService;

    @GetMapping
    @Operation(summary = "Получить запись 'О нас'")
    public ResponseEntity<AboutDTO> getAll() {
        return ResponseEntity.ok(aboutService.getAll());
    }

    @PostMapping
    @Operation(summary = "Создать новую запись 'О нас'")
    public ResponseEntity<AboutDTO> create(@Valid @RequestBody AboutDTO aboutDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aboutService.create(aboutDTO));
    }

    @PutMapping
    @Operation(summary = "Обновить запись 'О нас'")
    public ResponseEntity<AboutDTO> update(@Valid @RequestBody AboutDTO aboutDTO) {
        return ResponseEntity.ok(aboutService.update(aboutDTO));
    }

    @DeleteMapping
    @Operation(summary = "Удалить запись 'О нас'")
    public ResponseEntity<Void> delete() {
        aboutService.delete();
        return ResponseEntity.noContent().build();
    }
}


