package ru.spb.tacticul.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spb.tacticul.dto.AlbumDTO;
import ru.spb.tacticul.service.AlbumService;

import java.util.List;

@RestController
@RequestMapping("/album")
@RequiredArgsConstructor
@Tag(name = "Альбом", description = "API для управления альбомами")
public class AlbumController {

    private final AlbumService albumService;

    @GetMapping
    @Operation(summary = "Получить все альбомы")
    public ResponseEntity<List<AlbumDTO>> getAll() {
        return ResponseEntity.ok(albumService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить альбом по ID")
    public ResponseEntity<AlbumDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(albumService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Создать новый альбом")
    public ResponseEntity<AlbumDTO> create(@Valid @RequestBody AlbumDTO albumDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(albumService.create(albumDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить альбом по ID")
    public ResponseEntity<AlbumDTO> update(@PathVariable Long id, @Valid @RequestBody AlbumDTO albumDTO) {
        return ResponseEntity.ok(albumService.update(id, albumDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить альбом по ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        albumService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}
