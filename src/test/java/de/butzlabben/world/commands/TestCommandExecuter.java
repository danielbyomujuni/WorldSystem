package de.butzlabben.world.commands;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import de.butzlabben.world.WorldSystem;
import de.butzlabben.world.exceptions.InvaildCommandException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCommandExecuter
{


  @Test
  public void testExecuterInvalidCommand() {
    WorldSystemCommandExecuter cmdEx = new WorldSystemCommandExecuter();

    assertThrows(InvaildCommandException.class, () -> {cmdEx.executeCommand("test", null);});
  }

  @Test
  public void testExecuterAddCMD() throws InvaildCommandException
  {
    WorldSystemCommandExecuter cmdEx = new WorldSystemCommandExecuter();

    cmdEx.addCommand("test", new MockCommand());

    assertTrue(cmdEx.executeCommand("test", null));
  }

  @Test
  public void testExecuterMultipleCommands() throws InvaildCommandException
  {
    WorldSystemCommandExecuter cmdEx = new WorldSystemCommandExecuter();

    cmdEx.addCommand("test1", new MockCommand());
    cmdEx.addCommand("test2", new MockCommand());
    cmdEx.addCommand("test3", new MockCommand());

    assertTrue(cmdEx.executeCommand("test1", null));

    assertTrue(cmdEx.executeCommand("test2", null));

    assertTrue(cmdEx.executeCommand("test3", null));

    assertTrue(cmdEx.executeCommand("test2", null));
  }

}
