package ru.todoapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.todoapp.model.SaveTaskEntity;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

/**
 * Repository for any task related actions (addition and fetching for now)
 */
@Repository
@RequiredArgsConstructor
public class TaskRepository {
    public static final String SAVE_NEW_TASK = "INSERT INTO tda_task (description, datetime, user_uuid) VALUES (?,?,?)";

    private final JdbcClient jdbcClient;

    /**
     * Saves new task to DataBase
     */
    public void saveNewTask(SaveTaskEntity saveTaskEntity) {
        OffsetDateTime offsetDateTime = saveTaskEntity.datetime().atOffset(ZoneOffset.UTC);
        var params = Arrays.asList(
                saveTaskEntity.description(),
                offsetDateTime,
                saveTaskEntity.userUUID());
        jdbcClient.sql(SAVE_NEW_TASK).params(params).update();
    }
}