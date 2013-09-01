package com.mike724.motoapi.push;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MotoPushReconnect extends Event {
    private static final HandlerList handlers = new HandlerList();

    public MotoPushReconnect() {
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}