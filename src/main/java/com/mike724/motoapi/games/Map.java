package com.mike724.motoapi.games;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;

@SuppressWarnings("unused")
public class Map {

    private boolean mapSaving;

    public Map(File json) throws Exception {
        FileReader fr = new FileReader(json);
        BufferedReader br = new BufferedReader(fr);
        String jsonString = br.readLine();
        if(jsonString == null || jsonString.isEmpty()) {
            throw new Exception("Null or empty JSON string");
        }
        readJSON(jsonString);
    }

    public void readJSON(String jsonString) {
        JSONObject json = (JSONObject) JSONValue.parse(jsonString);

        //Read values
        Object obj = json.get("map_saving_enabled");
        mapSaving = (Boolean) obj;
        System.out.println("Object returned: "+obj.toString());
    }


    public boolean isMapSavingEnabled() {
        return mapSaving;
    }
}
