package de.butzlabben.world.data.objects;

import de.butzlabben.world.utils.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPlayerWorld {

  /**
   * Tests the basic initiazlization of a new World with an id of 0
   */
  @Test
  public void testPlayerWorldInit() {
    PlayerWorld pw = new PlayerWorld(0);
    assertEquals(0,pw.getWorldNumber());
  }

  /**
   * Test the initiazlization of a world with a index other than 0
   */
  @Test
  public void testPlayerWorldInit2() {
    PlayerWorld pw = new PlayerWorld(6);
    assertEquals(6,pw.getWorldNumber());
  }

  @Test
  public void testSavingPlayerLocationOfSinglePlayer()
  {
    PlayerWorld pw = new PlayerWorld(6);
    pw.addPlayerLocationData("Blank", new Location(0,20,5));

    assertEquals(0, pw.getLocationOfPlayer("Blank").getX());
    assertEquals(20, pw.getLocationOfPlayer("Blank").getY());
    assertEquals(5, pw.getLocationOfPlayer("Blank").getZ());
  }

  @Test
  public void testSavingPlayerLocationOfThreePlayers()
  {
    PlayerWorld pw = new PlayerWorld(6);
    pw.addPlayerLocationData("Blank", new Location(0,20,5));
    pw.addPlayerLocationData("Blank2", new Location(20,30,400));
    pw.addPlayerLocationData("Blank3", new Location(4,28,9));

    assertEquals(0, pw.getLocationOfPlayer("Blank").getX());
    assertEquals(20, pw.getLocationOfPlayer("Blank").getY());
    assertEquals(5, pw.getLocationOfPlayer("Blank").getZ());

    assertEquals(20, pw.getLocationOfPlayer("Blank2").getX());
    assertEquals(30, pw.getLocationOfPlayer("Blank2").getY());
    assertEquals(400, pw.getLocationOfPlayer("Blank2").getZ());

    assertEquals(4, pw.getLocationOfPlayer("Blank3").getX());
    assertEquals(28, pw.getLocationOfPlayer("Blank3").getY());
    assertEquals(9, pw.getLocationOfPlayer("Blank3").getZ());
  }

  @Test
  public void testDefaultWorldSpawn() {
    PlayerWorld pw = new PlayerWorld(0);
    assertEquals(0, pw.getWorldSpawn().getX());
    assertEquals(60, pw.getWorldSpawn().getY());
    assertEquals(0, pw.getWorldSpawn().getZ());
  }

  @Test
  public void testAdjustedWorldSpawn() {
    PlayerWorld pw = new PlayerWorld(0);
    assertEquals(0, pw.getWorldSpawn().getX());
    assertEquals(60, pw.getWorldSpawn().getY());
    assertEquals(0, pw.getWorldSpawn().getZ());

    pw.setWorldSpawn(new Location(20 ,80, 400));

    assertEquals(20, pw.getWorldSpawn().getX());
    assertEquals(80, pw.getWorldSpawn().getY());
    assertEquals(400, pw.getWorldSpawn().getZ());


  }

}
