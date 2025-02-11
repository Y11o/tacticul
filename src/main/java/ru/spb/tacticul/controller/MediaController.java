package ru.spb.tacticul.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.spb.tacticul.dto.MediaDTO;
import ru.spb.tacticul.service.MediaService;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
@Tag(name = "Фото", description = "API для управления фото")
public class MediaController {

    private final MediaService mediaService;

    @RequestMapping(
            path = "/save",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Сохранить изображение")
    public ResponseEntity<MediaDTO> saveImage(@RequestParam("file") MultipartFile file){
        return ResponseEntity.status(HttpStatus.CREATED).body(mediaService.saveImage(file));
    }

}
