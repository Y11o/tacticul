package ru.spb.tacticul.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "event")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String shortDescription;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String longDescription;

    @OneToOne(cascade = CascadeType.MERGE)
    private Media logo;

    @OneToOne(cascade = CascadeType.MERGE)
    private Media img;

    @Column
    private String position;
}
