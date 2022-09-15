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

    /**
     * Creates the WorldSystemDatabaseObject from the given path
     * This Constructor is currently designed for tests
     * @param databaseFilePath
     */
    public WorldDatabase(String databaseFilePath) {
        this.dbFilePath = databaseFilePath;
        this.wsDataBase = getWorldSystemDatabase();
    }

    /**
     * Gets the current count of player data stored by WorldSystem
     * @return num of players with worldsystem data
     */
    public int getPlayerCount() {
        return wsDataBase.getPlayers();
    }

    /**
     * Adds a player to the database
     * @param playerUUID the uuid of the player to add
     */
    public void addPlayer(String playerUUID) {
        wsDataBase.addplayer(playerUUID);
        saveWSData();
    }


    //This Function is used by the constructor
    // to translate a json file into
    // a WorldSystemData Object
    private WorldSystemData getWorldSystemDatabase() {

        //init necessary local variables
        File wsDataFile = new File(this.dbFilePath);
        Gson gson = new Gson();
        WorldSystemData newDatabase = null;

        //Checks if the file exists if not create it
        if (!wsDataFile.exists()) {
            newDatabase = new WorldSystemData();
            saveWSData();
            return newDatabase;
        }

        //if the file exist read it
        try {
            Reader reader = Files.newBufferedReader(Paths.get(this.dbFilePath));
            newDatabase = gson.fromJson(reader, WorldSystemData.class);
            reader.close();
        } catch (IOException e) {
            //Something bad when really wrong
            //so the server admin needs to be notifyed
            //and the current db needs to be duplicated
            //into a special Directory

            //TODO Log Warning
            //TODO Copy current db file
        }

        //if the WorldSystemData object was never created
        //creates it
        //Causes: empty file, very bad error(like data Corruption)
        if (newDatabase == null) {
            newDatabase = new WorldSystemData();
        }

        return newDatabase;
    }

    //This Function saves the current
    //WorldSystemData Object to the json
    //File (It does every time a record is
    //changed to prevent data loss).
    private void saveWSData() {
        //Init all local variable need
        Gson gson = new Gson();
        File dataFile = new File(this.dbFilePath);

        //File the File Does not exist then create it
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                //Should Never Run
                throw new RuntimeException(e);
            }
        }

        //Convert the data into a json formated
        //string then write it to a file.
        try {
            FileWriter writer = new FileWriter(dataFile);
            gson.toJson(wsDataBase, writer);
            writer.close();
        } catch (IOException e) {

            //Should never run
            throw new RuntimeException(e);
        }
    }
}
