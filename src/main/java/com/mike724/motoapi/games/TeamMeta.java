package com.mike724.motoapi.games;

@SuppressWarnings("unused")
public class TeamMeta {

    private String name;
    private int maxPlayers;

    public TeamMeta(String name, int maxPlayers) {
        this.name = name;
        this.maxPlayers = maxPlayers;
    }

    public TeamMeta(String name) {
        this.name = name;
        this.maxPlayers = -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasPlayerLimit() {
        return getMaxPlayers() != -1;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
}
