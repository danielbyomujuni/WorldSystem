package de.butzlabben.world.data.objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestPlayerData {

  @Test
  public void testPlayerDataInit() {
    PlayerData pd = new PlayerData();

    assertEquals(0, pd.getWorldCount());
  }

  @Test
  public void testAddWorld() {
    PlayerData pd = new PlayerData();

    pd.addWorld(new PlayerWorld(pd.getWorldCount()));
    assertEquals(1, pd.getWorldCount());

    for (int i = 0; i < 5; i++) {
      pd.addWorld(new PlayerWorld(pd.getWorldCount()));
    }
    assertEquals(6, pd.getWorldCount());
  }

  @Test
  public void testGetWorldAtIndex() {
    PlayerData pd = new PlayerData();

    pd.addWorld(new PlayerWorld(pd.getWorldCount()));

    assertEquals(1, pd.getWorldCount());
    assertEquals(0, pd.getWorldAt(0).getWorldNumber());
  }
}
