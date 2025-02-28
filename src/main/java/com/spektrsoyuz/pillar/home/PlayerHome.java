package com.spektrsoyuz.pillar.home;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.UUID;

@Getter
@Setter
public class PlayerHome {

    private final UUID mojangId;
    private final String name;
    private Location location;

    public PlayerHome(final UUID mojangId, final String name, final Location location) {
        this.mojangId = mojangId;
        this.name = name;
        this.location = location;
    }
}
