package ru.spb.tacticul.mapper;

import ru.spb.tacticul.dto.SocialMediaDTO;
import ru.spb.tacticul.model.SocialMedia;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SocialMediaMapper {

    SocialMediaDTO socialMediaToSocialMediaDTO(SocialMedia socialMedia);
    SocialMedia socialMediaDTOToSocialMedia(SocialMediaDTO socialMediaDTO);
}
