package com.development.trainerlord.addlight.listeners;

/*
 * Copyright 2017 hexosse
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import com.github.hexocraft.addlight.AddLightPlugin;
import com.github.hexocraft.addlight.LightsApi;
import com.github.hexocraft.addlight.configuration.Permissions;
//import com.github.hexocraftapi.message.predifined.message.SimplePrefixedMessage;
//import com.github.hexocraftapi.message.predifined.message.WarningPrefixedMessage;
//import com.github.hexocraftapi.reflection.minecraft.Minecraft;
//import com.github.hexocraftapi.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

import static com.github.hexocraft.addlight.AddLightPlugin.*;
import static com.github.hexocraft.addlight.commands.AlCommands.prefix;

/**
 * This file is part AddGlow
 *
 * @author <b>hexosse</b> (<a href="https://github.comp/hexosse">hexosse on GitHub</a>))
 */
@SuppressWarnings("unused")
public class PlayerListener implements Listener
{
    public PlayerListener(AddLightPlugin plugin)
    {
        super();
    }

    @EventHandler()
    public void onPlayerInteract(PlayerQuitEvent event)
    {
        LightsApi.remove(event.getPlayer());
    }


    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        // Get the player
        final Player player = event.getPlayer();

        //
        if(!LightsApi.isEnable(player)) return;

        //
        LightsApi.LigthPlayer lightPlayer = LightsApi.getPlayer(player);

        //
        if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)
           && player.getInventory().getItemInMainHand() != null//.getItemInHand(player) != null
           && player.getInventory().getItemInMainHand().getType() == Material.GLOWSTONE_DUST
           && Permissions.has(player, Permissions.USE))
        {
            // Clicked location
            Location clickedLoc = event.getClickedBlock().getLocation();

            // Player bypass cost
            boolean byPass = Permissions.has(player, Permissions.BYPASS_COST);

            // Glowstone cost for creating light
            if(!byPass && config.costGlowstone > 0 && player.getInventory().getItemInMainHand().getAmount() < config.costGlowstone)
            {
                // Cancel event
                event.setCancelled(true);
                // Relight, as ligth might have disappeared
                LightsApi.reLight(player, clickedLoc);
                // Send message
                //WarningPrefixedMessage.toPlayer(player, prefix, messages.eCostGlowstone); //TODO New Message System
                return;
            }

            // Money cost for creating light
            if(!byPass && config.costMoney > 0 && economy!=null && !economy.has(player, config.costMoney))
            {
                event.setCancelled(true);
                // Relight, as ligth might have disappeared
                LightsApi.reLight(player, clickedLoc);
                // Send message
                //WarningPrefixedMessage.toPlayer(player, prefix, messages.eCostMoney);//TODO New Message System
                return;
            }

            // Pay for creating ligths
            if(!byPass && config.costGlowstone > 0)
            {
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - config.costGlowstone);
                // Send message
                String message =String.format(messages.sCostGlowstone, config.costGlowstone);
               // SimplePrefixedMessage.toPlayer(player, prefix, message); //TODO New Message System
            }
            if(!byPass && config.costMoney > 0 && economy!=null)
            {
                economy.withdrawPlayer(player, config.costMoney);
                // Send message
                String message =String.format(messages.sCostMoney, config.costMoney, economy.currencyNamePlural());
                //SimplePrefixedMessage.toPlayer(player, prefix, message); //TODO New Message System
            }

            // Création de la lumière
            LightsApi.createLight(player, clickedLoc, lightPlayer.lightlevel, lightPlayer.connectedBlocks);
        }

        //
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
           && player.getInventory().getItemInMainHand() != null
           && player.getInventory().getItemInMainHand().getType() == Material.GLOWSTONE_DUST
           && Permissions.has(player, Permissions.USE))
        {
            /*if(Bukkit.getVersion()//Minecraft.Version.getVersion().newerThan(Minecraft.Version.v1_8_R4))
            {
                EquipmentSlot e = event.getHand();
                if(e.equals(EquipmentSlot.OFF_HAND))
                    return;
            }*/

            // Clicked location
            Location clickedLoc = event.getClickedBlock().getLocation();

            // Suppression de la lumière
            LightsApi.removeLight(player, clickedLoc, lightPlayer.connectedBlocks);
        }

//        //
//        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
//           && PlayerUtil.getItemInHand(player) != null
//           && PlayerUtil.getItemInHand(player).getType() == Material.AIR
//           && Permissions.has(player, Permissions.DEBUG))
//        {
//            if(Minecraft.Version.getVersion().newerThan(Minecraft.Version.v1_8_R4))
//            {
//                EquipmentSlot e = event.getHand();
//                if(e.equals(EquipmentSlot.OFF_HAND))
//                    return;
//            }
//
//            // Clicked location
//            Location clickedLoc = event.getClickedBlock().getLocation();
//
//            // Brightness
//            SimpleMessage.toPlayer(player, "Block : " + clickedLoc.getBlock().getState().getData());
//            SimpleMessage.toPlayer(player, "Location : " + clickedLoc.getBlockX() + " ," + clickedLoc.getBlockY() + " ," + clickedLoc.getBlockZ());
//            SimpleMessage.toPlayer(player, "Brightness (BLOCK) : " + NmsChunkUtil.getBrightness(0, clickedLoc));
//            SimpleMessage.toPlayer(player, "Brightness (SKY) : " + NmsChunkUtil.getBrightness(15, clickedLoc));
//        }
    }
}
