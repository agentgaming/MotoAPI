package com.mike724.motoapi;

import com.mike724.motoapi.storage.Storage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MotoAPI extends JavaPlugin {

    private static MotoAPI instance;
    private Storage storage;

	@Override
	public void onEnable() {
        instance = this;

        this.getLogger().info("MotoAPI Enabled");
	}
	
	@Override
	public void onDisable() {
		this.getLogger().info("MotoAPI Disabled");
	}

    public void killServer() {
        Bukkit.shutdown();
    }

    public static MotoAPI getInstance() {
        return instance;
    }
}