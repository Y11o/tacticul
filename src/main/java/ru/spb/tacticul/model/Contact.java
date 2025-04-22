package ru.spb.tacticul.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contact")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

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
