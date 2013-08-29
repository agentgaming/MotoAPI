package com.mike724.motoapi;

import com.mike724.motoapi.portals.PortalEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MotoAPI extends JavaPlugin {

    private static MotoAPI instance;

    @Override
    public void onEnable() {
        instance = this;

        this.getServer().getPluginManager().registerEvents(new PortalEvents(), this);

        this.getLogger().info("MotoAPI Enabled");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("MotoAPI Disabled");
    }

    @SuppressWarnings("unused")
    public void killServer() {
        Bukkit.shutdown();
    }

    public static MotoAPI getInstance() {
        return instance;
    }
}