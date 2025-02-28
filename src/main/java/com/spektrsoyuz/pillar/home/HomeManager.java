/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.home;

import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.config.ConfigPlaceholder;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class HomeManager {

    private final PillarPlugin plugin;
    private final Map<UUID, List<PlayerHome>> homeMap;

    // Constructor
    public HomeManager(final PillarPlugin plugin) {
        this.plugin = plugin;
        this.homeMap = new ConcurrentHashMap<>();
    }

    public void loadPlayerHomes(final Player player) {
        plugin.getDatabaseManager().queryPlayerHomes(player.getUniqueId()).thenAccept(homes -> {
            if (homes != null) {
                homeMap.put(player.getUniqueId(), homes);
            }
        });
    }

    public void removePlayerHomes(final Player player) {
        for (final PlayerHome home : homeMap.get(player.getUniqueId())) {
            plugin.getDatabaseManager().savePlayerHome(home);
        }
        homeMap.remove(player.getUniqueId());
    }

    // Get a set of homes associated with a mojangId
    public List<PlayerHome> getHomes(final UUID mojangId) {
        return homeMap.get(mojangId);
    }

    public void goToHome(final Player player, final String name) {
        final List<PlayerHome> homes = getHomes(player.getUniqueId());
        for (final PlayerHome home : homes) {
            if (home.getName().equalsIgnoreCase(name)) {
                player.teleport(home.getLocation());
                player.sendMessage(plugin.getConfigManager().getMessage("command-home-teleport", new ConfigPlaceholder("home", home.getName())));
            }
        }
    }

    public void addHome(final UUID mojangId, final PlayerHome home) {
        final List<PlayerHome> homes = getHomes(mojangId);
        homes.remove(home);
        homes.add(home);
        homeMap.put(mojangId, homes);
    }

    public void deleteHome(final UUID mojangId, final String name) {
        final List<PlayerHome> homes = getHomes(mojangId);
        homes.removeIf(home -> home.getName().equalsIgnoreCase(name));
        homeMap.put(mojangId, homes);
    }

    public void saveAll() {
        for (final UUID mojangId : homeMap.keySet()) {
            final List<PlayerHome> homes = homeMap.get(mojangId);
            for (final PlayerHome home : homes) {
                plugin.getDatabaseManager().savePlayerHome(home);
            }
        }
    }
}
