package de.butzlabben.WorldSystem.data;

import de.butzlabben.world.config.PluginConfig;

import java.sql.*;

public abstract class AbstractSqlLiteDatabase {

    private Connection connection;
    private Statement state;

    public AbstractSqlLiteDatabase() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + PluginConfig.getSqliteFile());
        this.construct_database();
    }

    public AbstractSqlLiteDatabase(String db_path) throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + db_path);
        this.state =  this.connection.createStatement();
        this.construct_database();
    }

    protected ResultSet query(String sql) throws SQLException {
        return this.state.executeQuery(sql);
    }

    protected abstract void construct_database();

    public void close() {
        try {
            this.state.close();
            this.connection.close();
        } catch (SQLException e) {

        }
    }
}
