package ru.spb.tacticul.service.email;

public interface EmailService {

    void sendRecoveryEmail(String to, String login, String newPassword);
}