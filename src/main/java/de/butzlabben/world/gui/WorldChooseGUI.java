package de.butzlabben.world.gui;

import de.butzlabben.inventory.OrcClickListener;
import de.butzlabben.inventory.OrcInventory;
import de.butzlabben.inventory.OrcItem;
import de.butzlabben.world.config.GuiConfig;
import de.butzlabben.world.wrapper.WorldTemplate;
import de.butzlabben.world.wrapper.WorldTemplateProvider;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * @author Butzlabben
 * @since 15.12.2018
 */
public class WorldChooseGUI extends OrcInventory {

    private final static String path = "worldchoose.";

    public WorldChooseGUI() {
        this(null, null);
    }

    public WorldChooseGUI(Player player) {
        this(null, player);
    }

    public WorldChooseGUI(Consumer<WorldTemplate> onClick, Player player) {
        super(GuiConfig.getTitle(GuiConfig.getConfig(), "worldchoose"), GuiConfig.getRows("worldchoose"),
                GuiConfig.isFill("worldchoose"));

        for (WorldTemplate template : WorldTemplateProvider.getInstace().getTemplates()) {
            // Check if player has permission to see template
            if (template.getPermission() != null && !player.hasPermission(template.getPermission()))
                continue;

            OrcItem icon = template.getIcon();
            if (onClick != null)
                icon.setOnClick((p, inv, item) -> {
                    p.closeInventory();
                    onClick.accept(template);
                });
            int slot = template.getSlot();
            addItem(slot, icon);
        }

        if (GuiConfig.isEnabled(path + "back")) {
            OrcItem back = OrcItem.back.clone();
            back.setOnClick((p, inv, item) -> {
                p.closeInventory();
            });
            addItem(GuiConfig.getSlot(path + "back"), back);
        }
    }

    public static void letChoose(Player player, Consumer<WorldTemplate> template) {
        player.openInventory(new WorldChooseGUI(template, player).getInventory(player));
    }

    public static void letChoose(Player player) {
        player.openInventory(new WorldChooseGUI(player).getInventory(player));
    }

    public void loadItem(String subpath, OrcClickListener listener) {
        if (GuiConfig.isEnabled(path + subpath) == false)
            return;
        OrcItem item = GuiConfig.getItem(path + subpath);
        if (item != null) {
            item.setOnClick(listener);
            addItem(GuiConfig.getSlot(path + subpath), item);
        }
    }

    public void loadItem(String subpath) {
        loadItem(subpath, null);
    }

    public boolean canOpen(Player p) {
        return true;
    }
}