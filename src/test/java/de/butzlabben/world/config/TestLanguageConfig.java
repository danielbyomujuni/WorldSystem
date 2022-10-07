package de.butzlabben.world.config;

import de.butzlabben.world.MockWorldSystem;
import de.butzlabben.world.WorldSystem;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLanguageConfig
{
  @BeforeAll
  static void CleanLastTest() throws IOException
  {
    FileUtils.cleanDirectory(new File("TestFiles/workingDir/"));
  }

  @Test
  public void testInitEN() {
    File languages = new File("TestFiles/en.yml");

    //Fails if something went Wrong
    LanguageConfig.checkConfig(languages);
  }

  @Test
  public void testInitES() {
    File languages = new File("TestFiles/es.yml");

    //Fails if something went Wrong
    LanguageConfig.checkConfig(languages);
  }

  @Test
  public void testInitENnoExist() {
    WorldSystem mock = new MockWorldSystem();
    File languages = new File("TestFiles/workingDir/en.yml");

    //Fails if something went Wrong
    LanguageConfig.checkConfig(languages);
  }

  @Test
  public void testHelpEN() {
    File languages = new File("TestFiles/en.yml");

    //Fails if something went Wrong
    LanguageConfig.checkConfig(languages);

    assertEquals(16,LanguageConfig.getCommandHelp().size());

    assertEquals(      "/ws get §8- §7Will give you a world",LanguageConfig.getCommandHelp().get(0));
  }

//  @Test
//  public void testNoPermEN() {
//    File languages = new File("TestFiles/en.yml");
//
//    //Fails if something went Wrong
//    LanguageConfig.checkConfig(languages);
//
//    assertEquals(      "§cYou do not have permission to that command!", LanguageConfig.getNoPermission());
//  }
}
