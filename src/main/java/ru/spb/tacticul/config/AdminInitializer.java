package ru.spb.tacticul.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import ru.spb.tacticul.repository.UserRepository;
import ru.spb.tacticul.model.User;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${admin.login}")
    private String adminLogin;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.email}")
    private String adminEmail;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void createAdminIfNotExists() {
        Optional<User> existingAdmin = userRepository.findByLogin(adminLogin);
        if (existingAdmin.isEmpty()) {
            User admin = User.builder()
                    .login(adminLogin)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .build();
            userRepository.save(admin);
            log.info("Администратор {} успешно создан", adminLogin);
        } else {
            log.info("Администратор уже существует");
        }
    }
}