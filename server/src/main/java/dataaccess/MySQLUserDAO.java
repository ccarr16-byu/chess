package dataaccess;

import model.UserData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static dataaccess.DatabaseManager.executeUpdate;

public class MySQLUserDAO implements UserDAO {

    public MySQLUserDAO() throws DataAccessException {
        String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
                `username` varchar(256) NOT NULL,
                `password` varchar(256) NOT NULL,
                `email` varchar(256) NOT NULL,
                PRIMARY KEY (`username`)
            )
            """
        };

        DatabaseManager.configureDatabase(createStatements);
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
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE users";
        executeUpdate(statement);
    }

}
