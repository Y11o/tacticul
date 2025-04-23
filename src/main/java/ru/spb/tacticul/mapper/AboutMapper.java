package ru.spb.tacticul.mapper;

import org.mapstruct.Mapper;
import ru.spb.tacticul.dto.AboutDTO;
import ru.spb.tacticul.model.About;

@Mapper(componentModel = "spring")
public interface AboutMapper {

    AboutDTO aboutToAboutDTO(About about);
    About aboutDTOToAbout(AboutDTO aboutDTO);
}
