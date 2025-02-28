/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.tpa;

import org.bukkit.entity.Player;

public record TeleportRequest(Player player, TeleportRequestType type) {
}
