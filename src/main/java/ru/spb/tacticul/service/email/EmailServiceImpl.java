package ru.spb.tacticul.service.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendRecoveryEmail(String to, String login, String newPassword) {
        String subject = "Восстановление пароля";
        SimpleMailMessage message = getMailMessage(to, newPassword, subject);

        try {
            mailSender.send(message);
            log.info("Письмо с восстановлением пароля отправлено на {}", to);
        } catch (MailException e) {
            log.error("Ошибка отправки письма на {}: {}", to, e.getMessage());
            throw new RuntimeException("Ошибка отправки письма", e);
        }
    }

    private SimpleMailMessage getMailMessage(String to, String newPassword, String subject) {
        String text = String.format(
                """
                        Тактикул.РФ
                        
                        Ваш временный пароль - %s. Вы можете его использовать и далее, \
                        но рекомендуем его изменить при первом входе в аккаунт.""",
                newPassword
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }
}
