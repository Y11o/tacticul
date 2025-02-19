package ru.spb.tacticul.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.spb.tacticul.dto.AlbumDTO;
import ru.spb.tacticul.dto.MediaDTO;
import ru.spb.tacticul.service.MediaService;

import java.util.List;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
@Tag(name = "Фото", description = "API для управления фото")
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/save")
    @Operation(summary = "Сохранить изображение")
    public ResponseEntity<MediaDTO> saveImage(@RequestParam("file") MultipartFile file){
        return ResponseEntity.status(HttpStatus.CREATED).body(mediaService.save(file));
    }

    @GetMapping
    @Operation(summary = "Получить все изображения")
    public ResponseEntity<List<MediaDTO>> getAll() {
        return ResponseEntity.ok(mediaService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить изображение по ID")
    public ResponseEntity<MediaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mediaService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить изображение по ID")
    public ResponseEntity<MediaDTO> update(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(mediaService.update(id, file));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить изображение по ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mediaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
