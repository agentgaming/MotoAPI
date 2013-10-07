package com.mike724.motoapi.games;

public class MapMeta {
    private String displayName;
    private String[] authors;
    private Boolean mapSaving;

    public String[] getAuthors() {
        return authors;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Boolean isSavingMap() {
        return mapSaving;
    }
}
