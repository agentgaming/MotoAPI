package com.mike724.motoapi.leaderboards;

import com.mike724.motoapi.storage.DataStorage;

public class Leaderboards {
    private DataStorage storage;

    public Leaderboards() {
        try {
            this.storage = new DataStorage("","","");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Leaderboard getLeaderboard(String type, String name) {
        return (Leaderboard) storage.getObject(Leaderboard.class, type + ":" + name);
    }

    public void saveLeaderboard(String type, Leaderboard l) {
        storage.writeObject((Object) l, type + ":" + l.getName());
    }
}
