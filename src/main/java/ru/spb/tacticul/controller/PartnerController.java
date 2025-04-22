package ru.spb.tacticul.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spb.tacticul.dto.PartnerDTO;
import ru.spb.tacticul.service.PartnerService;

import java.util.List;

@RestController
@RequestMapping("/partner")
@RequiredArgsConstructor
@Tag(name = "Партнеры", description = "API для управления партнерами")
public class PartnerController {
    private final PartnerService partnerService;

    @GetMapping
    @Operation(summary = "Получить всех партнеров")
    public ResponseEntity<List<PartnerDTO>> getAll() {
        return ResponseEntity.ok(partnerService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить партнера по ID")
    public ResponseEntity<PartnerDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(partnerService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Создать нового партнера")
    public ResponseEntity<PartnerDTO> create(@Valid @RequestBody PartnerDTO partnerDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partnerService.create(partnerDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить партнера по ID")
    public ResponseEntity<PartnerDTO> update(@PathVariable Long id, @Valid @RequestBody PartnerDTO partnerDTO) {
        return ResponseEntity.ok(partnerService.update(id, partnerDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить партнера по ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        partnerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}