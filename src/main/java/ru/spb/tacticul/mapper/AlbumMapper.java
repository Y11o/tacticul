package ru.spb.tacticul.mapper;

import org.mapstruct.Mapper;
import ru.spb.tacticul.dto.AlbumDTO;
import ru.spb.tacticul.model.Album;

@Mapper(componentModel = "spring")
public interface AlbumMapper {

    AlbumDTO albumToAlbumDTO(Album album);

    Album albumDTOtoAlbum(AlbumDTO albumDTO);

}
