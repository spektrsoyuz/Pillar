/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.player;

import com.spektrsoyuz.pillar.PillarPlugin;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PillarPlayerManager {

    private final PillarPlugin plugin;
    private final Map<UUID, PillarPlayer> players;

    // Constructor
    public PillarPlayerManager(final PillarPlugin plugin) {
        this.plugin = plugin;
        this.players = new ConcurrentHashMap<>();
    }

    // Load a PillarPlayer from the database and store in cache
    public void loadPlayer(final Player player) {
        final PillarPlayer current = getPlayer(player);
        if (current != null) {
            current.setUsername(player.getName());
            return;
        }

        final PillarPlayer pillarPlayer = new PillarPlayer(player);
        plugin.getDatabaseManager().queryPillarPlayer(player.getUniqueId()).thenAccept(dbPlayer -> {
            if (dbPlayer != null) {
                pillarPlayer.setUsername(dbPlayer.getUsername());
                pillarPlayer.setBackLocation(dbPlayer.getBackLocation());
            } else {
                plugin.getDatabaseManager().savePillarPlayer(pillarPlayer);
            }
        });

        players.put(player.getUniqueId(), pillarPlayer);
    }

    // Get a PillarPlayer from a Player
    public PillarPlayer getPlayer(final Player player) {
        return players.get(player.getUniqueId());
    }

    // Get a PillarPlayer from a UUID
    public PillarPlayer getPlayer(final UUID mojangId) {
        return players.get(mojangId);
    }

    // Get a PillarPlayer from a Username
    public PillarPlayer getPlayer(final String username) {
        for (final PillarPlayer player : players.values()) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    // Get a list of all PillarPlayer instances in cache
    public List<PillarPlayer> getPlayers() {
        return new ArrayList<>(players.values());
    }

    // Save all PillarPlayer instances in cache to the database
    public void saveAll() {
        for (final PillarPlayer pillarPlayer : players.values()) {
            plugin.getDatabaseManager().savePillarPlayer(pillarPlayer);
        }
    }
}
