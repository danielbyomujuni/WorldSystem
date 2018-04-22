package de.butzlabben.world.gui;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.entity.Player;

import de.butzlabben.world.config.GuiConfig;
import de.butzlabben.world.config.WorldConfig2;
import de.butzlabben.world.wrapper.SystemWorld;

/**
 * @author Butzlabben
 * @since 20.04.2018
 */
public class PlayersGUIManager {

	private static final int headsPerInv = GuiConfig.getConfig().getInt("options.players.player_list_to_row") * 9;

	private PlayersGUIManager() {
	}

	public static PlayersPageGUI getFirstPage(Player p) {
		return getPage(p, 1);
	}

	public static PlayersPageGUI getPage(Player p, int page) {
		String worldname = p.getWorld().getName();
		SystemWorld sw = SystemWorld.getSystemWorld(worldname);
		if (sw != null) {
			UUID[] members = WorldConfig2.getMembersFiltered(worldname);
			if (members == null || members.length == 0)
				return null;
			int pages = Math.round(members.length / headsPerInv) < 1 ? 1 : Math.round(members.length / headsPerInv);
			if (page > pages)
				return null;
			return getPage(p, page, pages);
		}
		return null;
	}

	public static PlayersPageGUI getPage(Player p, int page, int pages) {
		String worldname = p.getWorld().getName();
		SystemWorld sw = SystemWorld.getSystemWorld(worldname);
		if (sw != null) {
			UUID[] members = WorldConfig2.getMembersFiltered(worldname);
			if (members == null || members.length == 0)
				return null;
			UUID[] uuids = new UUID[headsPerInv + 1];

			int startPos = pages == 1 ? 0 : headsPerInv * (page - 1);
			int length = pages == 1 ? members.length : headsPerInv;

			uuids = Arrays.copyOfRange(members, startPos, startPos + length);

			int pageBefore = page == 1 ? pages : page - 1;
			int nextPage = pages == page ? 1 : page + 1;

			PlayersPageGUI ppg = new PlayersPageGUI(page, p.getUniqueId(), uuids, nextPage, pageBefore);
			return ppg;
		}
		return null;
	}
}
