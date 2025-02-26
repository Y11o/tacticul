package ru.spb.tacticul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.spb.tacticul.dto.AlbumDTO;
import ru.spb.tacticul.dto.GalleryDTO;
import ru.spb.tacticul.dto.MediaDTO;
import ru.spb.tacticul.dto.SummaryDTO;
import ru.spb.tacticul.repository.AlbumRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final AboutService aboutService;

    private final EventService eventService;

    private final AlbumService albumService;

    private final PartnerService partnerService;

    private final SocialMediaService socialMediaService;

    private final ContactService contactService;

    public SummaryDTO getSummary(){
        return SummaryDTO.builder()
                .about(aboutService.getAll())
                .events(eventService.getAll())
                .gallery(GalleryDTO.builder()
                        .albums(albumService.getAll().stream()
                                .map(album -> AlbumDTO.builder()
                                        .url(album.url())
                                        .id(album.id())
                                        .logo(album.logo())
                                        .name(album.name())
                                        .description(album.description())
                                        .build())
                                .collect(Collectors.toList()))
                        .backgroundImg(albumService.getAll().stream().findAny().orElse(AlbumDTO.builder()
                                .background(MediaDTO.builder()
                                        .url("nothing")
                                        .build()).build())
                                .background().getUrl())
                        .build())
                .partners(partnerService.getAll())
                .socialmedias(socialMediaService.getAll())
                .contacts(contactService.getAll())
                .build();
    }

}
