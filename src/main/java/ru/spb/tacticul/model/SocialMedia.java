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

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne(cascade = CascadeType.MERGE)
    private Media logo;

    private String url;
}