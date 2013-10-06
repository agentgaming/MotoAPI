package com.mike724.motoapi.games;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.util.HashMap;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class MapManager {

    private Logger infoLog;
    private HashMap<String, Map> registered;
    private HashMap<String, World> loaded;

    public MapManager() {
        infoLog = Logger.getLogger("MapManager");
        registered = new HashMap<>();
        loaded = new HashMap<>();
    }

    public void registerMap(String name, Map map) {
        if (!registered.containsKey(name)) {
            registered.put(name, map);
        }
    }

    public void unregisterMap(String name) {
        if (loaded.containsKey(name)) {
            loaded.remove(name);
        }
    }

    public boolean loadMap(String name) {
        if (!registered.containsKey(name)) {
            return false;
        }
        Map m = this.registered.get(name);
        World w = new WorldCreator(name).createWorld();
        if (w == null) {
            return false;
        } else {
            loaded.put(name, w);
            if (m.isMapSavingEnabled()) {
                w.setAutoSave(false);
            }
            infoLog.info("Loaded map: " + name);
            return true;
        }
    }

    public boolean unloadMap(String name) {
        if (loaded.containsKey(name)) {
            Map m = registered.get(name);
            Bukkit.unloadWorld(name, m.isMapSavingEnabled());
            infoLog.info("Unloaded map: " + name);
            return true;
        } else {
            return false;
        }
    }

}
