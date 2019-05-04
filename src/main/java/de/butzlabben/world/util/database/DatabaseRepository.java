package de.butzlabben.world.util.database;

import de.butzlabben.world.config.PluginConfig;
import lombok.Getter;

public class DatabaseRepository {
    @Getter
    private static DatabaseRepository instance = new DatabaseRepository();

    @Getter
    private final DatabaseUtil util;

    private DatabaseRepository() {
        String dbType = PluginConfig.getDatabaseType();
        if (dbType.equalsIgnoreCase("sqlite"))
            util = new SqliteConnection();
        else if (dbType.equalsIgnoreCase("mysql"))
            util = new MysqlConnection();
        else {
            throw new IllegalArgumentException("Unknown database type: " + dbType);
        }
    }
}
