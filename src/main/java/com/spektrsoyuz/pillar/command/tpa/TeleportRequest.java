package com.spektrsoyuz.pillar.command.tpa;

import org.bukkit.entity.Player;

public record TeleportRequest(Player player, TeleportRequestType type) {
}
