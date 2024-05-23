package de.butzlabben.WorldSystem.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MockDatabase extends AbstractSqlLiteDatabase {

    public MockDatabase() throws SQLException {
        super("./mock.db");
    }

    @Override
    protected void construct_database() {
        try {
            this.query("CREATE TABLE IF NOT EXISTS RealTable (id INTEGER PRIMARY KEY, quantity INTEGER);").close();
        } catch (SQLException e) {
            //
        }
    }

    public boolean does_table_exist(String Table_Name) throws SQLException {
        ResultSet res = this.query(String.format("SELECT count(*) FROM sqlite_master WHERE type='table' AND name = '%s';", Table_Name));
        res.next();
        int count = res.getInt("count(*)");
        System.out.println(count);
        return (count == 1);
    }
}
