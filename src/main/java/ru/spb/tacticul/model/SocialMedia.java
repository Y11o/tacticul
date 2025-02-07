package ru.spb.tacticul.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "social_media")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    private Media logo;

    private String url;
}
