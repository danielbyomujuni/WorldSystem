package de.butzlabben.world.data.objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestWorldSystemData {

  @Test
  public void testWorldSystemDataInit() {
    WorldSystemData wsd = new WorldSystemData();

    assertEquals(0, wsd.getPlayers());
  }

  @Test
  public void testAddPlayer() {
    WorldSystemData wsd = new WorldSystemData();
    assertEquals(0, wsd.getPlayers());

    wsd.addplayer("Blank_UUID");
    assertEquals(1, wsd.getPlayers());

  }

  @Test
  public void testAddMultiplePlayers() {
    WorldSystemData wsd = new WorldSystemData();
    assertEquals(0, wsd.getPlayers());

    wsd.addplayer("Blank_UUID");
    assertEquals(1, wsd.getPlayers());

    for (int i = 0; i < 5; i++) {
      wsd.addplayer("Blank_UUID" + i);
    }
    assertEquals(6, wsd.getPlayers());

  }


  @Test
  public void testAddDulpicatePlayer() {
    WorldSystemData wsd = new WorldSystemData();
    assertEquals(0, wsd.getPlayers());

    wsd.addplayer("Blank_UUID");
    assertEquals(1, wsd.getPlayers());

    wsd.addplayer("Blank_UUID");
    assertEquals(1, wsd.getPlayers());
  }

  @Test
  public void testAddWorldToPlayer() {
    WorldSystemData wsd = new WorldSystemData();
    assertEquals(0, wsd.getPlayers());

    wsd.addplayer("Blank_UUID");
    wsd.addWorldToPlayer("Blank_UUID", new PlayerWorld(wsd.getPlayer("Blank_UUID").getWorldCount()));

    assertEquals(1, wsd.getPlayer("Blank_UUID").getWorldCount());
  }


  @Test
  public void testAddMultipleWorldsToPlayer() {
    WorldSystemData wsd = new WorldSystemData();
    wsd.addplayer("Blank_UUID");
    assertEquals(1, wsd.getPlayers());

    wsd.addWorldToPlayer("Blank_UUID", new PlayerWorld(wsd.getPlayer("Blank_UUID").getWorldCount()));
    assertEquals(1, wsd.getPlayer("Blank_UUID").getWorldCount());

    for (int i = 0; i < 5; i++) {
      wsd.addWorldToPlayer("Blank_UUID", new PlayerWorld(wsd.getPlayer("Blank_UUID").getWorldCount()));
    }
    assertEquals(6, wsd.getPlayer("Blank_UUID").getWorldCount());

  }
}
