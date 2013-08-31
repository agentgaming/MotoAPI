package com.mike724.motoapi.storage.defaults;

@SuppressWarnings("unused")
public enum NetworkRank {
    USER(1),
    BUILDER(2),
    MOD(100),
    ADMIN(200),
    OWNER(1000);

    private final int permission;

    private NetworkRank(int permission) {
        this.permission = permission;
    }

    public int getPermission() {
        return permission;
    }
}