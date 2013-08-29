package com.mike724.motoapi.portals;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class PortalEvents implements Listener {

    @EventHandler
    public static void onPlayerMove(PlayerMoveEvent e) {
        if (e.isCancelled()) return;

        if (e.getTo().getBlock() == e.getFrom().getBlock()) return;

        for (Integer i : PortalManager.getPortals().keySet()) {
            ArrayList<Block> portal = PortalManager.getPortals().get(i);
            if (portal.contains(e.getTo().getBlock())) {
                Bukkit.getServer().getPluginManager().callEvent(new PortalEnterEvent(e.getPlayer(), i));
                break;
            }
        }
    }
}
