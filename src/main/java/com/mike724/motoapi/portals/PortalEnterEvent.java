package com.mike724.motoapi.portals;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PortalEnterEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player p;
    private Integer i;


    public PortalEnterEvent(Player p, Integer i) {
        this.p = p;
        this.i = i;
    }

    public Player getPlayer() {
        return p;
    }

    public Integer getPortal() {
        return i;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
