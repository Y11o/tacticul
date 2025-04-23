package ru.spb.tacticul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.spb.tacticul.dto.AlbumDTO;
import ru.spb.tacticul.dto.EventForSummaryDTO;
import ru.spb.tacticul.dto.MediaForSummaryDTO;
import ru.spb.tacticul.dto.SummaryDTO;

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
                .events(eventService.getAll()
                        .stream()
                        .map(eventDTO -> EventForSummaryDTO.builder()
                                .img(eventDTO.img())
                                .shortDescription(eventDTO.shortDescription())
                                .longDescription(eventDTO.longDescription())
                                .name(eventDTO.name())
                                .id(eventDTO.id())
                                .logo(MediaForSummaryDTO.builder()
                                        .id(eventDTO.logo().getId())
                                        .url(eventDTO.logo().getUrl())
                                        .position(eventDTO.position())
                                        .build())
                                .build())
                        .collect(Collectors.toList()))
                .gallery(albumService.getAll().stream()
                                .map(album -> AlbumDTO.builder()
                                        .url(album.url())
                                        .id(album.id())
                                        .logo(album.logo())
                                        .name(album.name())
                                        .description(album.description())
                                        .background(album.background())
                                        .build())
                                .collect(Collectors.toList())
                        )
                .partners(partnerService.getAll())
                .socialmedias(socialMediaService.getAll())
                .contacts(contactService.getAll())
                .build();
    }

}
