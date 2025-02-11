package ru.spb.tacticul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spb.tacticul.model.Album;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}
