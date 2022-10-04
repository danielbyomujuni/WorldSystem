package de.butzlabben.world.utils;

public class Location extends Location2D
{

  private int y;

  public Location(int x, int y, int z)
  {
    super(x, z);
    this.y = y;
  }

  public int getY() {
    return this.y;
  }
}
