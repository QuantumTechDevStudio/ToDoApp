package ru.todoapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.todoapp.model.UserEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Репозиторий сохранения и доступа к пользователям
 */
@Repository
@RequiredArgsConstructor
public class UserRepository {
    private static final String SQL_SAVE_REQUEST = "INSERT INTO tda_user VALUES (?, ?, ?);";

    private static final String SQL_CHECK_EXISTING = "SELECT * FROM tda_user WHERE uuid = ?";


    private final JdbcClient jdbcClient;

    /**
     * Добавление нового пользователя
     */
    public void saveUser(UserEntity userEntity) {
        var params = Arrays.asList(userEntity.userUUID(), userEntity.name(), userEntity.surname());

        jdbcClient.sql(SQL_SAVE_REQUEST).params(params).update();
    }

    /**
     * Выполнение проверки существования пользователя c UUID
     *
     * @param uuid - UUID нового пользователя
     * @return true если пользователь существует, false если пользователся с предоставленным UUID нет
     */
    public boolean exists(String uuid) {
        return jdbcClient.sql(SQL_CHECK_EXISTING).params(uuid).query(new UserRowMapper()).list().size() > 0;
    }

    /**
     * Правильно мапит результаты из БД в UserEntity
     */
    private static class UserRowMapper implements RowMapper<UserEntity> {
        @Override
        public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new UserEntity(rs.getString("uuid"),
                    rs.getString("name"),
                    rs.getString("surname"));
        }
    }
}
