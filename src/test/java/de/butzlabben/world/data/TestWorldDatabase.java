package de.butzlabben.world.data;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class TestWorldDatabase {

    /**
     * Before the Test this function cleans out the Working Directory
     * This allows us to view the results of our prevous tests as needed
     * but then allows us to run the tests Fresh.
     */
    @BeforeAll
    static void CleanLastTest() {
        File workingDir = new File("src\\TestFiles\\workingDir\\");
        for (File file : workingDir.listFiles()) {
            file.delete();
        }
    }

    /**
     * This Tests the Basis Initaliaztion of the DataBase from nothing
     */
    @Test
    public void testDatabaseInitalizationFromNoFile() {
        final String path = "src\\TestFiles\\workingDir\\dataBaseInitTestFromNoFile.json";

        WorldDatabase wb = new WorldDatabase(path);
        assertEquals(0, wb.getPlayerCount());
    }

    /**
     * This Test Creates the new Object and Verifies that is can save data Properly;
     * @throws FileNotFoundException
     */
    @Test
    public void testDatabaseInitalizationCreateValidFile() throws FileNotFoundException {
        final String path = "src\\TestFiles\\workingDir\\dataBaseInitTestCreateValidFile.json";

        WorldDatabase wb = new WorldDatabase(path);
        wb.addPlayer("BlankUUID");


        String input = new Scanner(new File(path)).nextLine();
        assertEquals("{\"players\":{\"BlankUUID\":{\"playerWorlds\":[]}}}", input);
    }


    /**
     * This Test to see if it can create to database when loading from a blank File
     */
    @Test
    public void testDatabaseInitalizationWithExistingEmptyFile() {
        final String path = "src\\TestFiles\\ExistingEmptyFileInit.json";

        WorldDatabase wb = new WorldDatabase(path);
        assertEquals(0, wb.getPlayerCount());
    }

    /**
     * This Test to see if it can create to database when loading from a populated File
     */
    @Test
    public void testDatabaseInitalizationWithPopulatedEmptyFile() throws FileNotFoundException {
        final String path = "src\\TestFiles\\ExistingPopulatedFileInit.json";

        WorldDatabase wb = new WorldDatabase(path);


        String input = new Scanner(new File(path)).nextLine();
        assertEquals("{\"players\":{\"BlankUUID\":{\"playerWorlds\":[]}}}", input);
        assertEquals(1, wb.getPlayerCount());
    }
}
