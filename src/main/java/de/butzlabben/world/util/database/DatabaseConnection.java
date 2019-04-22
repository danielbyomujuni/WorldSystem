package de.butzlabben.world.util.database;

import java.sql.*;

public abstract class DatabaseConnection implements DatabaseUtil {
    protected Connection connection;
    protected Object lock = new Object();

    public void close() {
        synchronized (lock) {
            try {
                if (connection == null || connection.isClosed()) {
                    System.err.println("[WorldSystem|DB] Connection does not exist or was already closed");
                    return;
                }
                connection.close();
            } catch (SQLException e) {
                System.out.println("[WorldSystem|DB] Connection could not be closed");
                e.printStackTrace();
            }
        }
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        synchronized (lock) {
            if (connection == null || connection.isClosed())
                connect();
            return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        }
    }

    public ResultSet executeQuery(PreparedStatement ps) throws SQLException {
        synchronized (lock) {
            if (connection == null || connection.isClosed())
                connect();
            return ps.executeQuery();
        }
    }

    public int executeUpdate(PreparedStatement ps) throws SQLException {
        synchronized (lock) {
            if (connection == null || connection.isClosed())
                connect();
            return ps.executeUpdate();
        }
    }

    public Connection getConnection() throws SQLException {
        synchronized (lock) {
            if (connection == null || connection.isClosed())
                connect();
            return connection;
        }
    }
}
