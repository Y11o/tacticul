package ru.spb.tacticul.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.spb.tacticul.dto.ContactDTO;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.mapper.ContactMapper;
import ru.spb.tacticul.mapper.MediaMapper;
import ru.spb.tacticul.model.Contact;
import ru.spb.tacticul.repository.ContactRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ContactMapper contactMapper;

    @Mock
    private MediaMapper mediaMapper;

    @InjectMocks
    private ContactService contactService;

    private Contact contact;
    private ContactDTO contactDTO;

    @BeforeEach
    void setUp() {
        contact = new Contact();
        contact.setId(1L);
        contact.setName("Test Contact");
        contact.setDescription("Test Description");

        contactDTO = new ContactDTO(1L, "Test Contact", "Test Description", null, "https://contact.com");
    }

    @Test
    void getAll_ShouldReturnListOfContactDTOs() {
        when(contactRepository.findAll()).thenReturn(List.of(contact));
        when(contactMapper.contactToContactDTO(contact)).thenReturn(contactDTO);

        List<ContactDTO> result = contactService.getAll();

        assertEquals(1, result.size());
        assertEquals("Test Contact", result.get(0).name());
        verify(contactRepository, times(1)).findAll();
    }

    @Test
    void getById_ExistingId_ShouldReturnContactDTO() {
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(contactMapper.contactToContactDTO(contact)).thenReturn(contactDTO);

        ContactDTO result = contactService.getById(1L);

        assertEquals("Test Contact", result.name());
        verify(contactRepository, times(1)).findById(1L);
    }

    @Test
    void getById_NonExistingId_ShouldThrowException() {
        when(contactRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contactService.getById(2L));
    }

    @Test
    void create_ShouldReturnCreatedContactDTO() {
        when(contactMapper.contactDTOToContact(contactDTO)).thenReturn(contact);
        when(contactRepository.save(contact)).thenReturn(contact);
        when(contactMapper.contactToContactDTO(contact)).thenReturn(contactDTO);

        ContactDTO result = contactService.create(contactDTO);

        assertEquals("Test Contact", result.name());
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void update_ExistingId_ShouldUpdateAndReturnDTO() {
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(contactRepository.save(any())).thenReturn(contact);
        when(contactMapper.contactToContactDTO(any())).thenReturn(contactDTO);

        ContactDTO result = contactService.update(1L, contactDTO);

        assertEquals("Test Contact", result.name());
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void update_NonExistingId_ShouldThrowException() {
        when(contactRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> contactService.update(2L, contactDTO));
    }

    @Test
    void delete_ExistingId_ShouldDeleteSuccessfully() {
        when(contactRepository.existsById(1L)).thenReturn(true);
        doNothing().when(contactRepository).deleteById(1L);

        assertDoesNotThrow(() -> contactService.delete(1L));
        verify(contactRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_NonExistingId_ShouldThrowException() {
        when(contactRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> contactService.delete(2L));
    }
}
