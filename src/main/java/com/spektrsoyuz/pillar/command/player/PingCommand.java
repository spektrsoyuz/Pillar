/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.command.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.PillarUtils;
import com.spektrsoyuz.pillar.config.ConfigPlaceholder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings({"UnstableApiUsage"})
public final class PingCommand {

    // Constructor
    public PingCommand(final PillarPlugin plugin, final Commands registrar) {
        final LiteralCommandNode<CommandSourceStack> node = Commands.literal("ping")
                .requires(stack -> {
                    final CommandSender sender = stack.getSender();
                    return sender.hasPermission(PillarUtils.PERMISSION_COMMAND_PING) && sender instanceof Player;
                })
                .executes(context -> {
                    final Player player = (Player) context.getSource().getSender();

                    player.sendMessage(plugin.getConfigManager().getMessage("command-ping", new ConfigPlaceholder("ping", String.valueOf(player.getPing()))));
                    return Command.SINGLE_SUCCESS;
                })
                .build();

        registrar.register(node, "Check your ping");
    }
}
