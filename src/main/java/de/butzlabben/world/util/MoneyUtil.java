package de.butzlabben.world.util;

import com.google.common.base.Preconditions;
import de.butzlabben.world.config.PluginConfig;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

/**
 * Little util class for dealing with money with vault
 * Used for #15
 */
public class MoneyUtil {
    private static Object economy = null;

    static {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            try {
                RegisteredServiceProvider<Economy> service = Bukkit.getServicesManager().getRegistration(Economy.class);
                if (service != null)
                    economy = service.getProvider();
            } catch (Exception ignored) {
            }

        }

        if (economy == null)
            Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix() + "Couldn't find a Vault Economy extension");
    }

    private MoneyUtil() {
    }

    public static void removeMoney(UUID uuid, int money) {
        Preconditions.checkNotNull(uuid);
        Preconditions.checkNotNull(economy);
        Preconditions.checkArgument(money > 0, "Money must not be negative");
        OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
        Economy economy = (Economy) MoneyUtil.economy;
        EconomyResponse response = economy.withdrawPlayer(op, money);
        if (!response.transactionSuccess()) {
            Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix() + "§cTransaction failure for removing " + money + " from " + op.getName());
            Bukkit.getConsoleSender().sendMessage(PluginConfig.getPrefix() + "§cError message: " + response.errorMessage);
        }
    }

    public static boolean hasMoney(UUID uuid, int money) {
        Preconditions.checkNotNull(uuid);
        Preconditions.checkNotNull(economy);
        Preconditions.checkArgument(money > 0, "Money must not be negative");
        OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
        Economy economy = (Economy) MoneyUtil.economy;
        return economy.getBalance(op) >= money;
    }
}
