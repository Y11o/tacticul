package ru.spb.tacticul.repository;

import org.springframework.stereotype.Repository;
import ru.spb.tacticul.model.SocialMedia;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SocialMediaRepository extends JpaRepository<SocialMedia, Long> {}
