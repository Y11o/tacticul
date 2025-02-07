package ru.spb.tacticul.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AboutDTO {
    private Long id;
    private String name;
    private String description;
    private MediaDTO logo;
}
