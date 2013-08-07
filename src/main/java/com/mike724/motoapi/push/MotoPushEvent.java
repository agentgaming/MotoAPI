package com.mike724.motoapi.push;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MotoPushEvent extends Event{
    private static final HandlerList handlers = new HandlerList();
    private MotoPushData data;
    private boolean cancelled;

    public MotoPushEvent(MotoPushData data) {
        this.data = data;
    }

    public MotoPushData getPushData() {
        return data;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
