package ru.spb.tacticul.service;

import ru.spb.tacticul.dto.authentication.SignInRequest;
import ru.spb.tacticul.dto.authentication.SignUpRequest;
import ru.spb.tacticul.dto.authentication.TokenResponse;
import ru.spb.tacticul.model.User;
import ru.spb.tacticul.repository.UserRepository;
import ru.spb.tacticul.config.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

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
        log.info("Аутентификация пользователя: {}", request.login());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.login(), request.password()));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.login());

        log.info("Пользователь {} успешно аутентифицирован", request.login());
        return new TokenResponse(jwtUtil.generateToken(userDetails.getUsername()));
    }
}
