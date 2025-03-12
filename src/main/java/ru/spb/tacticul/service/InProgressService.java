package ru.spb.tacticul.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spb.tacticul.dto.InProgressDTO;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.mapper.InProgressMapper;
import ru.spb.tacticul.model.InProgress;
import ru.spb.tacticul.repository.InProgressRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InProgressService {
    private final InProgressRepository inProgressRepository;
    private final InProgressMapper inProgressMapper;

    public List<InProgressDTO> getAll() {
        log.info("Получение всех записей InProgress");
        return inProgressRepository.findAll().stream()
                .map(inProgressMapper::inProgressToInProgressDTO)
                .collect(Collectors.toList());
    }

    public InProgressDTO getById(Long id) {
        log.info("Поиск InProgress по ID: {}", id);
        return inProgressRepository.findById(id)
                .map(inProgressMapper::inProgressToInProgressDTO)
                .orElseThrow(() -> new ResourceNotFoundException("InProgress", id));
    }

    @Transactional
    public InProgressDTO create(InProgressDTO inProgressDTO) {
        log.info("Создание нового InProgress: {}", inProgressDTO);
        InProgress inProgress = inProgressMapper.inProgressDTOToInProgress(inProgressDTO);
        inProgress = inProgressRepository.save(inProgress);
        log.info("InProgress с ID {} успешно создан", inProgress.getId());
        return inProgressMapper.inProgressToInProgressDTO(inProgress);
    }

    @Transactional
    public InProgressDTO update(Long id, InProgressDTO inProgressDTO) {
        log.info("Обновление InProgress с ID: {}", id);
        return inProgressRepository.findById(id)
                .map(existing -> {
                    if (inProgressDTO.isAvailable() != null) {
                        existing.setIsAvailable(inProgressDTO.isAvailable());
                    }
                    if (inProgressDTO.description() != null && !inProgressDTO.description().isEmpty()) {
                        existing.setDescription(inProgressDTO.description());
                    }
                    inProgressRepository.save(existing);
                    log.info("InProgress с ID {} успешно обновлен", id);
                    return inProgressMapper.inProgressToInProgressDTO(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("InProgress", id));
    }

    @Transactional
    public void delete(Long id) {
        log.info("Удаление InProgress с ID: {}", id);
        if (!inProgressRepository.existsById(id)) {
            throw new ResourceNotFoundException("InProgress", id);
        }
        inProgressRepository.deleteById(id);
        log.info("InProgress с ID {} успешно удален", id);
    }
}