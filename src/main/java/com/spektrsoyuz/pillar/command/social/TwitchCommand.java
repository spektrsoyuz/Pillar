/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.command.social;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.PillarUtils;
import com.spektrsoyuz.pillar.config.ConfigManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;

@SuppressWarnings({"UnstableApiUsage"})
public final class TwitchCommand {

    // Constructor
    public TwitchCommand(final PillarPlugin plugin, final Commands registrar) {
        final ConfigManager config = plugin.getConfigManager();

        final LiteralCommandNode<CommandSourceStack> node = Commands.literal("twitch")
                .requires(stack -> stack.getSender().hasPermission(PillarUtils.PERMISSION_COMMAND_SOCIAL))
                .executes(context -> {
                    final CommandSender sender = context.getSource().getSender();
                    sender.sendMessage(config.getMessage("command-social-twitch"));
                    return Command.SINGLE_SUCCESS;
                })
                .build();

        registrar.register(node);
    }
}
