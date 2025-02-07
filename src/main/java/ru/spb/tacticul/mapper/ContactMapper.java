package ru.spb.tacticul.mapper;

import ru.spb.tacticul.dto.ContactDTO;
import ru.spb.tacticul.model.Contact;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    ContactDTO contactToContactDTO(Contact contact);
    Contact contactDTOToContact(ContactDTO contactDTO);
}
