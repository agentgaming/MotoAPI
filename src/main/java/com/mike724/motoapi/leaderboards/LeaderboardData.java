package com.mike724.motoapi.leaderboards;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaderboardData {
    private String player;
    private ArrayList<String> sortKeys;
    private HashMap<String,Integer> statsMap = new HashMap<>();

    public LeaderboardData(String player, ArrayList<String> sortKeys) {
        this.player = player;
        this.sortKeys = sortKeys;
    }

    public String getPlayer() {
        return player;
    }

    public void addStat(String key, Integer value) {
        statsMap.put(key,value);
    }

    public Integer getStat(String key) {
        return statsMap.get(key);
    }
}
