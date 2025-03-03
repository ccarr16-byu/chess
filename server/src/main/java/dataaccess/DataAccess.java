package dataaccess;

import java.util.Collection;

public interface DataAccess {
    Object add(Object object) throws DataAccessException;

    Object get(Object object) throws DataAccessException;

    Collection<Object> list() throws DataAccessException;

    void delete(Object object) throws DataAccessException;

    void deleteAll() throws DataAccessException;
}
