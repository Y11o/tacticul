package ru.spb.tacticul.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spb.tacticul.dto.AboutDTO;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.mapper.AboutMapper;
import ru.spb.tacticul.mapper.MediaMapper;
import ru.spb.tacticul.model.About;
import ru.spb.tacticul.repository.AboutRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AboutService {
    private final AboutRepository aboutRepository;
    private final AboutMapper aboutMapper;
    private final MediaMapper mediaMapper;

    public AboutDTO getAll() {
        log.info("Получение всех записей 'О нас'");
        return aboutRepository.findAll().stream()
                .map(aboutMapper::aboutToAboutDTO)
                .findFirst()
                .orElse(null);
    }

    public Optional<About> getAbout() {
        return aboutRepository.findAll().stream()
                .findFirst();
    }

    public AboutDTO getById(Long id) {
        log.info("Поиск записи 'О нас' по ID: {}", id);
        return aboutRepository.findById(id)
                .map(aboutMapper::aboutToAboutDTO)
                .orElseThrow(() -> new ResourceNotFoundException("О нас", id));
    }

    @Transactional
    public AboutDTO create(AboutDTO aboutDTO) {
        log.info("Создание новой записи 'О нас': {}", aboutDTO.name());

        About about = aboutMapper.aboutDTOToAbout(aboutDTO);
        aboutRepository.deleteAll();
        about = aboutRepository.save(about);

        log.info("Запись 'О нас' успешно создана");
        return aboutMapper.aboutToAboutDTO(about);
    }

    @Transactional
    public AboutDTO update(AboutDTO aboutDTO) {
        log.info("Обновление записи 'О нас'");

        return getAbout()
                .map(existingAbout -> {
                    if (aboutDTO.name() != null && !aboutDTO.name().isEmpty()) {
                        existingAbout.setName(aboutDTO.name());
                    }
                    if (aboutDTO.description() != null && !aboutDTO.description().isEmpty()) {
                        existingAbout.setDescription(aboutDTO.description());
                    }

                    aboutRepository.save(existingAbout);
                    log.info("Запись 'О нас' успешно обновлена");
                    return aboutMapper.aboutToAboutDTO(existingAbout);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Нет записи О нас"));
    }

    @Transactional
    public void delete() {
        log.info("Удаление записи 'О нас'");
        aboutRepository.deleteAll();
        log.info("Запись 'О нас' успешно удалена");
    }
}
