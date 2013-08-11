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

    public Leaderboard getLeaderboard(String table, String name) {
        return (Leaderboard) storage.getObject(Leaderboard.class,table + ":" + name);
    }

    public void saveLeaderboard(String table, String name, Leaderboard l) {
        storage.writeObject((Object) l, table + ":" + name);
    }
}
