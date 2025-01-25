/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar;

import com.spektrsoyuz.pillar.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PillarPlugin extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onLoad() {
        // Plugin load logic
        configManager = new ConfigManager(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
