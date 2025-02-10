package ru.spb.tacticul.mapper;

import ru.spb.tacticul.dto.MediaDTO;
import ru.spb.tacticul.model.Media;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MediaMapper {

    MediaDTO mediaToMediaDTO(Media media);
    Media mediaDTOToMedia(MediaDTO mediaDTO);
}
