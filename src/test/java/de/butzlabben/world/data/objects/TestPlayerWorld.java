package de.butzlabben.world.data.objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPlayerWorld {

  @Test
  public void testPlayerWorldInit() {
    PlayerWorld pw = new PlayerWorld(0);
    assertEquals(0,pw.getWorldNumber());
  }

  @Test
  public void testPlayerWorldInit2() {
    PlayerWorld pw = new PlayerWorld(6);
    assertEquals(6,pw.getWorldNumber());
  }
}
