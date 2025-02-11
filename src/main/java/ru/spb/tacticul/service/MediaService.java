package ru.spb.tacticul.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.spb.tacticul.dto.MediaDTO;
import ru.spb.tacticul.model.Media;
import ru.spb.tacticul.repository.MediaRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaService {

    private final MediaRepository mediaRepository;

    @Value("${photo_size}")
    private int PHOTO_MAX_SIZE;

    @Value("${upload_dir}")
    private String UPLOAD_DIR;

    @Value("${dev_ip}")
    private String IP;

    @Value("${dev_port}")
    private String PORT;

    public MediaDTO saveImage(MultipartFile file){
        if (file == null || file.isEmpty()){
            throw new IllegalArgumentException("Файл не может быть пустым.");
        }

        String contentType = file.getContentType();
        List<String> allowedTypes = Arrays.asList("image/jpeg", "image/png");

        if (!allowedTypes.contains(contentType)) {
            throw new IllegalArgumentException("Тип файла " + contentType + " не поддерживается. Возможные типы: " + String.join(", ", allowedTypes));
        }

        if (file.getSize() > PHOTO_MAX_SIZE) {
            throw new IllegalArgumentException(String.format("Размер файла не должен превышать %s МB.", PHOTO_MAX_SIZE / (1024 * 1024)));
        }

        Path dirPath = Paths.get(UPLOAD_DIR);
        File dir = dirPath.toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        Path filePath = dirPath.resolve(file.getOriginalFilename());
        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream = Files.newOutputStream(filePath)) {
            byte[] buffer = new byte[PHOTO_MAX_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        Media media = mediaRepository.save(Media.builder()
                .fileName(String.format("http://%s:%s/uploads/%s", IP, PORT, file.getOriginalFilename()))
                .build());

        return MediaDTO.builder()
                .id(media.getId())
                .url(media.getFileName())
                .build();
    }

}
