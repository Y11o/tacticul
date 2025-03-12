package ru.spb.tacticul.repository;

import org.springframework.stereotype.Repository;
import ru.spb.tacticul.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByLogo_Id(Long logoId);
}
