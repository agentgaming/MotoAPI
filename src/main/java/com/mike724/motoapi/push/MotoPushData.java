package com.mike724.motoapi.push;

import java.util.HashMap;

@SuppressWarnings("unused")
public class MotoPushData {
    private String command;
    private HashMap<String, String> data = new HashMap<>();

    public MotoPushData(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void addData(String key, String value) {
        data.put(key, value);
    }

    public HashMap<String, String> getData() {
        return data;
    }
}
