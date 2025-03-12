package ru.spb.tacticul.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.spb.tacticul.dto.AboutDTO;
import ru.spb.tacticul.dto.MediaDTO;
import ru.spb.tacticul.model.About;
import ru.spb.tacticul.model.Media;

@SpringBootTest
public class AboutMapperTest {

    @Autowired
    private AboutMapper aboutMapper;

    @Test
    void testAboutToAboutDTO() {
        Media mediaZh = Media.builder().id(1L).fileName("logoZh.png").build();
        Media mediaEdc = Media.builder().id(2L).fileName("logoEdc.png").build();
        Media mediaDv = Media.builder().id(3L).fileName("logoDv.png").build();
        Media mediaRus = Media.builder().id(4L).fileName("logoRus.png").build();

        About about = About.builder()
                .id(1L)
                .name("Test About")
                .description("Test Description")
                .logoZh(mediaZh)
                .logoEdc(mediaEdc)
                .logoDv(mediaDv)
                .logoRus(mediaRus)
                .build();

        AboutDTO aboutDTO = aboutMapper.aboutToAboutDTO(about);

        assertNotNull(aboutDTO);
        assertEquals(about.getId(), aboutDTO.id());
        assertEquals(about.getName(), aboutDTO.name());
        assertEquals(about.getDescription(), aboutDTO.description());
        assertNotNull(aboutDTO.logoZh());
        assertNotNull(aboutDTO.logoEdc());
        assertNotNull(aboutDTO.logoDv());
        assertNotNull(aboutDTO.logoRus());
    }

    @Test
    void testAboutDTOToAbout() {
        MediaDTO mediaDTOZh = new MediaDTO(1L, "logoZh.png");
        MediaDTO mediaDTOEdc = new MediaDTO(2L, "logoEdc.png");
        MediaDTO mediaDTODv = new MediaDTO(3L, "logoDv.png");
        MediaDTO mediaDTORus = new MediaDTO(4L, "logoRus.png");

        AboutDTO aboutDTO = new AboutDTO(1L, "Test About", "Test Description", mediaDTOZh, mediaDTOEdc, mediaDTODv, mediaDTORus);

        About about = aboutMapper.aboutDTOToAbout(aboutDTO);

        assertNotNull(about);
        assertEquals(aboutDTO.id(), about.getId());
        assertEquals(aboutDTO.name(), about.getName());
        assertEquals(aboutDTO.description(), about.getDescription());
        assertNotNull(about.getLogoZh());
        assertNotNull(about.getLogoEdc());
        assertNotNull(about.getLogoDv());
        assertNotNull(about.getLogoRus());
    }
}


