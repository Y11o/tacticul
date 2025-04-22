package ru.spb.tacticul.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "in_progress")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Boolean isAvailable;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
}
