package com.mike724.motoapi.interfaces;

import org.bukkit.entity.Player;

public class InterfaceClick {
    private Player p;
    private Boolean enabled;

    public InterfaceClick(Player p, Boolean enabled) {
        this.enabled = enabled;
        this.p = p;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public Player getPlayer() {
        return p;
    }
}
