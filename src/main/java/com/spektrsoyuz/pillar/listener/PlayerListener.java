package com.spektrsoyuz.pillar.listener;

import com.spektrsoyuz.pillar.PillarPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class PlayerListener implements Listener {

    private final PillarPlugin plugin;

    public PlayerListener(final PillarPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        final Location from = event.getFrom();
        final Location to = event.getTo();
    }
}
