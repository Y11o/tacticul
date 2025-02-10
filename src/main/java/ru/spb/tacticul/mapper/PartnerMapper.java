package ru.spb.tacticul.mapper;

import ru.spb.tacticul.dto.PartnerDTO;
import ru.spb.tacticul.model.Partner;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PartnerMapper {

    PartnerDTO partnerToPartnerDTO(Partner partner);
    Partner partnerDTOToPartner(PartnerDTO partnerDTO);
}
