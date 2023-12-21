package ru.todoapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;

/**
 * Конфигурация JDBC клиента
 */
@Configuration
public class JdbcClientConfig {
    /**
     * Бин JdbcClient, создаваемый из DataSource
     */
    @Bean
    public JdbcClient jdbcClient(DataSource dataSource) {
        return JdbcClient.create(dataSource);
    }
}
