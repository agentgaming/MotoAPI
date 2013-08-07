package com.mike724.motoapi.push;

import java.util.HashMap;

public class MotoPushData {
    private String apiKey;
    private HashMap<String,String> data = new HashMap<>();

    public MotoPushData(String apiKey) {
        this.apiKey = apiKey;
    }

    public void addData(String key, String value) {
        data.put(key,value);
    }

    public HashMap<String,String> getData() {
        return data;
    }
}