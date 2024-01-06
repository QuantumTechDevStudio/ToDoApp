package ru.todoapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.todoapp.model.RequestEntity;

import java.sql.Timestamp;
import java.util.Arrays;

/**
 * Repository for saving request information
 */
@Repository
@RequiredArgsConstructor
public class RequestRepository {
    public static final String SQL_SAVE_REQUEST = "INSERT INTO tda_request VALUES (?, ?, ?);";


    private final JdbcClient jdbcClient;

    /**
     * Saving request Information
     */
    public void save(RequestEntity requestEntity) {
        var requestTimestamp = new Timestamp(requestEntity.requestTimestamp().toEpochMilli());
        var params = Arrays.asList(
                requestEntity.requestUUID(),
                requestEntity.requestType().name(),
                requestTimestamp
        );

        jdbcClient
                .sql(SQL_SAVE_REQUEST)
                .params(params)
                .update();
    }
}
