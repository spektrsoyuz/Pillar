package com.spektrsoyuz.pillar.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public final class PillarPlayer {

    private final UUID mojangId;
    private String username;
    private Location backLocation;

    // Constructor
    // Create instance of PillarPlayer from Player
    public PillarPlayer(final Player player) {
        this.mojangId = player.getUniqueId();
        this.username = player.getName();
        this.backLocation = player.getLocation();
    }

    // Constructor
    // Create instance of PillarPlayer by specifying existing properties
    public PillarPlayer(final UUID mojangId, final String username, final Location backLocation) {
        this.mojangId = mojangId;
        this.username = username;
        this.backLocation = backLocation;
    }
}
