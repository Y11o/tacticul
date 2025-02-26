package ru.spb.tacticul.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.spb.tacticul.dto.authentication.SignInRequest;
import ru.spb.tacticul.dto.authentication.SignUpRequest;
import ru.spb.tacticul.dto.authentication.TokenResponse;
import ru.spb.tacticul.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "API для управления аутентификацией")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Регистрация нового пользователя (только администратором)")
    public ResponseEntity<TokenResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.signUp(request));
    }

    @PostMapping("/signin")
    @Operation(summary = "Вход в систему")
    public ResponseEntity<TokenResponse> signIn(@Valid @RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signIn(request));
    }
}