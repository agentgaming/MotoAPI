package com.mike724.motoapi.leaderboards;

import java.util.ArrayList;

@Deprecated
public class Leaderboard {
    private String name;
    public ArrayList<LeaderboardData> data;

    public Leaderboard(String name) {
        this.name = name;
        this.data = new ArrayList<LeaderboardData>();
    }

    public String getName() {
        return name;
    }

    public void addData(LeaderboardData data) {
        this.data.add(data);
    }

    public ArrayList<LeaderboardData> getData() {
        return data;
    }
}
