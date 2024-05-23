package de.butzlabben.WorldSystem.data;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TestAbstractSqlLiteDatabase {

    @Test
    public void testContructDatabase() throws SQLException {
       MockDatabase db = new MockDatabase();
       assertTrue(db.does_table_exist("RealTable"));
       db.close();
    }
    @Test
    public void testNonExistantTable()  throws SQLException {
        MockDatabase db = new MockDatabase();
        assertFalse(db.does_table_exist("NotRealTable"));
        db.close();
    }
}
