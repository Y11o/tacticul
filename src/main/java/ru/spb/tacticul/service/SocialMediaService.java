package ru.spb.tacticul.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spb.tacticul.dto.SocialMediaDTO;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.mapper.MediaMapper;
import ru.spb.tacticul.mapper.SocialMediaMapper;
import ru.spb.tacticul.model.SocialMedia;
import ru.spb.tacticul.repository.SocialMediaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialMediaService {
    private final SocialMediaRepository socialMediaRepository;
    private final SocialMediaMapper socialMediaMapper;
    private final MediaMapper mediaMapper;

    public List<SocialMediaDTO> getAll() {
        log.info("Получение всех социальных сетей");
        return socialMediaRepository.findAll().stream()
                .map(socialMediaMapper::socialMediaToSocialMediaDTO)
                .collect(Collectors.toList());
    }

    public SocialMediaDTO getById(Long id) {
        log.info("Поиск социальной сети по ID: {}", id);
        return socialMediaRepository.findById(id)
                .map(socialMediaMapper::socialMediaToSocialMediaDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Социальная сеть", id));
    }

    @Transactional
    public SocialMediaDTO create(SocialMediaDTO socialMediaDTO) {
        log.info("Создание новой социальной сети: {}", socialMediaDTO.name());

        SocialMedia socialMedia = socialMediaMapper.socialMediaDTOToSocialMedia(socialMediaDTO);
        socialMedia = socialMediaRepository.save(socialMedia);

        log.info("Социальная сеть с ID {} успешно создана", socialMedia.getId());
        return socialMediaMapper.socialMediaToSocialMediaDTO(socialMedia);
    }

    @Transactional
    public SocialMediaDTO update(Long id, SocialMediaDTO socialMediaDTO) {
        log.info("Обновление социальной сети с ID: {}", id);

        return socialMediaRepository.findById(id)
                .map(existingSocialMedia -> {
                    if (socialMediaDTO.name() != null && !socialMediaDTO.name().isEmpty()) {
                        existingSocialMedia.setName(socialMediaDTO.name());
                    }
                    if (socialMediaDTO.description() != null && !socialMediaDTO.description().isEmpty()) {
                        existingSocialMedia.setDescription(socialMediaDTO.description());
                    }
                    if (socialMediaDTO.logo() != null) {
                        existingSocialMedia.setLogo(mediaMapper.mediaDTOToMedia(socialMediaDTO.logo()));
                    }
                    if (socialMediaDTO.url() != null) {
                        existingSocialMedia.setUrl(socialMediaDTO.url());
                    }

                    socialMediaRepository.save(existingSocialMedia);
                    log.info("Социальная сеть с ID {} успешно обновлена", id);
                    return socialMediaMapper.socialMediaToSocialMediaDTO(existingSocialMedia);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Социальная сеть", id));
    }

    @Transactional
    public void delete(Long id) {
        log.info("Удаление социальной сети с ID: {}", id);
        if (!socialMediaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Социальная сеть", id);
        }
        socialMediaRepository.deleteById(id);
        log.info("Социальная сеть с ID {} успешно удалена", id);
    }
}