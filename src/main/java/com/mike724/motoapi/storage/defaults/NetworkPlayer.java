package com.mike724.motoapi.storage.defaults;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class NetworkPlayer {
    private final String player;
    private boolean isBanned;
    private NetworkRank rank;
    private long joinDate;
    private ArrayList<String> friends;

    public NetworkPlayer(String player, Boolean isBanned, NetworkRank rank, long joinDate) {
        this.player = player;
        this.isBanned = isBanned;
        this.rank = rank;
        this.joinDate = joinDate;
        this.friends = new ArrayList<>();
    }

    public NetworkPlayer(String player) {
        this(player, false, NetworkRank.USER, System.currentTimeMillis());
    }

    //Player
    public String getPlayer() {
        return player;
    }

    //Banned
    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }


    //Rank
    public NetworkRank getRank() {
        return rank;
    }

    public void setRank(NetworkRank rank) {
        this.rank = rank;
    }

    //Join Date
    public long getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(long joinDate) {
        this.joinDate = joinDate;
    }

    //Friends
    public void addFriend(String p) {
        friends.add(p);
    }

    public void removeFriend(String p) {
        friends.remove(p);
    }

    public ArrayList<String> getFriends() {
        return friends;
    }
}