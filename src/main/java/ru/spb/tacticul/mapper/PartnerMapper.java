package ru.spb.tacticul.mapper;

import org.mapstruct.Mapper;
import ru.spb.tacticul.dto.PartnerDTO;
import ru.spb.tacticul.model.Partner;

@Mapper(componentModel = "spring")
public interface PartnerMapper {

    PartnerDTO partnerToPartnerDTO(Partner partner);
    Partner partnerDTOToPartner(PartnerDTO partnerDTO);
}
