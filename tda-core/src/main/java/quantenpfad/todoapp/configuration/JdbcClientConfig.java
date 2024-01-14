package quantenpfad.todoapp.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * JDBC client configuration
 */
@Configuration
@Slf4j
@AllArgsConstructor
public class JdbcClientConfig {

    private final Environment env;

    /**
     * JdbcClient bean, that creates from DataSource
     */
    @Bean
    public JdbcClient jdbcClient(DataSource dataSource) {
        return JdbcClient.create(dataSource);
    }

    /**
     * @return temporary datasource, that can connect to DB, even if target database doesn't exist yet
     */
    private DriverManagerDataSource getTempDatasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url.base"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));

        return dataSource;
    }

    /**
     * @return temporary {@link JdbcClient}, so basic database checks can be performed
     */
    private JdbcClient createTempJdbcClient() {
        return JdbcClient.create(getTempDatasource());
    }

    /**
     * Migration strategy, that ensures target database exists, and migration can be performed
     */
    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            createDb();
            flyway.migrate();
        };
    }

    /**
     * Create target database if it's not exists
     */
    private void createDb() {
        JdbcClient tempJdbcClient = createTempJdbcClient();
        if (!isDatabaseCreated(tempJdbcClient)) {
            tempJdbcClient.sql(String.format("create database \"%s\"", env.getProperty("db.name"))).update();
            log.info("Database created!");
        }
    }

    /**
     * @return is target database exists
     */
    private boolean isDatabaseCreated(JdbcClient jdbcClient) {
        return jdbcClient.sql("select (select count(datname) from pg_database where datname=?) > 0")
                .param(env.getProperty("db.name"))
                .query(Boolean.class).single();
    }
}
