/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.home;

import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.config.ConfigManager;
import com.spektrsoyuz.pillar.config.ConfigPlaceholder;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class HomeManager {

    private final ConfigManager config;
    private final Map<UUID, Set<PlayerHome>> homeMap;

    // Constructor
    public HomeManager(final PillarPlugin plugin) {
        this.config = plugin.getConfigManager();
        this.homeMap = new ConcurrentHashMap<>();
    }

    // Get a set of homes associated with a mojangId
    public Set<PlayerHome> getHomes(final UUID mojangId) {
        return homeMap.get(mojangId);
    }

    public void goToHome(final Player player, final String name) {
        final Set<PlayerHome> homes = getHomes(player.getUniqueId());
        for (final PlayerHome home : homes) {
            if (home.getName().equalsIgnoreCase(name)) {
                player.teleport(home.getLocation());
                player.sendMessage(config.getMessage("command-home-teleport", new ConfigPlaceholder("home", home.getName())));
            }
        }
    }

    public void addHome(final UUID mojangId, final PlayerHome home) {
        final Set<PlayerHome> homes = getHomes(mojangId);
        homes.remove(home);
        homes.add(home);
        homeMap.put(mojangId, homes);
    }

    public void deleteHome(final UUID mojangId, final String name) {
        final Set<PlayerHome> homes = getHomes(mojangId);
        homes.removeIf(home -> home.getName().equalsIgnoreCase(name));
        homeMap.put(mojangId, homes);
    }
}
