package de.butzlabben.world.util;

import de.butzlabben.world.config.PluginConfig;

import java.sql.*;

public class SqliteConnection extends DatabaseConnection {

    public void connect(String file) {
        synchronized (lock) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("[WorldSystem|MySQL] Drivers are not working properly");
                return;
            }
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + file);
            } catch (SQLException e) {
                System.out.println("[WorldSystem|MySQL] Failed to connect with given server:");
                e.printStackTrace();
            }
        }
    }

    public void connect() {
        connect(PluginConfig.getSqliteFile());
    }
}
