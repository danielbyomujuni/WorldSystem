package de.butzlabben.world.gui.playeroption;

import de.butzlabben.inventory.DependListener;
import de.butzlabben.inventory.OrcItem;
import de.butzlabben.world.wrapper.WorldPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WorldEditStatus
        implements DependListener {
    private final WorldPlayer wp;

    public WorldEditStatus(WorldPlayer wp) {
        this.wp = wp;
    }

    public ItemStack getItemStack(Player p, WorldPlayer player) {
        return this.wp.canWorldedit() ? OrcItem.enabled.getItemStack(p, this.wp) : OrcItem.disabled.getItemStack(p, this.wp);
    }
}
