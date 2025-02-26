/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.command.social;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.config.ConfigManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;

import java.util.List;

@SuppressWarnings({"UnstableApiUsage"})
public class YoutubeCommand {

    // Constructor
    public YoutubeCommand(final PillarPlugin plugin, final Commands registrar) {
        final ConfigManager config = plugin.getConfigManager();

        final LiteralCommandNode<CommandSourceStack> node = Commands.literal("youtube")
                .requires(stack -> stack.getSender().hasPermission("pillar.command.youtube"))
                .executes(context -> {
                    final CommandSender sender = context.getSource().getSender();
                    sender.sendMessage(config.getMessage("command-social-youtube"));
                    return Command.SINGLE_SUCCESS;
                })
                .build();

        registrar.register(node, List.of("yt)"));
    }
}
