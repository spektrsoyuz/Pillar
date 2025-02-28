package com.spektrsoyuz.pillar.task;

import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.player.PillarPlayer;
import org.bukkit.entity.Player;

public final class SavePillarPlayerTask implements Runnable {

    private final PillarPlugin plugin;

    // Constructor
    public SavePillarPlayerTask(PillarPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            PillarPlayer pillarPlayer = plugin.getPillarPlayerManager().getPlayer(player);
            if (pillarPlayer != null) {
                plugin.getDatabaseManager().savePillarPlayer(pillarPlayer);
            }
        }
    }
}
