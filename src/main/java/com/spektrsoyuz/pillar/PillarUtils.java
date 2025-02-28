/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.spektrsoyuz.pillar.config.ConfigPlaceholder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings({"UnstableApiUsage"})
public final class PillarUtils {

    public static final int CONFIG_VERSION = 1;

    public static final String PERMISSION_COMMAND_BACK = "pillar.command.back";
    public static final String PERMISSION_COMMAND_GAMEMODE = "pillar.command.gamemode";
    public static final String PERMISSION_COMMAND_GAMEMODE_OTHER = "pillar.command.gamemode.other";
    public static final String PERMISSION_COMMAND_PING = "pillar.command.ping";
    public static final String PERMISSION_COMMAND_BROADCAST = "pillar.command.broadcast";
    public static final String PERMISSION_COMMAND_SOCIAL = "pillar.command.social";

    // Method to set the GameMode of a Player
    public static int setGamemode(final PillarPlugin plugin, final CommandContext<CommandSourceStack> context, GameMode gameMode) {
        final Player player = (Player) context.getSource().getSender();
        final String gameModeString = StringUtils.capitalize(gameMode.name().toLowerCase());

        player.setGameMode(gameMode);
        player.sendMessage(plugin.getConfigManager().getMessage("command-gamemode", new ConfigPlaceholder("gamemode", gameModeString)));
        return Command.SINGLE_SUCCESS;
    }

    // Method to set the GameMode of a Player from an external sender
    public static int setGamemodeOther(final PillarPlugin plugin, final CommandContext<CommandSourceStack> context, GameMode gameMode) {
        final CommandSender sender = context.getSource().getSender();
        final String gameModeString = StringUtils.capitalize(gameMode.name().toLowerCase());
        final PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);

        try {
            final Player target = targetResolver.resolve(context.getSource()).getFirst();

            target.setGameMode(gameMode);
            target.sendMessage(plugin.getConfigManager().getMessage("command-gamemode", new ConfigPlaceholder("gamemode", gameModeString)));
            sender.sendMessage(plugin.getConfigManager().getMessage("command-gamemode-other",
                    new ConfigPlaceholder("gamemode", gameModeString),
                    new ConfigPlaceholder("target", target.getName())));
        } catch (CommandSyntaxException e) {
            sender.sendMessage(Component.text(e.getMessage(), NamedTextColor.RED));
        }
        return Command.SINGLE_SUCCESS;
    }
}
