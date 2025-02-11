package ru.spb.tacticul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spb.tacticul.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
