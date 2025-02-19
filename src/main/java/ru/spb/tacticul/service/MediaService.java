package ru.spb.tacticul.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
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

    @Value("${dev_ip}")
    private String IP;

    @Value("${dev_port}")
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

    }

    public MediaDTO save(MultipartFile file){
        saveImage(file);
        Media media = mediaRepository.save(Media.builder()
                .fileName(String.format("http://%s:%s/uploads/%s", IP, PORT, file.getOriginalFilename()))
                .build());

        return MediaDTO.builder()
                .id(media.getId())
                .url(media.getFileName())
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
        File file = new File(UPLOAD_DIR + media.getFileName().substring(media.getFileName().lastIndexOf('/') + 1));
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        mediaRepository.deleteById(id);
    }

    public MediaDTO update(Long id, MultipartFile file){
        saveImage(file);
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Изображение", id));
        media.setFileName(String.format("http://%s:%s/uploads/%s", IP, PORT, file.getOriginalFilename()));

        mediaRepository.save(media);

        return MediaDTO.builder()
                .id(media.getId())
                .url(media.getFileName())
                .build();
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void deleteUnusedImages(){

        List<Media> medias = mediaRepository.findAll().stream()
                .filter(media -> aboutRepository.findByLogo_Id(media.getId()).isEmpty())
                .filter(media -> albumRepository.findByLogo_Id(media.getId()).isEmpty())
                .filter(media -> albumRepository.findByBackgroundImage_Id(media.getId()).isEmpty())
                .filter(media -> contactRepository.findByLogo_Id(media.getId()).isEmpty())
                .filter(media -> eventRepository.findByLogo_Id(media.getId()).isEmpty())
                .filter(media -> partnerRepository.findByLogo_Id(media.getId()).isEmpty())
                .filter(media -> socialMediaRepository.findByLogo_Id(media.getId()).isEmpty())
                .collect(Collectors.toList());
        log.info(String.valueOf(medias.size()));
        medias.forEach(
               media -> mediaRepository.deleteById(media.getId())
        );
    }

}
