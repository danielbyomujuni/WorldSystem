package de.butzlabben.world.wrapper;

import de.butzlabben.inventory.OrcItem;
import de.butzlabben.world.config.GuiConfig;

/**
 * @author Butzlabben
 * @since 15.12.2018
 */
public class WorldTemplate {

    private final String name;
    private final String permission;
    private final OrcItem icon;
    private final int slot;
    private final int cost;

    public WorldTemplate(String name, String permission, int cost) {
        this.name = name;
        this.permission = permission;
        this.cost = cost;

        this.icon = GuiConfig.getItem("worldchoose." + name);
        this.slot = GuiConfig.getSlot("worldchoose." + name);

        icon.setOnClick((p, inv, item) -> {
            p.closeInventory();
            p.chat("/ws get " + name);
        });
    }

    public int getSlot() {
        return slot;
    }

    public int getCost() {
        return cost;
    }

    public OrcItem getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return "plugins/WorldSystem/worldsources/" + name;
    }

    public String getPermission() {
        return permission;
    }
}
