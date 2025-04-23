package ru.spb.tacticul.mapper;

import org.mapstruct.Mapper;
import ru.spb.tacticul.dto.ContactDTO;
import ru.spb.tacticul.model.Contact;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    ContactDTO contactToContactDTO(Contact contact);
    Contact contactDTOToContact(ContactDTO contactDTO);
}
