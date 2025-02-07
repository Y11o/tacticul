package ru.spb.tacticul.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialMediaDTO {
    private Long id;
    private String name;
    private String description;
    private MediaDTO logo;
    private String url;
}
