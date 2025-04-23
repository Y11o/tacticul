package ru.spb.tacticul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.spb.tacticul.config.JwtUtil;
import ru.spb.tacticul.dto.authentication.RecoveryRequest;
import ru.spb.tacticul.dto.authentication.SignInRequest;
import ru.spb.tacticul.dto.authentication.SignUpRequest;
import ru.spb.tacticul.dto.authentication.TokenResponse;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.model.User;
import ru.spb.tacticul.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.reset-password.script-path}")
    private String resetPasswordScriptPath;

    @Transactional
    public TokenResponse signUp(SignUpRequest request) {
        log.info("Регистрация нового пользователя: {}", request.login());

        if (userRepository.findByLogin(request.login()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }

        User user = new User();
        user.setLogin(request.login());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        log.info("Пользователь {} успешно зарегистрирован", request.login());
        return new TokenResponse(jwtUtil.generateToken(user.getLogin()));
    }

    public TokenResponse signIn(SignInRequest request) {
        log.info("Аутентификация пользователя: {}", request.credentials());

        Optional<User> userOptional = userRepository.findByEmail(request.credentials());
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByLogin(request.credentials());
        }
        User user = userOptional.orElseThrow(() -> new ResourceNotFoundException("Пользователь с таким email/login: " +
                request.credentials() + " не найден"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), request.password()));

        log.info("Пользователь {} успешно аутентифицирован", user.getLogin());
        return new TokenResponse(jwtUtil.generateToken(user.getLogin()));
    }

    @Transactional
    public void recoveryPassword(RecoveryRequest request) {
        log.info("Запрос на восстановление пароля для email: {}", request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с таким email: " +
                        request.email() +  " не найден"));

        if (user.getTelegramId() == null) {
            throw new IllegalStateException("У пользователя не задан Telegram ID для восстановления пароля");
        }

        String newPassword = generateRandomPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        Path scriptPath = Paths.get(resetPasswordScriptPath).toAbsolutePath();
        if (!scriptPath.toFile().exists()) {
            log.error("Скрипт восстановления пароля не найден по пути: {}", scriptPath);
            throw new RuntimeException("Скрипт восстановления пароля не найден");
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "python3",
                    scriptPath.toString(),
                    user.getTelegramId().toString(),
                    newPassword
            );
            
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            
            if (exitCode != 0) {
                log.error("Ошибка при выполнении скрипта resetpassword.py. Код выхода: {}", exitCode);
                throw new RuntimeException("Ошибка при сбросе пароля через Telegram");
            }
            
            log.info("Пароль успешно сброшен через Telegram для пользователя: {}", user.getLogin());
        } catch (IOException | InterruptedException e) {
            log.error("Ошибка при выполнении скрипта resetpassword.py", e);
            throw new RuntimeException("Ошибка при сбросе пароля через Telegram", e);
        }
    }

    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
