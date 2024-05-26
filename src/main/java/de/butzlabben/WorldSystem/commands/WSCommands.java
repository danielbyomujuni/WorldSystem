package de.butzlabben.WorldSystem.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import de.butzlabben.world.config.MessageConfig;
import org.bukkit.entity.Player;

@CommandAlias("ws-new")
public class WSCommands extends BaseCommand {
    @Subcommand("create")
    //@Syntax("")
    //@Description("Lists all of your or another players residences.")
    public static void create_world(Player player, String[] args) {
        player.sendMessage(MessageConfig.getNotOnWorld());
    }

}
