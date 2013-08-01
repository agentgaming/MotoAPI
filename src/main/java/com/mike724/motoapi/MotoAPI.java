package com.mike724.motoapi;

import org.bukkit.plugin.java.JavaPlugin;

public class MotoAPI extends JavaPlugin {

    private static MotoAPI instance;

	@Override
	public void onEnable() {
        instance = this;
		this.getLogger().info("MotoAPI Enabled");
	}
	
	@Override
	public void onDisable() {
		this.getLogger().info("MotoAPI Disabled");
	}

    public static MotoAPI getInstance() {
        return instance;
    }
}