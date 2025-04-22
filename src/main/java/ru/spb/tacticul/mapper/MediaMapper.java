package ru.spb.tacticul.mapper;

import org.mapstruct.Mapper;
import ru.spb.tacticul.dto.MediaDTO;
import ru.spb.tacticul.model.Media;

@Mapper(componentModel = "spring")
public interface MediaMapper {

    MediaDTO mediaToMediaDTO(Media media);
    Media mediaDTOToMedia(MediaDTO mediaDTO);
}
