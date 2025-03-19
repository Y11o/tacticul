package ru.spb.tacticul.service;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import ru.spb.tacticul.dto.authentication.RecoveryRequest;
import ru.spb.tacticul.dto.authentication.SignInRequest;
import ru.spb.tacticul.dto.authentication.SignUpRequest;
import ru.spb.tacticul.dto.authentication.TokenResponse;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.model.User;
import ru.spb.tacticul.repository.UserRepository;
import ru.spb.tacticul.config.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.spb.tacticul.service.email.EmailService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthenticationService authenticationService;


    @Captor
    private ArgumentCaptor<User> userCaptor;

    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;
    private RecoveryRequest recoveryRequest;
    private User user;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest("testuser", "test@example.com", "password");
        signInRequest = new SignInRequest("testuser", "password");
        recoveryRequest = new RecoveryRequest("test@example.com");

        user = new User();
        user.setLogin("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
    }

    @Test
    void signUp_Success() {
        when(userRepository.findByLogin(signUpRequest.login())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpRequest.password())).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(user.getLogin())).thenReturn("jwtToken");
        when(userRepository.save(any(User.class))).thenReturn(user);

        TokenResponse response = authenticationService.signUp(signUpRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.token());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signUp_UserAlreadyExists() {
        when(userRepository.findByLogin(signUpRequest.login())).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> authenticationService.signUp(signUpRequest));
        assertEquals("Пользователь с таким логином уже существует", exception.getMessage());
    }

    @Test
    void signIn_Success() {
        when(userRepository.findByEmail(signInRequest.credentials())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtUtil.generateToken(user.getLogin())).thenReturn("jwtToken");

        TokenResponse response = authenticationService.signIn(signInRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.token());
    }

    @Test
    void recoveryPassword_Success() {
        when(userRepository.findByEmail(recoveryRequest.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");

        authenticationService.recoveryPassword(recoveryRequest);

        verify(userRepository).save(userCaptor.capture());
        User updatedUser = userCaptor.getValue();

        assertNotNull(updatedUser.getPassword());
        assertNotEquals("encodedPassword", updatedUser.getPassword());
        verify(emailService).sendRecoveryEmail(eq(user.getEmail()), eq(user.getLogin()), anyString());
    }

    @Test
    void recoveryPassword_UserNotFound() {
        when(userRepository.findByEmail(recoveryRequest.email())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> authenticationService.recoveryPassword(recoveryRequest));

        assertEquals("Пользователь с таким email: test@example.com не найден", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(emailService, never()).sendRecoveryEmail(anyString(), anyString(), anyString());
    }
}

