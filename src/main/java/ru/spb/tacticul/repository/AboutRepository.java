package ru.spb.tacticul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spb.tacticul.model.About;

@Repository
public interface AboutRepository extends JpaRepository<About, Long> {
}