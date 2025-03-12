package dataaccess;

import model.UserData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class MySQLUserDAO implements UserDAO {

    public MySQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public UserData createUser(UserData userData) throws DataAccessException {
        return null;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<UserData> listUsers() throws DataAccessException {
        return List.of();
    }

    @Override
    public void clear() {

    }

    private final String[] createStatements = {
        """
            CREATE TABLE IF NOT EXISTS users (
                `username` varchar(256) NOT NULL,
                `password` varchar(256) NOT NULL,
                `email` varchar(256) NOT NULL,
                PRIMARY KEY (`username`)
            )
        """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
