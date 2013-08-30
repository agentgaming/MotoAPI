package com.mike724.motoapi.portals;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("unused")
public class PortalManager implements Listener {

    private HashMap<Integer, ArrayList<Block>> portals = new HashMap<>();

    private BlockFace[] facesToCheck = {BlockFace.DOWN, BlockFace.UP, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH};

    public PortalManager() {
        portals = new HashMap<>();
    }

    @SuppressWarnings("unused")
    public Integer registerPortal(Block mainBlock) {
        ArrayList<Block> waterBlocks = new ArrayList<>();

        collectBlocks(mainBlock, waterBlocks);

        Integer idx = portals.size();
        portals.put(idx, waterBlocks);
        return idx;
    }

    private void collectBlocks(Block anchor, ArrayList<Block> collected) {
        if (collected.size() >= 64) return;
        if (!anchor.getType().equals(Material.STATIONARY_WATER)) return;
        if (collected.contains(anchor)) return;

        collected.add(anchor);

        for (BlockFace face : facesToCheck) {
            collectBlocks(anchor.getRelative(face), collected);
        }
    }

    @SuppressWarnings("unused")
    public void unregisterPortal(Integer i) {
        if (portals.containsKey(i)) portals.remove(i);
    }

    public HashMap<Integer, ArrayList<Block>> getPortals() {
        return portals;
    }

    //Events
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.isCancelled()) return;

        if (e.getTo().getBlock() == e.getFrom().getBlock()) return;

        for (Integer i : getPortals().keySet()) {
            ArrayList<Block> portal = getPortals().get(i);
            if (portal.contains(e.getTo().getBlock())) {
                Bukkit.getServer().getPluginManager().callEvent(new PortalEnterEvent(e.getPlayer(), i));
                break;
            }
        }
    }
}
