/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.command.server;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.PillarUtils;
import com.spektrsoyuz.pillar.config.ConfigManager;
import com.spektrsoyuz.pillar.config.ConfigPlaceholder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;

@SuppressWarnings({"UnstableApiUsage"})
public class BroadcastCommand {

    // Constructor
    public BroadcastCommand(final PillarPlugin plugin, final Commands registrar) {
        final ConfigManager config = plugin.getConfigManager();

        final LiteralCommandNode<CommandSourceStack> node = Commands.literal("broadcast")
                .requires(stack -> stack.getSender().hasPermission(PillarUtils.PERMISSION_COMMAND_BROADCAST))
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            final String message = context.getArgument("message", String.class);
                            final Component component = config.getMessage("command-broadcast", new ConfigPlaceholder("message", message));
                            plugin.getServer().broadcast(component);
                            return Command.SINGLE_SUCCESS;
                        }))
                .build();

        registrar.register(node);
    }
}
