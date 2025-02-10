package ru.spb.tacticul.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spb.tacticul.dto.PartnerDTO;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.mapper.MediaMapper;
import ru.spb.tacticul.mapper.PartnerMapper;
import ru.spb.tacticul.model.Partner;
import ru.spb.tacticul.repository.PartnerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final PartnerMapper partnerMapper;
    private final MediaMapper mediaMapper;

    public List<PartnerDTO> getAll() {
        log.info("Получение всех партнеров");
        return partnerRepository.findAll().stream()
                .map(partnerMapper::partnerToPartnerDTO)
                .collect(Collectors.toList());
    }

    public PartnerDTO getById(Long id) {
        log.info("Поиск партнера по ID: {}", id);
        return partnerRepository.findById(id)
                .map(partnerMapper::partnerToPartnerDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Партнер", id));
    }

    @Transactional
    public PartnerDTO create(PartnerDTO partnerDTO) {
        log.info("Создание нового партнера: {}", partnerDTO.name());

        Partner partner = partnerMapper.partnerDTOToPartner(partnerDTO);
        partner = partnerRepository.save(partner);

        log.info("Партнер с ID {} успешно создан", partner.getId());
        return partnerMapper.partnerToPartnerDTO(partner);
    }

    @Transactional
    public PartnerDTO update(Long id, PartnerDTO partnerDTO) {
        log.info("Обновление партнера с ID: {}", id);

        return partnerRepository.findById(id)
                .map(existingPartner -> {
                    if (partnerDTO.name() != null && !partnerDTO.name().isEmpty()) {
                        existingPartner.setName(partnerDTO.name());
                    }
                    if (partnerDTO.description() != null && !partnerDTO.description().isEmpty()) {
                        existingPartner.setDescription(partnerDTO.description());
                    }
                    if (partnerDTO.logo() != null) {
                        existingPartner.setLogo(mediaMapper.mediaDTOToMedia(partnerDTO.logo()));
                    }
                    if (partnerDTO.url() != null) {
                        existingPartner.setUrl(partnerDTO.url());
                    }

                    partnerRepository.save(existingPartner);
                    log.info("Партнер с ID {} успешно обновлен", id);
                    return partnerMapper.partnerToPartnerDTO(existingPartner);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Партнер", id));
    }

    @Transactional
    public void delete(Long id) {
        log.info("Удаление партнера с ID: {}", id);
        if (!partnerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Партнер", id);
        }
        partnerRepository.deleteById(id);
        log.info("Партнер с ID {} успешно удален", id);
    }
}