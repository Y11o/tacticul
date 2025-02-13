package ru.spb.tacticul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spb.tacticul.model.Media;

public interface MediaRepository extends JpaRepository<Media, Long> {
}
