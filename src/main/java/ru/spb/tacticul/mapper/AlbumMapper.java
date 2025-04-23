package ru.spb.tacticul.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.spb.tacticul.dto.AlbumDTO;
import ru.spb.tacticul.model.Album;

@Mapper(componentModel = "spring")
public interface AlbumMapper {

    @Mapping(source = "backgroundImage", target = "background")
    AlbumDTO albumToAlbumDTO(Album album);

    @Mapping(source = "background", target = "backgroundImage")
    Album albumDTOtoAlbum(AlbumDTO albumDTO);

}
