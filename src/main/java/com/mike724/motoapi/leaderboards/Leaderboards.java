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

    public Leaderboard getLeaderboard(String type_moto_really_wanted_this_name, String name) {
        return (Leaderboard) storage.getObject(Leaderboard.class, type_moto_really_wanted_this_name + ":" + name);
    }

    public void saveLeaderboard(String type_moto_really_wanted_this_name, Leaderboard l) {
        storage.writeObject((Object) l, type_moto_really_wanted_this_name + ":" + l.getName());
    }
}
