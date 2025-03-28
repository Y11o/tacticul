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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InProgressService {
    private final InProgressRepository inProgressRepository;
    private final InProgressMapper inProgressMapper;

    public InProgressDTO getAll() {
        log.info("Получение всех записей InProgress");
        return inProgressRepository.findAll().stream()
                .map(inProgressMapper::inProgressToInProgressDTO)
                .findFirst()
                .orElse(null);
    }

    public Optional<InProgress> getInProgress(){
        return inProgressRepository.findAll().stream().findFirst();
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
        inProgressRepository.deleteAll();
        inProgress = inProgressRepository.save(inProgress);
        log.info("InProgress с ID {} успешно создан", inProgress.getId());
        return inProgressMapper.inProgressToInProgressDTO(inProgress);
    }

    @Transactional
    public InProgressDTO update(InProgressDTO inProgressDTO) {
        log.info("Обновление InProgress");
        return getInProgress()
                .map(existing -> {
                    if (inProgressDTO.isAvailable() != null) {
                        existing.setIsAvailable(inProgressDTO.isAvailable());
                    }
                    if (inProgressDTO.description() != null && !inProgressDTO.description().isEmpty()) {
                        existing.setDescription(inProgressDTO.description());
                    }
                    inProgressRepository.save(existing);
                    log.info("InProgress успешно обновлен");
                    return inProgressMapper.inProgressToInProgressDTO(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Не найден InProgress"));
    }

    @Transactional
    public void delete() {
        log.info("Удаление InProgress");
        inProgressRepository.deleteAll();
        log.info("InProgress успешно удален");
    }
}