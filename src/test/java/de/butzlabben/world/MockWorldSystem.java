package de.butzlabben.world;

import org.apache.commons.io.FileUtils;

import java.io.*;

public class MockWorldSystem extends WorldSystem
{
  private File configFile;

  public MockWorldSystem(File cfg) {
    activeInst = this;
    configFile = cfg;
  }

  public MockWorldSystem() {
    activeInst = this;
    configFile = null;
  }

  @Override
  public void saveDefaultConfig() {
    File source = new File("src/main/resources/config.yml");
    try {
      FileUtils.copyFile(source, configFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public InputStream getResource(String filename) {
    try
    {
      return new FileInputStream("src/main/resources/" + filename);
    }
    catch (FileNotFoundException e)
    {
      return null;
    }
  }
}
