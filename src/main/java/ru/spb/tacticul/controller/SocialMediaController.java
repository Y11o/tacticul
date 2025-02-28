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
import ru.spb.tacticul.dto.SocialMediaDTO;
import ru.spb.tacticul.service.SocialMediaService;

import java.util.List;

@RestController
@RequestMapping("/social-media")
@RequiredArgsConstructor
@Tag(name = "Социальные сети", description = "API для управления социальными сетями")
public class SocialMediaController {
    private final SocialMediaService socialMediaService;

    @GetMapping
    @Operation(summary = "Получить все социальные сети")
    public ResponseEntity<List<SocialMediaDTO>> getAll() {
        return ResponseEntity.ok(socialMediaService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить социальную сеть по ID")
    public ResponseEntity<SocialMediaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(socialMediaService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Создать новую социальную сеть")
    public ResponseEntity<SocialMediaDTO> create(@Valid @RequestBody SocialMediaDTO socialMediaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(socialMediaService.create(socialMediaDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить социальную сеть по ID")
    public ResponseEntity<SocialMediaDTO> update(@PathVariable Long id, @Valid @RequestBody SocialMediaDTO socialMediaDTO) {
        return ResponseEntity.ok(socialMediaService.update(id, socialMediaDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить социальную сеть по ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        socialMediaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}