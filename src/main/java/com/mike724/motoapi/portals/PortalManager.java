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

    @SuppressWarnings("unused")
    public Integer registerPortal(Block mainBlock) {
        int blockCount = 0;
        ArrayList<Block> blocks = new ArrayList<>();
        ArrayList<Block> portalBlocks = new ArrayList<>();

        blocks.add(mainBlock);

        while (blocks.size() > 0) {
            Block test = blocks.get(0);
            if (portalBlocks.contains(test)) continue;
            if (test.getType() == Material.STATIONARY_WATER) {
                portalBlocks.add(test);
                blockCount++;
                for (BlockFace face : facesToCheck) {
                    Block relBlock = test.getRelative(face);
                    if (relBlock.getType() == Material.STATIONARY_WATER) {
                        if (!portalBlocks.contains(relBlock)) blocks.add(relBlock);
                    }
                }
            }
            blocks.remove(test);
            if (blockCount >= 64) break;
        }
        if (portalBlocks.size() < 1) return null;

        Integer idx = portals.size();
        portals.put(idx, portalBlocks);
        return idx;
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
