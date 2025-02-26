package ru.spb.tacticul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.spb.tacticul.model.Album;
import ru.spb.tacticul.model.Event;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByLogo_Id(Long logoId);
}
