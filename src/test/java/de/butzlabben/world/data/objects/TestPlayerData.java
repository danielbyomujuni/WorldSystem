package de.butzlabben.world.data.objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestPlayerData {

  /**
   * Tests the Basic Initalization of a PlayerData Object
   */
  @Test
  public void testPlayerDataInit() {
    PlayerData pd = new PlayerData();

    assertEquals(0, pd.getWorldCount());
  }

  /**
   * Tests adding one world to the player
   */
  @Test
  public void testAddWorld() {
    PlayerData pd = new PlayerData();

    pd.addWorld(new PlayerWorld(pd.getWorldCount()));
    assertEquals(1, pd.getWorldCount());
  }

  /**
   * Tests adding multiple worlds to the player
   */
  @Test
  public void testAddMultipleWorlds() {
    PlayerData pd = new PlayerData();

    for (int i = 0; i < 5; i++) {
      pd.addWorld(new PlayerWorld(pd.getWorldCount()));
    }
    assertEquals(5, pd.getWorldCount());
  }

  /**
   * Tests geting a world at a specified index
   */
  @Test
  public void testGetWorldAtIndex() {
    PlayerData pd = new PlayerData();

    pd.addWorld(new PlayerWorld(pd.getWorldCount()));

    assertEquals(1, pd.getWorldCount());
    assertEquals(0, pd.getWorldAt(0).getWorldNumber());
  }
}
