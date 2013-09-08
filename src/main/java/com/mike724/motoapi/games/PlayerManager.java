package com.mike724.motoapi.games;

import org.bukkit.entity.Player;

public abstract class PlayerManager {

    public PlayerManager() {

    }

    public abstract void addPlayers(Player... players);

    public abstract void removePlayers(Player... players);

    public abstract void removeAllPlayers();

    public abstract Player[] getAllPlayers();

}