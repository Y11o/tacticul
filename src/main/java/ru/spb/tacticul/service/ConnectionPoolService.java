package ru.spb.tacticul.service;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionPoolService {

    private final DataSource dataSource;

    @Scheduled(fixedRate = 1800000) // 30 минут
    public void refreshConnections() {
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            log.info("Обновление соединений в пуле. Активных соединений: {}", hikariDataSource.getHikariPoolMXBean().getActiveConnections());
            
            try (Connection connection = hikariDataSource.getConnection()) {
                // Просто открываем и закрываем соединение для обновления пула
                log.info("Соединение успешно обновлено");
            } catch (SQLException e) {
                log.error("Ошибка при обновлении соединения", e);
            }
        }
    }
} 