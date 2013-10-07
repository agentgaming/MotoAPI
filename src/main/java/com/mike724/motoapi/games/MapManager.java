package com.mike724.motoapi.games;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class MapManager {

    private Logger infoLog;
    private HashMap<String, Map> registered;
    private HashMap<String, World> loaded;
    public final static String METADATA_FILE_NAME = "meta.json";

    public MapManager() {
        infoLog = Logger.getLogger("MapManager");
        registered = new HashMap<>();
        loaded = new HashMap<>();
    }

    public void registerAllInDirectory(File dir) {
        if(dir.isDirectory()) {
            File[] maps = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if(pathname.isDirectory()) {
                        return (new File(pathname, METADATA_FILE_NAME)).exists();
                    }
                    return false;
                }
            });
            //Process maps/worlds
            for(File map : maps) {
                try {
                    Map m = new Map(map);
                    this.registerMap(map.getName(), m);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            //Has to be a directory.
            return;
        }
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
