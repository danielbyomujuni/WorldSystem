package de.butzlabben.WorldSystem.data;

import lombok.SneakyThrows;

public class WorldData {
    private static WorldData con;
    private static SqlLiteDatabase core;

    private WorldData() {
        WorldData.core = SqlLiteDatabase.connect();
    }

    @SneakyThrows
    public static WorldData connect() {
        if (con == null) {
            con = new WorldData();
        }
        return con;
    }
}
