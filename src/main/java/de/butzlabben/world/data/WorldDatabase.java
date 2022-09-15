package de.butzlabben.world.data;

import com.google.gson.Gson;
import de.butzlabben.world.data.objects.PlayerData;
import de.butzlabben.world.data.objects.WorldSystemData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WorldDatabase {

    //This is Set to Static to allow for only one to prevent Data to be not saved;
    private static WorldSystemData wsDataBase;
    private String dbFilePath;

    public WorldDatabase(String databaseFilePath) {
        this.dbFilePath = databaseFilePath;
        this.wsDataBase = getWorldSystemDatabase();
    }

    public int getPlayerCount() {
        return wsDataBase.getPlayers();
    }

    public void addPlayer(String playerUUID) {
        wsDataBase.addplayer(playerUUID);
        saveWSData();
    }




    private WorldSystemData getWorldSystemDatabase() {
        File wsDataFile = new File(this.dbFilePath);
        Gson gson = new Gson();
        WorldSystemData newDatabase = null;


        if (!wsDataFile.exists()) {
            newDatabase = new WorldSystemData();
            saveWSData();
            return newDatabase;
        }

        try {
            Reader reader = Files.newBufferedReader(Paths.get(this.dbFilePath));
            newDatabase = gson.fromJson(reader, WorldSystemData.class);
            reader.close();
        } catch (IOException e) {
            //Log Warning
        }

        if (newDatabase == null) {
            newDatabase = new WorldSystemData();
        }

        return newDatabase;
    }

    private void saveWSData() {
        Gson gson = new Gson();

        File dataFile = new File(this.dbFilePath);
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                //Should Never Run
                throw new RuntimeException(e);
            }
        }
        try {
            FileWriter writer = new FileWriter(dataFile);
            gson.toJson(wsDataBase, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
