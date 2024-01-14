package quantenpfad.todoapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import quantenpfad.todoapp.model.UserEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Repository for saving and accessing users
 */
@Repository
@RequiredArgsConstructor
public class UserRepository {
    private static final String SQL_SAVE_REQUEST = "INSERT INTO tda_user VALUES (?, ?, ?);";

    private static final String SQL_CHECK_EXISTING = "SELECT * FROM tda_user WHERE uuid = ?";


    private final JdbcClient jdbcClient;

    /**
     * Saving new user
     */
    public void saveUser(UserEntity userEntity) {
        var params = Arrays.asList(userEntity.userUUID(), userEntity.name(), userEntity.surname());

        jdbcClient.sql(SQL_SAVE_REQUEST).params(params).update();
    }

    /**
     * Check for already registered user with provided UUID
     *
     * @param uuid - UUID provided for check
     * @return true if user already registered, false if no user with provided UUID was found
     */
    public boolean exists(String uuid) {
        return jdbcClient.sql(SQL_CHECK_EXISTING).params(uuid).query(new UserRowMapper()).list().size() > 0;
    }

    /**
     * Mapping DataBase rows to UserEntity
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
