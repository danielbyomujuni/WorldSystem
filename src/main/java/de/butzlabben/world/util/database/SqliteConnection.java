package de.butzlabben.world.util.database;

import de.butzlabben.world.config.PluginConfig;
import de.butzlabben.world.util.VersionUtil;
import org.bukkit.Bukkit;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class SqliteConnection extends DatabaseConnection {

    private void connect(String file) {
        if (VersionUtil.getVersion() <= 8) {
            Bukkit.getLogger().log(Level.SEVERE, "[WorldSystem | SQLite] ========================================================");
            Bukkit.getLogger().log(Level.SEVERE, "[WorldSystem | SQLite] SQLite is not available in 1.8.");
            Bukkit.getLogger().log(Level.SEVERE, "[WorldSystem | SQLite] Please consider using MySQL or disable the use_last_location option");
            Bukkit.getLogger().log(Level.SEVERE, "[WorldSystem | SQLite] ========================================================");
            return;
        }

        synchronized (lock) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                Bukkit.getLogger().log(Level.SEVERE, "[WorldSystem | SQLite] Drivers are not working properly");
                return;
            }
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + file);
                Bukkit.getLogger().log(Level.INFO, "[WorldSystem | SQLite] Connected to local file database");
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.SEVERE, "[WorldSystem | SQLite] Failed to connect with given server:");
                e.printStackTrace();
            }
        }
    }

    public void connect() {
        connect(PluginConfig.getSqliteFile());
    }
}
