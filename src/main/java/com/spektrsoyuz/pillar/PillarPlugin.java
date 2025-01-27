/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar;

import com.spektrsoyuz.pillar.command.server.BroadcastCommand;
import com.spektrsoyuz.pillar.config.ConfigManager;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings({"UnstableApiUsage"})
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
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigManager config() {
        return configManager;
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            final Commands registrar = commands.registrar();

            new BroadcastCommand(this).register(registrar);
        });
    }

    private void registerListeners() {

    }
}
