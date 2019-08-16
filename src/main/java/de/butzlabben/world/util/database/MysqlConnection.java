package de.butzlabben.world.util.database;

import de.butzlabben.world.config.PluginConfig;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnection extends DatabaseConnection {

    private void connect(String host, String database, String port, String user, String password) {
        synchronized (lock) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("[WorldSystem | MySQL] Drivers are not working properly");
                return;
            }
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user="
                        + user + "&password=" + password);
            } catch (SQLException e) {
                System.out.println("[WorldSystem | MySQL] Failed to connect with given server:");
                e.printStackTrace();
            }
        }
    }

    public void connect() {
        connect(PluginConfig.getMysqlHost(), PluginConfig.getMysqlDatabaseName(), PluginConfig.getMysqlPort() + "",
                PluginConfig.getMysqlUser(), PluginConfig.getMysqlPassword());
    }
}
