package ru.spb.tacticul.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "album")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne(cascade = CascadeType.MERGE)
    private Media logo;

    @OneToOne(cascade = CascadeType.MERGE)
    private Media backgroundImage;

    private String url;

}
