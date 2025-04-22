package ru.spb.tacticul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spb.tacticul.model.Album;

import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByLogo_Id(Long logoId);

    Optional<Album> findByBackgroundImage_Id(Long backgroundId);
}
