package com.mike724.motoapi.games;

import org.bukkit.entity.Player;

public interface PlayerManager {

    void addPlayers(Player... players);

    void removePlayers(Player... players);

    void removeAllPlayers();

    Player[] getAllPlayers();

}