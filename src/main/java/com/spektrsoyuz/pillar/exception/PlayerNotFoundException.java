package com.spektrsoyuz.pillar.exception;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@SuppressWarnings({"UnstableApiUsage"})
public class PlayerNotFoundException extends SimpleCommandExceptionType {
    public PlayerNotFoundException(final String playerName) {
        super(MessageComponentSerializer.message().serialize(Component.text("Player " + playerName + " not found.", NamedTextColor.RED)));
    }
}
