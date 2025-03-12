package ru.spb.tacticul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spb.tacticul.model.InProgress;

@Repository
public interface InProgressRepository extends JpaRepository<InProgress, Long> {
}
