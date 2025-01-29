package com.spektrsoyuz.pillar.tpa;

import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.config.ConfigManager;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.*;

public class TPAManager {

    private final ConfigManager config;
    private final Map<Player, Set<TeleportRequest>> requests;

    public TPAManager(final PillarPlugin plugin) {
        this.config = plugin.getConfigManager();
        this.requests = new HashMap<>();
    }

    public void removeRequests(final Player player) {
        requests.values().forEach(requests -> requests.removeIf(request -> request.player().equals(player)));
        requests.values().removeIf(Collection::isEmpty);
        requests.remove(player);
    }

    public Optional<TeleportRequest> getRequest(final Player player, final Player target) {
        return getRequests(player).stream()
                .filter(request -> request.player().equals(target))
                .findAny();
    }

    public List<TeleportRequest> getRequests(final Player player) {
        var requests = this.requests.get(player);
        if (requests == null) return List.of();
        return List.copyOf(requests);
    }

    public boolean addRequest(final Player player, final Player sender, final TeleportRequestType type) {
        var players = requests.computeIfAbsent(player, ignored -> new HashSet<>());
        return players.stream().noneMatch(request -> request.player().equals(sender))
                && players.add(new TeleportRequest(sender, type));
    }

    public void expireRequest(final Player player, final Player sender, final TeleportRequestType type) {
        if (!removeRequest(player, sender, type)) return;
        sender.sendMessage(config.getMessage("command.tpa.timeout.self", null, Placeholder.parsed("player", player.getName()), config.papiTag(player)));
        player.sendMessage(config.getMessage("command.tpa.timeout", null, Placeholder.parsed("player", sender.getName()), config.papiTag(sender)));
    }

    public boolean removeRequest(final Player sender, final Player player, final TeleportRequestType type) {
        var players = requests.get(sender);
        var result = players != null && players.removeIf(request ->
                request.player().equals(player) && request.type() == type);
        if (players != null && players.isEmpty()) requests.remove(sender);
        return result;
    }
}
