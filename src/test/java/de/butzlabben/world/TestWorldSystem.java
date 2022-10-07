package de.butzlabben.world;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestWorldSystem
{
  @Test
  public void testMockInstance() {
    WorldSystem mock = new MockWorldSystem(null);
    assertEquals(mock, WorldSystem.getInstance());
  }

}
