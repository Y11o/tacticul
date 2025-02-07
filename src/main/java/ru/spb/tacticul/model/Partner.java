package ru.spb.tacticul.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "partner")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Lob
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    private Media logo;

    private String url;
}
