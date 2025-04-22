package ru.spb.tacticul.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.spb.tacticul.dto.MediaDTO;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.mapper.MediaMapper;
import ru.spb.tacticul.model.Media;
import ru.spb.tacticul.repository.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaService {

    private final MediaRepository mediaRepository;

    private final MediaMapper mediaMapper;

    private final AlbumRepository albumRepository;

    private final AboutRepository aboutRepository;

    private final ContactRepository contactRepository;

    private final EventRepository eventRepository;

    private final PartnerRepository partnerRepository;

    private final SocialMediaRepository socialMediaRepository;

    @Value("${photo_size}")
    private int PHOTO_MAX_SIZE;

    @Value("${upload_dir}")
    private String UPLOAD_DIR;

    @Value("${app.dev_ip}")
    private String IP;

    @Value("${app.dev_port}")
    private String PORT;

    public void saveImage(MultipartFile file){
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

        log.info("Сохраняем файл в " + UPLOAD_DIR);

        Path dirPath = Paths.get(UPLOAD_DIR);
        File dir = dirPath.toFile();
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            log.info("Директория создана: " + created);
        }

        log.info("Абсолютный путь к директории: " + dir.getAbsolutePath());

        Path filePath = dirPath.resolve(file.getOriginalFilename());
        log.info("Полный путь к файлу: " + filePath.toAbsolutePath());
        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream = Files.newOutputStream(filePath)) {
            byte[] buffer = new byte[PHOTO_MAX_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            log.info("Файл успешно сохранен: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            log.error("Ошибка при сохранении файла", e);
            throw new RuntimeException("Не удалось сохранить файл", e);
        }
    }

    public MediaDTO save(MultipartFile file){
        saveImage(file);
        Media media = mediaRepository.save(Media.builder()
                .url(String.format("http://%s/api/%s", IP, file.getOriginalFilename()))
                .build());

        return MediaDTO.builder()
                .id(media.getId())
                .url(media.getUrl())
                .build();
    }

    public List<MediaDTO> getAll(){
        return mediaRepository.findAll().stream()
                .map(mediaMapper::mediaToMediaDTO)
                .collect(Collectors.toList());
    }

    public MediaDTO getById(Long id){
        return mediaRepository.findById(id)
                .map(mediaMapper::mediaToMediaDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Изображение", id));
    }

    public void delete(Long id){
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Изображение", id));
        String fileName = media.getUrl().substring(media.getUrl().lastIndexOf('/') + 1);
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        log.info("Пытаемся удалить файл: " + filePath.toAbsolutePath());
        try {
            boolean deleted = Files.deleteIfExists(filePath);
            log.info("Файл удален: " + deleted);
        } catch (IOException e) {
            log.error("Ошибка при удалении файла", e);
        }
        mediaRepository.deleteById(id);
    }

    public MediaDTO update(Long id, MultipartFile file){
        saveImage(file);
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Изображение", id));
        media.setUrl(String.format("http://%s/api/%s", IP, file.getOriginalFilename()));

        mediaRepository.save(media);

        return MediaDTO.builder()
                .id(media.getId())
                .url(media.getUrl())
                .build();
    }

    @Scheduled(fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
    @Transactional
    public void deleteUnusedImages(){

        List<Media> medias = mediaRepository.findAll().stream()
                .filter(media -> albumRepository.findByLogo_Id(media.getId()).isEmpty())
                .filter(media -> albumRepository.findByBackgroundImage_Id(media.getId()).isEmpty())
                .filter(media -> contactRepository.findByLogo_Id(media.getId()).isEmpty())
                .filter(media -> eventRepository.findByLogo_Id(media.getId()).isEmpty())
                .filter(media -> partnerRepository.findByLogo_Id(media.getId()).isEmpty())
                .filter(media -> socialMediaRepository.findByLogo_Id(media.getId()).isEmpty())
                .collect(Collectors.toList());
        medias.forEach(
               media -> mediaRepository.deleteById(media.getId())
        );
    }

}
