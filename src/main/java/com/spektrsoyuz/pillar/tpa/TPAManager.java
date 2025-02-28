/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.tpa;

import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.config.ConfigManager;
import com.spektrsoyuz.pillar.config.ConfigPlaceholder;
import org.bukkit.entity.Player;

import java.util.*;

public final class TPAManager {

    private final ConfigManager config;
    private final Map<Player, Set<TeleportRequest>> requests;

    // Constructor
    public TPAManager(final PillarPlugin plugin) {
        this.config = plugin.getConfigManager();
        this.requests = new HashMap<>();
    }

    // Remove all requests for a player
    public void removeRequests(final Player player) {
        requests.values().forEach(requests -> requests.removeIf(request -> request.player().equals(player)));
        requests.values().removeIf(Collection::isEmpty);
        requests.remove(player);
    }

    // Get a request between two players
    public Optional<TeleportRequest> getRequest(final Player player, final Player target) {
        return getRequests(player).stream()
                .filter(request -> request.player().equals(target))
                .findAny();
    }

    // Get all requests for a player
    public List<TeleportRequest> getRequests(final Player player) {
        var requests = this.requests.get(player);
        if (requests == null) return List.of();
        return List.copyOf(requests);
    }

    // Add a new request
    public boolean addRequest(final Player player, final Player sender, final TeleportRequestType type) {
        var players = requests.computeIfAbsent(player, ignored -> new HashSet<>());
        return players.stream().noneMatch(request -> request.player().equals(sender))
                && players.add(new TeleportRequest(sender, type));
    }

    // Expire a request
    public void expireRequest(final Player player, final Player sender, final TeleportRequestType type) {
        if (!removeRequest(player, sender, type)) return;
        sender.sendMessage(config.getMessage(player, "command-tpa-timeout_self", new ConfigPlaceholder("player", player.getName())));
        player.sendMessage(config.getMessage(player, "command-tpa-timeout", new ConfigPlaceholder("player", sender.getName())));
    }

    // Remove a request between two players
    public boolean removeRequest(final Player sender, final Player player, final TeleportRequestType type) {
        var players = requests.get(sender);
        var result = players != null && players.removeIf(request ->
                request.player().equals(player) && request.type() == type);
        if (players != null && players.isEmpty()) requests.remove(sender);
        return result;
    }
}
