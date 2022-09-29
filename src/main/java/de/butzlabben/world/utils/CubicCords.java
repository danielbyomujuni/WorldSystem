package de.butzlabben.world.utils;

public class CubicCords extends PlanerCords {

  private int z;

  public CubicCords(int x, int y, int z)
  {
    super(x, y);
    this.z = z;
  }

  public int getZ() {
    return this.z;
  }
}
