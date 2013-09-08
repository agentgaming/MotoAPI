package com.mike724.motoapi.games;

public abstract class GameState {

    private String name;

    public GameState(String name) {
        this.name = name;
    }

    public abstract void loop();

    public abstract void setupState();

    public abstract void endState();

}
