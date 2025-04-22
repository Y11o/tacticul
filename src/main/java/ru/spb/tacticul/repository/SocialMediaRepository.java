package ru.spb.tacticul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spb.tacticul.model.SocialMedia;

import java.util.Optional;

@Repository
public interface SocialMediaRepository extends JpaRepository<SocialMedia, Long> {
    Optional<SocialMedia> findByLogo_Id(Long logoId);
}
