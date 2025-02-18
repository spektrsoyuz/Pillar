/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.command.social;

import com.mojang.brigadier.Command;
import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.config.ConfigManager;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;

import java.util.List;

@SuppressWarnings({"UnstableApiUsage"})
public class TwitterCommand {

    // Constructor
    public TwitterCommand(final PillarPlugin plugin, final Commands registrar) {
        final ConfigManager config = plugin.getConfigManager();

        var node = Commands.literal("twitter")
                .requires(stack -> stack.getSender().hasPermission("pillar.command.twitter"))
                .executes(context -> {
                    final CommandSender sender = context.getSource().getSender();
                    sender.sendMessage(config.getMessage("command-social-twitter"));
                    return Command.SINGLE_SUCCESS;
                })
                .build();

        registrar.register(node, List.of("x"));
    }
}
