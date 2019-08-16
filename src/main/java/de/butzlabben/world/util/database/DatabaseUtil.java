package de.butzlabben.world.util.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseUtil {

    ResultSet executeQuery(PreparedStatement preparedStatement) throws SQLException;

    int executeUpdate(PreparedStatement preparedStatement) throws SQLException;

    PreparedStatement prepareStatement(String sql) throws SQLException;

    void close();

    void connect();

    boolean isConnectionAvailable();
}
