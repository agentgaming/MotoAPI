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

    public Leaderboard getLeaderboard(String type) {
        return (Leaderboard) storage.getObject(Leaderboard.class, type);
    }

    public void saveLeaderboard(Leaderboard l) {
        storage.writeObject((Object) l, l.getName());
    }
}
