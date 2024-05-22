package de.butzlabben.world.util.database;

import lombok.Getter;

import java.sql.*;
import java.util.logging.Level;
import de.butzlabben.world.WorldSystem;



public abstract class DatabaseConnection implements DatabaseUtil {

    final Object lock = new Object();
    @Getter
    Connection connection;

    public void close() {
        synchronized (lock) {
            try {
                if (connection == null || connection.isClosed()) {
                    WorldSystem.logger().log(Level.SEVERE,"[WorldSystem | DB] Connection does not exist or was already closed");
                    return;
                }
                connection.close();
            } catch (SQLException e) {
                System.out.println("[WorldSystem | DB] Connection could not be closed");
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

    @Override
    public boolean isConnectionAvailable() {
        return connection != null;
    }
}
