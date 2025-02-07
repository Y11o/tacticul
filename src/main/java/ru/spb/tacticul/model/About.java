package ru.spb.tacticul.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "about")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class About {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    private Media logo;
}
