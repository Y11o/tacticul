package ru.spb.tacticul.repository;

import org.springframework.stereotype.Repository;
import ru.spb.tacticul.model.Album;
import ru.spb.tacticul.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    Optional<Partner> findByLogo_Id(Long logoId);
}