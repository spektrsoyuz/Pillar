package com.spektrsoyuz.pillar.task;

import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.home.PlayerHome;
import org.bukkit.entity.Player;

public final class SavePlayerHomeTask implements Runnable {

    private final PillarPlugin plugin;

    // Constructor
    public SavePlayerHomeTask(PillarPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (final Player player : plugin.getServer().getOnlinePlayers()) {
            for (final PlayerHome home : plugin.getHomeManager().getHomes(player.getUniqueId())) {
                plugin.getDatabaseManager().savePlayerHome(home);
            }
        }
    }
}
