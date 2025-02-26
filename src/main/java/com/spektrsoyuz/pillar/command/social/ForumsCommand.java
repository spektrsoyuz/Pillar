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

@SuppressWarnings({"UnstableApiUsage"})
public class ForumsCommand {

    // Constructor
    public ForumsCommand(final PillarPlugin plugin, final Commands registrar) {
        final ConfigManager config = plugin.getConfigManager();

        final LiteralCommandNode<CommandSourceStack> node = Commands.literal("forums")
                .requires(stack -> stack.getSender().hasPermission("pillar.command.forums"))
                .executes(context -> {
                    final CommandSender sender = context.getSource().getSender();
                    sender.sendMessage(config.getMessage("command-social-forums"));
                    return Command.SINGLE_SUCCESS;
                })
                .build();

        registrar.register(node);
    }
}
