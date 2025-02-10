package ru.spb.tacticul.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spb.tacticul.dto.ContactDTO;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.mapper.ContactMapper;
import ru.spb.tacticul.mapper.MediaMapper;
import ru.spb.tacticul.model.Contact;
import ru.spb.tacticul.repository.ContactRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {
    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;
    private final MediaMapper mediaMapper;

    public List<ContactDTO> getAll() {
        log.info("Получение всех контактов");
        return contactRepository.findAll().stream()
                .map(contactMapper::contactToContactDTO)
                .collect(Collectors.toList());
    }

    public ContactDTO getById(Long id) {
        log.info("Поиск контакта по ID: {}", id);
        return contactRepository.findById(id)
                .map(contactMapper::contactToContactDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Контакт", id));
    }

    @Transactional
    public ContactDTO create(ContactDTO contactDTO) {
        log.info("Создание нового контакта: {}", contactDTO.name());

        Contact contact = contactMapper.contactDTOToContact(contactDTO);
        contact = contactRepository.save(contact);

        log.info("Контакт с ID {} успешно создан", contact.getId());
        return contactMapper.contactToContactDTO(contact);
    }

    @Transactional
    public ContactDTO update(Long id, ContactDTO contactDTO) {
        log.info("Обновление контакта с ID: {}", id);

        return contactRepository.findById(id)
                .map(existingContact -> {
                    if (contactDTO.name() != null && !contactDTO.name().isEmpty()) {
                        existingContact.setName(contactDTO.name());
                    }
                    if (contactDTO.description() != null && !contactDTO.description().isEmpty()) {
                        existingContact.setDescription(contactDTO.description());
                    }
                    if (contactDTO.logo() != null) {
                        existingContact.setLogo(mediaMapper.mediaDTOToMedia(contactDTO.logo()));
                    }
                    if (contactDTO.url() != null) {
                        existingContact.setUrl(contactDTO.url());
                    }

                    contactRepository.save(existingContact);
                    log.info("Контакт с ID {} успешно обновлен", id);
                    return contactMapper.contactToContactDTO(existingContact);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Контакт", id));
    }

    @Transactional
    public void delete(Long id) {
        log.info("Удаление контакта с ID: {}", id);
        if (!contactRepository.existsById(id)) {
            throw new ResourceNotFoundException("Контакт", id);
        }
        contactRepository.deleteById(id);
        log.info("Контакт с ID {} успешно удален", id);
    }
}
