package ru.spb.tacticul.repository;

import org.springframework.stereotype.Repository;
import ru.spb.tacticul.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {}