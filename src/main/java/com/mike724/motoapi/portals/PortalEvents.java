package com.mike724.motoapi.portals;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class PortalEvents implements Listener {
    private static HashMap<Player, Block> playerBlocks = new HashMap<>();

    @EventHandler
    public static void onPlayerMove(PlayerMoveEvent e) {
        if(e.isCancelled()) return;

        if(playerBlocks.containsKey(e.getPlayer())) {
            if(playerBlocks.get(e.getPlayer()) == e.getTo().getBlock()) return;
        }

        playerBlocks.put(e.getPlayer(), e.getTo().getBlock());

        for(Integer i : PortalManager.getPortals().keySet()) {
            ArrayList<Block> portal = PortalManager.getPortals().get(i);
            if(portal.contains(e.getTo().getBlock())) {
                Bukkit.getServer().getPluginManager().callEvent(new PortalEnterEvent(e.getPlayer(), i));
                break;
            }
        }
    }
}
