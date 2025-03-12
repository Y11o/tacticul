package ru.spb.tacticul.service.email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> mailMessageCaptor;

    private final String fromEmail = "noreply@example.com";
    private final String toEmail = "user@example.com";
    private final String login = "testuser";
    private final String newPassword = "newPassword123";

    @BeforeEach
    void setUp() {
        try {
            var field = EmailServiceImpl.class.getDeclaredField("fromEmail");
            field.setAccessible(true);
            field.set(emailService, fromEmail);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось установить значение fromEmail в EmailServiceImpl", e);
        }
    }

    @Test
    void sendRecoveryEmail_Success() {
        emailService.sendRecoveryEmail(toEmail, login, newPassword);

        verify(mailSender).send(mailMessageCaptor.capture());
        SimpleMailMessage capturedMessage = mailMessageCaptor.getValue();

        assertEquals(fromEmail, capturedMessage.getFrom());
        assertEquals(toEmail, Objects.requireNonNull(capturedMessage.getTo())[0]);
        assertEquals("Восстановление пароля", capturedMessage.getSubject());
        assertTrue(capturedMessage.getText().contains(newPassword));
    }

    @Test
    void sendRecoveryEmail_ShouldThrowException_WhenMailSendingFails() {
        doThrow(new MailException("Ошибка отправки") {}).when(mailSender).send(any(SimpleMailMessage.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                emailService.sendRecoveryEmail(toEmail, login, newPassword));

        assertTrue(exception.getMessage().contains("Ошибка отправки письма"),
                "Ожидалось, что сообщение исключения содержит 'Ошибка отправки письма'");
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
