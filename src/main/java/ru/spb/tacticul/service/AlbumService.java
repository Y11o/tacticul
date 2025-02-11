package ru.spb.tacticul.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spb.tacticul.dto.AlbumDTO;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.mapper.AlbumMapper;
import ru.spb.tacticul.mapper.MediaMapper;
import ru.spb.tacticul.model.Album;
import ru.spb.tacticul.repository.AlbumRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final MediaMapper mediaMapper;

    public List<AlbumDTO> getAll() {
        log.info("Получение всех альбомов");
        return albumRepository.findAll().stream()
                .map(albumMapper::albumToAlbumDTO)
                .collect(Collectors.toList());
    }

    public AlbumDTO getById(Long id) {
        log.info("Поиск альбома по ID: {}", id);
        return albumRepository.findById(id)
                .map(albumMapper::albumToAlbumDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Альбом", id));
    }

    @Transactional
    public AlbumDTO create(AlbumDTO albumDTO) {
        log.info("Создание нового альбома: {}", albumDTO.name());

        Album album = albumMapper.albumDTOtoAlbum(albumDTO);
        album = albumRepository.save(album);

        log.info("Альбом с ID {} успешно создан", album.getId());
        return albumMapper.albumToAlbumDTO(album);
    }

    @Transactional
    public AlbumDTO update(Long id, AlbumDTO albumDTO) {
        log.info("Обновление социальной сети с ID: {}", id);

        return albumRepository.findById(id)
                .map(existingAlbum -> {
                    if (albumDTO.name() != null && !albumDTO.name().isEmpty()) {
                        existingAlbum.setName(albumDTO.name());
                    }
                    if (albumDTO.description() != null && !albumDTO.description().isEmpty()) {
                        existingAlbum.setDescription(albumDTO.description());
                    }
                    if (albumDTO.logo() != null) {
                        existingAlbum.setLogo(mediaMapper.mediaDTOToMedia(albumDTO.logo()));
                    }
                    if (albumDTO.background() != null) {
                        existingAlbum.setBackgroundImage(mediaMapper.mediaDTOToMedia(albumDTO.background()));
                    }
                    if (albumDTO.url() != null) {
                        existingAlbum.setUrl(albumDTO.url());
                    }

                    albumRepository.save(existingAlbum);
                    log.info("Альбом с ID {} успешно обновлен", id);
                    return albumMapper.albumToAlbumDTO(existingAlbum);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Альбом", id));
    }

    @Transactional
    public void delete(Long id) {
        log.info("Удаление альбома с ID: {}", id);
        if (!albumRepository.existsById(id)) {
            throw new ResourceNotFoundException("Альбом", id);
        }
        albumRepository.deleteById(id);
        log.info("Альбом с ID {} успешно удалён", id);
    }
}
