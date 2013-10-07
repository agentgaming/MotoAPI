package com.mike724.motoapi.games;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@SuppressWarnings("unused")
public class Map {

    private String displayName;
    private String[] authors;
    private boolean mapSaving;

    public Map(File mapDir) throws Exception {
        if (!mapDir.isDirectory() || !mapDir.exists()) {
            throw new Exception("Map directory does not exist or is not a directory");
        }
        File json = new File(mapDir, MapManager.METADATA_FILE_NAME);
        FileReader fr = new FileReader(json);
        BufferedReader br = new BufferedReader(fr);
        String jsonString = "";
        String line;
        while ((line = br.readLine()) != null) {
            jsonString += line;
        }
        jsonString = jsonString.trim();
        if (jsonString.isEmpty()) {
            throw new Exception("Null or empty JSON string");
        }
        readJSON(jsonString);
    }

    public void readJSON(String jsonString) {
        Gson gson = new Gson();

        MapMeta meta = gson.fromJson(jsonString, MapMeta.class);

        displayName = meta.getDisplayName();
        authors = meta.getAuthors();
        mapSaving = meta.isSavingMap();
    }

    public String getDisplayName() {
        return displayName;
    }

    public String[] getAuthors() {
        return authors;
    }

    public boolean isMapSavingEnabled() {
        return mapSaving;
    }
}
