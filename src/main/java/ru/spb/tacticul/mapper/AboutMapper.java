package ru.spb.tacticul.mapper;

import ru.spb.tacticul.dto.AboutDTO;
import ru.spb.tacticul.model.About;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AboutMapper {

    AboutDTO aboutToAboutDTO(About about);
    About aboutDTOToAbout(AboutDTO aboutDTO);
}
