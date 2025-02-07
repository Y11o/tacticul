package ru.spb.tacticul.repository;

import ru.spb.tacticul.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
