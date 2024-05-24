package de.butzlabben.WorldSystem.data;

import de.butzlabben.world.WorldSystem;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.logging.Level;

public class SqlLiteDatabase {

    private static final String DATABASE_FILE = "plugins/WorldSystem/data.db";
    private static final String WS_WORLDS = "ws_worlds";

    private Connection connection;
    private Statement state;

    private static SqlLiteDatabase database;

    SqlLiteDatabase(String db_path) throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + db_path);
        this.state = this.connection.createStatement();
        this.construct_database();
    }

    @SneakyThrows
    public static SqlLiteDatabase connect() {
        try {
            if (database == null) {
                database = new SqlLiteDatabase(DATABASE_FILE);
            }
            return database;
        } catch (SQLException e) {

            WorldSystem.disable_plugin();
            return new SqlLiteDatabase(DATABASE_FILE); //doesn't create
        }
    }



    protected void void_query(String sql) throws SQLException {
        this.state.executeUpdate(sql);
    }

    protected ResultSet query(String sql) throws SQLException {
        return this.state.executeQuery(sql);
    }

    protected void construct_database() {
        try {
            //create the worlds table
            this.void_query(String.format("CREATE TABLE IF NOT EXISTS %s (world_id INTEGER PRIMARY KEY, player_uuid varchar(40), player_name varchar(20), last_loaded long);", WS_WORLDS));
        } catch (SQLException e) {
            WorldSystem.logger().log(Level.SEVERE, "[WorldSystem] Unable To create the SQLite Database");
            WorldSystem.logger().log(Level.SEVERE, e.getMessage());
            WorldSystem.disable_plugin();
        }
    }

    public void close() {
        try {
            this.state.close();
            this.connection.close();
        } catch (SQLException e) {

        }
    }
}
