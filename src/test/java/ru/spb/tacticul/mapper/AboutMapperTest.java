package ru.spb.tacticul.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.spb.tacticul.dto.AboutDTO;
import ru.spb.tacticul.model.About;

public class AboutMapperTest {

    private final AboutMapper aboutMapper = Mappers.getMapper(AboutMapper.class);

    @Test
    void testAboutToAboutDTO() {
        About about = About.builder()
                .id(1L)
                .name("Test About")
                .description("Test Description")
                .build();

        AboutDTO aboutDTO = aboutMapper.aboutToAboutDTO(about);

        assertNotNull(aboutDTO);
        assertEquals(about.getId(), aboutDTO.id());
        assertEquals(about.getName(), aboutDTO.name());
        assertEquals(about.getDescription(), aboutDTO.description());
    }

    @Test
    void testAboutDTOToAbout() {
        AboutDTO aboutDTO = new AboutDTO(1L, "Test About", "Test Description");

        About about = aboutMapper.aboutDTOToAbout(aboutDTO);

        assertNotNull(about);
        assertEquals(aboutDTO.id(), about.getId());
        assertEquals(aboutDTO.name(), about.getName());
        assertEquals(aboutDTO.description(), about.getDescription());
    }
}


