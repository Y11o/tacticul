package ru.spb.tacticul.mapper;

import org.mapstruct.Mapper;
import ru.spb.tacticul.dto.SocialMediaDTO;
import ru.spb.tacticul.model.SocialMedia;

@Mapper(componentModel = "spring")
public interface SocialMediaMapper {

    SocialMediaDTO socialMediaToSocialMediaDTO(SocialMedia socialMedia);
    SocialMedia socialMediaDTOToSocialMedia(SocialMediaDTO socialMediaDTO);
}
