package com.mike724.motoapi.storage.defaults;

public enum NetworkRank {
    USER(1),
    BUILDER(2),
    MODERATOR(100),
    ADMINISTRATOR(200),
    OWNER(1000);

    private final int permission;

    private NetworkRank(int permission) {
        this.permission = permission;
    }

    public int getPermission() {
        return permission;
    }
}