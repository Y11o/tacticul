package ru.spb.tacticul.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.spb.tacticul.model.User;
import ru.spb.tacticul.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Попытка аутентификации пользователя: {}", username);

        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> {
                    log.warn("Ошибка аутентификации: пользователь '{}' не найден", username);
                    return new UsernameNotFoundException("Пользователь не найден: " + username);
                });

        log.info("Пользователь '{}' успешно аутентифицирован", username);

        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );
    }
}
