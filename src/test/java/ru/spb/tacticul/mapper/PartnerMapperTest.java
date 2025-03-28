package ru.spb.tacticul.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.spb.tacticul.dto.MediaDTO;
import ru.spb.tacticul.dto.PartnerDTO;
import ru.spb.tacticul.model.Media;
import ru.spb.tacticul.model.Partner;

public class PartnerMapperTest {

    private final PartnerMapper partnerMapper = Mappers.getMapper(PartnerMapper.class);

    @Test
    void testPartnerToPartnerDTO() {
        Media media = Media.builder().id(1L).url("logo.png").build();
        Partner partner = Partner.builder()
                .id(1L)
                .name("Test Partner")
                .description("Test Description")
                .logo(media)
                .url("http://example.com")
                .build();

        PartnerDTO partnerDTO = partnerMapper.partnerToPartnerDTO(partner);

        assertNotNull(partnerDTO);
        assertEquals(partner.getId(), partnerDTO.id());
        assertEquals(partner.getName(), partnerDTO.name());
        assertEquals(partner.getDescription(), partnerDTO.description());
        assertNotNull(partnerDTO.logo());
        assertEquals(partner.getUrl(), partnerDTO.url());
    }

    @Test
    void testPartnerDTOToPartner() {
        MediaDTO mediaDTO = new MediaDTO(1L, "logo.png");
        PartnerDTO partnerDTO = new PartnerDTO(1L, "Test Partner", "Test Description", mediaDTO, "http://example.com");

        Partner partner = partnerMapper.partnerDTOToPartner(partnerDTO);

        assertNotNull(partner);
        assertEquals(partnerDTO.id(), partner.getId());
        assertEquals(partnerDTO.name(), partner.getName());
        assertEquals(partnerDTO.description(), partner.getDescription());
        assertNotNull(partner.getLogo());
        assertEquals(partnerDTO.url(), partner.getUrl());
    }
}
