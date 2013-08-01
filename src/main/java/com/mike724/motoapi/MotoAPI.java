package com.mike724.motoapi;

import org.bukkit.plugin.java.JavaPlugin;

public class MotoAPI extends JavaPlugin {

	@Override
	public void onEnable() {
		this.getLogger().info("MotoAPI Enabled");
	}
	
	@Override
	public void onDisable() {
		this.getLogger().info("MotoAPI Disabled");
	}
}