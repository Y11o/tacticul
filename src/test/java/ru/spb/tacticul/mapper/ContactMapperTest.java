package ru.spb.tacticul.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.spb.tacticul.dto.ContactDTO;
import ru.spb.tacticul.dto.MediaDTO;
import ru.spb.tacticul.model.Contact;
import ru.spb.tacticul.model.Media;

public class ContactMapperTest {

    private final ContactMapper contactMapper = Mappers.getMapper(ContactMapper.class);

    @Test
    void testContactToContactDTO() {
        Media media = Media.builder().id(1L).fileName("logo.png").build();
        Contact contact = Contact.builder()
                .id(1L)
                .name("Test Contact")
                .description("Test Description")
                .logo(media)
                .url("http://example.com").build();

        ContactDTO contactDTO = contactMapper.contactToContactDTO(contact);

        assertNotNull(contactDTO);
        assertEquals(contact.getId(), contactDTO.id());
        assertEquals(contact.getName(), contactDTO.name());
        assertEquals(contact.getDescription(), contactDTO.description());
        assertNotNull(contactDTO.logo());
        assertEquals(contact.getUrl(), contactDTO.url());
    }

    @Test
    void testContactDTOToContact() {
        MediaDTO mediaDTO = new MediaDTO(1L, "logo.png");
        ContactDTO contactDTO = new ContactDTO(1L, "Test Contact", "Test Description", mediaDTO, "http://example.com");

        Contact contact = contactMapper.contactDTOToContact(contactDTO);

        assertNotNull(contact);
        assertEquals(contactDTO.id(), contact.getId());
        assertEquals(contactDTO.name(), contact.getName());
        assertEquals(contactDTO.description(), contact.getDescription());
        assertNotNull(contact.getLogo());
        assertEquals(contactDTO.url(), contact.getUrl());
    }
}


