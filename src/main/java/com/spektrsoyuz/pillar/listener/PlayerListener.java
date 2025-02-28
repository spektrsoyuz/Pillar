/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.listener;

import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.player.PillarPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class PlayerListener implements Listener {

    private final PillarPlugin plugin;

    public PlayerListener(final PillarPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        plugin.getPillarPlayerManager().loadPlayer(player).thenAccept(pillarPlayer -> {

        });
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final PillarPlayer pillarPlayer = plugin.getPillarPlayerManager().getPlayer(player);

        if (pillarPlayer != null) {
            plugin.getDatabaseManager().savePillarPlayer(pillarPlayer);
        }
    }

    @EventHandler
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        final Location from = event.getFrom();

        final PillarPlayer pillarPlayer = plugin.getPillarPlayerManager().getPlayer(player);
        if (pillarPlayer != null) {
            pillarPlayer.setBackLocation(from);
        }
    }
}
