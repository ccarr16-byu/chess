package dataaccess;

import chess.ChessGame;
import model.AuthData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static dataaccess.DatabaseManager.executeUpdate;

public class MySQLAuthDAO implements AuthDAO {

    public MySQLAuthDAO() throws DataAccessException {
        String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auths (
                `authToken` varchar(256) NOT NULL,
                `username` varchar(256) NOT NULL,
                PRIMARY KEY (`authToken`)
            )
            """
        };

        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public AuthData createAuth(AuthData authData) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public Collection<AuthData> listAuths() throws DataAccessException {
        return List.of();
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auths";
        executeUpdate(statement);
    }

}
