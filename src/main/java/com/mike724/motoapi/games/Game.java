package com.mike724.motoapi.games;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Game implements Runnable {

    private String displayName;
    private GameState defaultState;
    private GameState currentState;
    private PlayerManager playerManager;

    public Game(String displayName, GameState defaultState, PlayerManager playerManager) {
        this.displayName = displayName;
        this.defaultState = defaultState;
        this.playerManager = playerManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public void startDefaultState() {
        startState(this.defaultState);
    }

    public void startState(GameState state) {
        endCurrentState();
        state.setupState();
        this.currentState = state;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void endCurrentState() {
        GameState current = this.getCurrentState();
        if(current != null) {
            current.endState();
        }
    }

    /** Announces the given message with a universal format */
    public void announce(String message) {
        Bukkit.broadcastMessage(ChatColor.DARK_RED+""+ChatColor.BOLD+"["+displayName+"] "+ChatColor.WHITE+message);
    }

    @Override
    public void run() {
        GameState state = getCurrentState();
        if(state != null) {
            state.loop();
        }
    }
}
