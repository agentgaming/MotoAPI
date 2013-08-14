package com.mike724.motoapi.storage.defaults;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class NetworkPlayer {
    private final String player;
    private boolean isBanned;
    private NetworkRank rank;
    private long joinDate;
    private String currentIP;
    private ArrayList<String> friends;

    public NetworkPlayer(String player, Boolean isBanned, NetworkRank rank, long joinDate) {
        this.player = player;
        this.isBanned = isBanned;
        this.rank = rank;
        this.joinDate = joinDate;
    }

    public NetworkPlayer(String player) {
        this(player, false, NetworkRank.USER, System.currentTimeMillis());
    }

    public String getPlayer() {
        return player;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public NetworkRank getRank() {
        return rank;
    }

    public void setRank(NetworkRank rank) {
        this.rank = rank;
    }

    public long getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(long joinDate) {
        this.joinDate = joinDate;
    }

    public void addFriend(String p) {
        friends.add(p);
    }

    public ArrayList<String> getFriends() {
        return friends;
    }
}